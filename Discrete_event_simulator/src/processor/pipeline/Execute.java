package processor.pipeline;

import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.ExecutionCompleteEvent;
import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Simulator;
import generic.Statistics;
import processor.Clock;
import processor.Processor;
public class Execute implements Element{
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	IF_OF_LatchType IF_OF_Latch;
	int counter=0;
	boolean branch_taken=false;
	int branchaddress=0;
	int result_value= 0;
	int x_31_value=0;
	Instruction insturction_ex;
	boolean ex_pass=true;
	
	
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
		//System.out.println("EX:"+OF_EX_Latch.instruction);
		if(!(EX_MA_Latch.getisprogramfinished()))
		{
			if(EX_MA_Latch.is_ma_busy)
			{
				OF_EX_Latch.setexbusy(true);
				return;
			}
			if(OF_EX_Latch.isexbusy())
			{
				//System.out.println("in ex in exbusy");
				EX_MA_Latch.setinstruction(null);
				return;
			}
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
			if(OF_EX_Latch.getinstruction()==null && ex_pass)
			{
				//System.out.println("in ex in performing ex with expass");
				EX_MA_Latch.setinstruction(null);
				IF_OF_Latch.setEXinstruction(null);

			}
			else if(OF_EX_Latch.getinstruction()==null && !ex_pass)
			{
				//System.out.println("in ex in performing ex with !expass");
				ex_pass=true;
				IF_OF_Latch.setEXinstruction(null);
			}
			else if(OF_EX_Latch.getinstruction()!=null)
			{
				//System.out.println("in ex in performing ex with instruction");
				Instruction instruction=OF_EX_Latch.getinstruction();
				OperationType operation=instruction.getOperationType();
				int flag=0;
				int result=0;
				int op1=instruction.getSourceOperand1().getValue();
				int op2=instruction.getSourceOperand2().getValue();
				int rd=instruction.getDestinationOperand().getValue();
				int x31_value=containingProcessor.getRegisterFile().getValue(31);
				//long outofbound;
				switch(operation)
				{
					case add:
					case addi:
							Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
							OF_EX_Latch.setexbusy(true);
							EX_MA_Latch.setinstruction(null);
							OF_EX_Latch.setinstruction(null);
							result=op1+op2;
							break;
					case sub:
					case subi:
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
						OF_EX_Latch.setexbusy(true);
						EX_MA_Latch.setinstruction(null);
						OF_EX_Latch.setinstruction(null);
								result=op1-op2;
							break;
					case mul:
					case muli:
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.multiplier_latency, this, this));
						OF_EX_Latch.setexbusy(true);
						EX_MA_Latch.setinstruction(null);
						OF_EX_Latch.setinstruction(null);
								result=op1*op2;
							break;
					case div:
					case divi:
						//System.out.println("in div"+Configuration.divider_latency);
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.divider_latency, this, this));
						OF_EX_Latch.setexbusy(true);
						EX_MA_Latch.setinstruction(null);
						OF_EX_Latch.setinstruction(null);
							result=op1/op2;
							x31_value=op1%op2;
							break;
					case and:
					case andi:
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
						OF_EX_Latch.setexbusy(true);
						EX_MA_Latch.setinstruction(null);
						OF_EX_Latch.setinstruction(null);
							result=op1&op2;
							break;
					case or:
					case ori:
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
						OF_EX_Latch.setexbusy(true);
						EX_MA_Latch.setinstruction(null);
						OF_EX_Latch.setinstruction(null);		
							result=op1|op2;
							break;
					case xor:
					case xori:
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
						OF_EX_Latch.setexbusy(true);
						EX_MA_Latch.setinstruction(null);
						OF_EX_Latch.setinstruction(null);
							result=op1^op2;
							break;
					case slt:
					case slti:
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
						OF_EX_Latch.setexbusy(true);
						EX_MA_Latch.setinstruction(null);
						OF_EX_Latch.setinstruction(null);
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
							Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
							OF_EX_Latch.setexbusy(true);
							EX_MA_Latch.setinstruction(null);
							OF_EX_Latch.setinstruction(null);
							result=op1<<op2;
							x31_value=op1>>>32-op2;
							break;
					case srl:
					case srli:
							Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
							OF_EX_Latch.setexbusy(true);
							EX_MA_Latch.setinstruction(null);
							OF_EX_Latch.setinstruction(null);
							result=op1>>>op2;
							x31_value=(op1<<32-op2)>>>32-op2;
							break;
					case sra:
					case srai:
							Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
							OF_EX_Latch.setexbusy(true);
							EX_MA_Latch.setinstruction(null);
							OF_EX_Latch.setinstruction(null);
							result=op1>>op2;
							x31_value=(op1<<32-op2)>>>32-op2;
							break;
					case load:
							Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
							OF_EX_Latch.setexbusy(true);
							EX_MA_Latch.setinstruction(null);
							OF_EX_Latch.setinstruction(null);
							result=op1+op2;
							break;
					case store:
							Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
							OF_EX_Latch.setexbusy(true);
							EX_MA_Latch.setinstruction(null);
							OF_EX_Latch.setinstruction(null);
							result=containingProcessor.getRegisterFile().getValue(rd)+op2;
							break;
					case jmp:
							Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
							OF_EX_Latch.setexbusy(true);
							EX_MA_Latch.setinstruction(null);
							OF_EX_Latch.setinstruction(null);
							result=rd;
							flag=1;
							break;
					case beq:
							Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
							OF_EX_Latch.setexbusy(true);
							EX_MA_Latch.setinstruction(null);
							OF_EX_Latch.setinstruction(null);
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
							Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
							OF_EX_Latch.setexbusy(true);
							EX_MA_Latch.setinstruction(null);
							OF_EX_Latch.setinstruction(null);
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
							Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
							OF_EX_Latch.setexbusy(true);
							EX_MA_Latch.setinstruction(null);
							OF_EX_Latch.setinstruction(null);
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
							Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
							OF_EX_Latch.setexbusy(true);
							EX_MA_Latch.setinstruction(null);
							OF_EX_Latch.setinstruction(null);
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
							EX_MA_Latch.setinstruction(instruction);
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
				x_31_value=x31_value;
				result_value=result;

				insturction_ex=instruction;	
				//System.out.println("setting insturction in ex to "+ insturction_ex);
				//EX_MA_Latch.setx31_value(x31_value);				
				//EX_MA_Latch.setinstruction(instruction);
				//EX_MA_Latch.setresult(result);
				IF_OF_Latch.setEXinstruction(instruction);
			}
		}	
	}
	@Override
	public void handleEvent(Event event) 
	{
		if(EX_MA_Latch.is_ma_busy())
		{
			//System.out.println("hi in ex event handler busy");
			event.setEventTime(Clock.getCurrentTime()+1);
			Simulator.getEventQueue().addEvent(event);
		}
		else
		{
			
			//Instruction instruction=OF_EX_Latch.getinstruction();	
			//System.out.println("hi in exma not busy"+ insturction_ex);		
			EX_MA_Latch.setinstruction(insturction_ex);
			EX_MA_Latch.setresult(result_value);
			EX_MA_Latch.setx31_value(x_31_value);
			//EX_MA_Latch.setMA_enable(true);
			OF_EX_Latch.setexbusy(false);
			IF_OF_Latch.setislock(false);
			//System.out.println(EX_MA_Latch.getinstruction());
			ex_pass=false;
		}
	}
}