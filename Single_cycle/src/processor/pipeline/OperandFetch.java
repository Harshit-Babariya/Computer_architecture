package processor.pipeline;

import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Operand;
import generic.Operand.OperandType;
import processor.Processor;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	
	
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}
	
	public void performOF()
	{
		
		if(IF_OF_Latch.isOF_enable())
		{
			//TODO
	
			Instruction instruction= new Instruction();
			int inst=IF_OF_Latch.getInstruction();
			String ansstring=String.format("%32s", Integer.toBinaryString(inst)).replace(' ', '0');
			int opcode=Integer.parseInt(ansstring.substring(0,5),2);
			instruction.setOperationType(OperationType.values()[opcode]);
			Operand sourceoperand1=new Operand();
			Operand sourceoperand2=new Operand();
			Operand destinationoperand= new Operand();
			if(opcode%2==0 && opcode<=21)
			{
				int rs1=Integer.parseInt(ansstring.substring(5,10),2);
				int rs2=Integer.parseInt(ansstring.substring(10,15),2);
				int rd=Integer.parseInt(ansstring.substring(15,20),2);
				sourceoperand1.setOperandType(OperandType.Register);
				sourceoperand2.setOperandType(OperandType.Register);
				destinationoperand.setOperandType(OperandType.Register);
				sourceoperand1.setValue(containingProcessor.getRegisterFile().getValue(rs1));
				sourceoperand2.setValue(containingProcessor.getRegisterFile().getValue(rs2));
				destinationoperand.setValue(rd);
				instruction.setSourceOperand1(sourceoperand1);
				instruction.setSourceOperand2(sourceoperand2);
				instruction.setDestinationOperand(destinationoperand);

			}
			else if(opcode==29)
			{
				sourceoperand1.setOperandType(OperandType.Register);
				sourceoperand2.setOperandType(OperandType.Register);
				destinationoperand.setOperandType(OperandType.Register);
				sourceoperand1.setValue(0);
				sourceoperand2.setValue(0);
				destinationoperand.setValue(0);
				instruction.setSourceOperand1(sourceoperand1);
				instruction.setSourceOperand2(sourceoperand2);
				instruction.setDestinationOperand(destinationoperand);
			}
			else if(opcode==24)
			{
				int rd=Integer.parseInt(ansstring.substring(5,10 ),2);
				int immediate=(inst<<10)>>10;
				if(immediate==0)
				{
					destinationoperand.setOperandType(OperandType.Register);
					destinationoperand.setValue(containingProcessor.getRegisterFile().getValue(rd)+containingProcessor.getRegisterFile().getProgramCounter()-1);
					instruction.setDestinationOperand(destinationoperand);

				}
				else
				{
					destinationoperand.setOperandType(OperandType.Label);
					immediate=immediate+containingProcessor.getRegisterFile().getProgramCounter()-1;
					destinationoperand.setValue(immediate);
					instruction.setDestinationOperand(destinationoperand);
				}
				sourceoperand1.setOperandType(OperandType.Register);
				sourceoperand2.setOperandType(OperandType.Register);
				sourceoperand1.setValue(0);
				sourceoperand2.setValue(0);
				instruction.setSourceOperand1(sourceoperand1);
				instruction.setSourceOperand2(sourceoperand2);
				//System.out.println("rd="+rd);

			}
			else if (opcode>=25 && opcode<=28)
			{
				int rs1=Integer.parseInt(ansstring.substring(5,10),2);
				int rs2=Integer.parseInt(ansstring.substring(10,15),2);
				int rd=(inst<<15)>>15;
				//int rd=Integer.parseInt(ansstring.substring(15, 32),2);
				sourceoperand1.setOperandType(OperandType.Register);
				sourceoperand2.setOperandType(OperandType.Register);
				sourceoperand1.setValue(containingProcessor.getRegisterFile().getValue(rs1));
				sourceoperand2.setValue(containingProcessor.getRegisterFile().getValue(rs2));
				destinationoperand.setOperandType(OperandType.Label);
				instruction.setSourceOperand1(sourceoperand1);
				instruction.setSourceOperand2(sourceoperand2);
				rd=rd+containingProcessor.getRegisterFile().getProgramCounter()-1;
				destinationoperand.setValue(rd);
				instruction.setDestinationOperand(destinationoperand);
				//System.out.println("rd="+rd);
			}
			else
			{
				int rs1=Integer.parseInt(ansstring.substring(5,10),2);
				int rd=Integer.parseInt(ansstring.substring(10,15),2);
				//int rs2=Integer.parseInt(ansstring.substring(15,32),2);
				int rs2=(inst<<15)>>15;
				sourceoperand1.setOperandType(OperandType.Register);
				sourceoperand2.setOperandType(OperandType.Immediate);
				sourceoperand1.setValue(containingProcessor.getRegisterFile().getValue(rs1));
				sourceoperand2.setValue(rs2);
				//System.out.println("rs2"+rs2);
				destinationoperand.setOperandType(OperandType.Register);
				destinationoperand.setValue(rd);
				instruction.setSourceOperand1(sourceoperand1);
				instruction.setSourceOperand2(sourceoperand2);
				instruction.setDestinationOperand(destinationoperand);
			}
			
			OF_EX_Latch.setinstruction(instruction);
			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);
			
			
		}
	}

}
