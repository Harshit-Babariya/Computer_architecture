package processor.pipeline;

import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Statistics;
import processor.Processor;
public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	IF_OF_LatchType IF_OF_Latch;
	int counter=0;
	boolean branch_taken=false;
	int branchaddress=0;
	
	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch,IF_OF_LatchType iF_OF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		this.IF_OF_Latch = iF_OF_Latch;
	}
	
	public void performEX()
	{
		//TODO
		if(!(EX_MA_Latch.getisprogramfinished()))
		{
			if(counter!=0)
			{
				counter=0;
				EX_MA_Latch.setinstruction(null);
				EX_IF_Latch.setEX_IF_Latch(branch_taken);
				EX_IF_Latch.setbranchaddress(branchaddress);
				IF_OF_Latch.setEXinstruction(null);
				Statistics.setnumber_of_times_instructions_on_wrong_path(Statistics.getnumber_of_times_instructions_on_wrong_path()+2);
				return;
			}
			if(OF_EX_Latch.getinstruction()==null)
			{
				EX_MA_Latch.setinstruction(null);
				IF_OF_Latch.setEXinstruction(null);

			}
			else if(OF_EX_Latch.getinstruction()!=null)
			{
				Instruction instruction=OF_EX_Latch.getinstruction();
				OperationType operation=instruction.getOperationType();
				int flag=0;
				int result=0;
				int op1=instruction.getSourceOperand1().getValue();
				int op2=instruction.getSourceOperand2().getValue();
				int rd=instruction.getDestinationOperand().getValue();
				int x31_value=containingProcessor.getRegisterFile().getValue(31);
				long outofbound;
				switch(operation)
				{
					case add:
					case addi:
							outofbound=(long)op1+(long)op2;
							if(outofbound>Integer.MAX_VALUE||outofbound<Integer.MIN_VALUE)
							{
								long extrabits=outofbound>>>32;
								result=(int)((outofbound<<32)>>>32);
								x31_value=(int)extrabits;
							}
							else
							{
								result=op1+op2;
							}
							break;
					case sub:
					case subi:
							outofbound=(long)op1-(long)op2;
							if(outofbound>Integer.MAX_VALUE||outofbound<Integer.MIN_VALUE)
							{
								long extrabits=outofbound>>>32;
								result=(int)((outofbound<<32)>>>32);
								x31_value=(int)extrabits;
							}
							else
							{
								result=op1-op2;
							}
							break;
					case mul:
					case muli:
							outofbound=(long)op1*(long)op2;
							if(outofbound>Integer.MAX_VALUE||outofbound<Integer.MIN_VALUE)
							{
								long extrabits=outofbound>>>32;
								result=(int)((outofbound<<32)>>>32);
								x31_value=(int)extrabits;
							}
							else
							{
								result=op1*op2;
							}
							break;
					case div:
					case divi:
							result=op1/op2;
							x31_value=op1%op2;
							break;
					case and:
					case andi:
							result=op1&op2;
							break;
					case or:
					case ori:		
							result=op1|op2;
							break;
					case xor:
					case xori:
							result=op1^op2;
							break;
					case slt:
					case slti:
							if(op1<op2)
							{
								result=1;
							}
							else
							{
								result=0;
							}
							break;
					case sll:
					case slli:
							result=op1<<op2;
							x31_value=op1>>>32-op2;
							break;
					case srl:
					case srli:
							result=op1>>>op2;
							x31_value=(op1<<32-op2)>>>32-op2;
							break;
					case sra:
					case srai:
							result=op1>>op2;
							x31_value=(op1<<32-op2)>>>32-op2;
							break;
					case load:
							result=op1+op2;
							break;
					case store:
							result=containingProcessor.getRegisterFile().getValue(rd)+op2;
							break;
					case jmp:
							result=rd;
							flag=1;
							break;
					case beq:
							if(op1==op2)
							{
								result=rd;
								flag=1;
							}
							else
							{
								result=-1;
							}
							break;
					case bne:
							if(op1!=op2)
							{
								result=rd;
								flag=1;
							}
							else
							{
								result=-1;
							}
							break;
					case blt:
							if(op1<op2)
							{
								result=rd;
								flag=1;
							}
							else
							{
								result=-1;
							}
							break;
					case bgt:
							if(op1>op2)
							{
								result=rd;
								flag=1;
							}
							else
							{
								result=-1;
							}
							break;
					case end:
							OF_EX_Latch.setisprogramfinished(true);
							break;
					default:
							break;	
				}
				if(flag==1)
				{
					branchaddress=result;
					branch_taken=true;
					counter=1;
				}
				EX_MA_Latch.setx31_value(x31_value);				
				EX_MA_Latch.setinstruction(instruction);
				EX_MA_Latch.setresult(result);
				IF_OF_Latch.setEXinstruction(instruction);
			}
		}	
	}
}