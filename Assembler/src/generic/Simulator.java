package generic;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Simulator 
{
	static FileInputStream inputcodeStream = null;
	public static void setupSimulation(String assemblyProgramFile, String objectProgramFile)
	{	
		int firstCodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
		ParsedProgram.parseCodeSection(assemblyProgramFile, firstCodeAddress);
		ParsedProgram.printState();
	}
	
	public static void assemble(String objectProgramFile)
	{
		try (FileOutputStream outputfile = new FileOutputStream(objectProgramFile)) 
		{
			
			int number= ParsedProgram.firstCodeAddress;
			byte[] inobject = new byte[4];
	                inobject[0]=(byte)((number >> 24) & 0xFF);
	        	inobject[1]=(byte)((number >> 16) & 0xFF);
	        	inobject[2]=(byte)((number >> 8) & 0xFF);
	        	inobject[3]=(byte)(number & 0xFF);
	        	outputfile.write(inobject);
                	for(int data: ParsedProgram.data)
			{
				inobject[0]=(byte)((data >> 24) & 0xFF); 
				inobject[1]=(byte)((data >> 16) & 0xFF);
				inobject[2]=(byte)((data >> 8) & 0xFF);
				inobject[3]=(byte)(data & 0xFF); 
				outputfile.write(inobject);
			}
			for(Instruction instruction: ParsedProgram.code)
			{

				int opcode = instruction.getOperationType().ordinal();
				String ansstring = String.format("%5s", Integer.toBinaryString(opcode)).replace(' ', '0');
				if(opcode%2==0 && opcode<=21)
				{
					int rs1=instruction.getSourceOperand1().getValue();
					int rs2=instruction.getSourceOperand2().getValue();
					int rd=instruction.getDestinationOperand().getValue();
					ansstring+= String.format("%5s",Integer.toBinaryString(rs1)).replace(' ', '0');
					ansstring+= String.format("%5s",Integer.toBinaryString(rs2)).replace(' ', '0');
					ansstring+= String.format("%5s",Integer.toBinaryString(rd)).replace(' ', '0');
					String padding = String.format("%12s",Integer.toBinaryString(0)).replace(' ', '0');
					ansstring+= padding;
					byte[] ansbytes = new byte[4];
					ansbytes[0]=(byte)Integer.parseInt(ansstring.substring(0,8),2);
					ansbytes[1]=(byte)Integer.parseInt(ansstring.substring(8,16),2);
					ansbytes[2]=(byte)Integer.parseInt(ansstring.substring(16,24),2);
					ansbytes[3]=(byte)Integer.parseInt(ansstring.substring(24,32),2);	
					outputfile.write(ansbytes);
				}
				else if(opcode==24)
				{
					if(instruction.destinationOperand.operandType==Operand.OperandType.Register)
					{
						int rd=instruction.destinationOperand.getValue();
						ansstring+= String.format("%5s",Integer.toBinaryString(rd)).replace(' ', '0');
						String padding = String.format("%22s",Integer.toBinaryString(0)).replace(' ', '0');
						ansstring+= padding;
						byte[] ansbytes = new byte[4];
						ansbytes[0]=(byte)Integer.parseInt(ansstring.substring(0,8),2);
						ansbytes[1]=(byte)Integer.parseInt(ansstring.substring(8,16),2);
						ansbytes[2]=(byte)Integer.parseInt(ansstring.substring(16,24),2);
						ansbytes[3]=(byte)Integer.parseInt(ansstring.substring(24,32),2);	
						outputfile.write(ansbytes);
					}
					else 
					{
						int rd=ParsedProgram.symtab.get(instruction.destinationOperand.labelValue);
						rd=rd-instruction.getProgramCounter();

						String padding = String.format("%5s",Integer.toBinaryString(0)).replace(' ', '0');
						ansstring+= padding;
						ansstring+= String.format("%22s",Integer.toBinaryString(rd& 0x3FFFFF)).replace(' ', '0');
						
						byte[] ansbytes = new byte[4];
						ansbytes[0]=(byte)Integer.parseInt(ansstring.substring(0,8),2);
						ansbytes[1]=(byte)Integer.parseInt(ansstring.substring(8,16),2);
						ansbytes[2]=(byte)Integer.parseInt(ansstring.substring(16,24),2);
						ansbytes[3]=(byte)Integer.parseInt(ansstring.substring(24,32),2);	
						outputfile.write(ansbytes);
					}
				}
				else if(opcode>=25 && opcode<=28)
				{
					int rs1=instruction.getSourceOperand1().getValue();
					int rs2=instruction.getSourceOperand2().getValue();
					int rd=ParsedProgram.symtab.get(instruction.destinationOperand.labelValue);
					rd=rd-instruction.getProgramCounter();
					ansstring+= String.format("%5s",Integer.toBinaryString(rs1)).replace(' ', '0');
					ansstring+= String.format("%5s",Integer.toBinaryString(rs2)).replace(' ', '0');
					ansstring+= String.format("%17s",Integer.toBinaryString(rd&0x1FFFF)).replace(' ', '0');
					byte[] ansbytes = new byte[4];
					ansbytes[0]=(byte)Integer.parseInt(ansstring.substring(0,8),2);
					ansbytes[1]=(byte)Integer.parseInt(ansstring.substring(8,16),2);
					ansbytes[2]=(byte)Integer.parseInt(ansstring.substring(16,24),2);
					ansbytes[3]=(byte)Integer.parseInt(ansstring.substring(24,32),2);	
					outputfile.write(ansbytes);
				}
				else if(opcode==29)
				{
					String padding = String.format("%27s",Integer.toBinaryString(0)).replace(' ', '0');
					ansstring+= padding;
					byte[] ansbytes = new byte[4];
					ansbytes[0]=(byte)Integer.parseInt(ansstring.substring(0,8),2);
					ansbytes[1]=(byte)Integer.parseInt(ansstring.substring(8,16),2);
					ansbytes[2]=(byte)Integer.parseInt(ansstring.substring(16,24),2);
					ansbytes[3]=(byte)Integer.parseInt(ansstring.substring(24,32),2);	
					outputfile.write(ansbytes);
				}
				else 
				{
					int rs1=instruction.getSourceOperand1().getValue();
					int rs2=instruction.getSourceOperand2().getValue();
					int rd=instruction.getDestinationOperand().getValue();
					ansstring+= String.format("%5s",Integer.toBinaryString(rs1)).replace(' ', '0');
					ansstring+= String.format("%5s",Integer.toBinaryString(rd)).replace(' ', '0');
					ansstring+= String.format("%17s",Integer.toBinaryString(rs2&0x1FFFF)).replace(' ', '0');
					byte[] ansbytes = new byte[4];
					ansbytes[0]=(byte)Integer.parseInt(ansstring.substring(0,8),2);
					ansbytes[1]=(byte)Integer.parseInt(ansstring.substring(8,16),2);
					ansbytes[2]=(byte)Integer.parseInt(ansstring.substring(16,24),2);
					ansbytes[3]=(byte)Integer.parseInt(ansstring.substring(24,32),2);	
					outputfile.write(ansbytes);
				}
				
			}
       		} 
        	catch (IOException e) 
        	{
           		System.err.println("Error writing to object file: " + e.getMessage());
            		e.printStackTrace();
        	}
	}
}
