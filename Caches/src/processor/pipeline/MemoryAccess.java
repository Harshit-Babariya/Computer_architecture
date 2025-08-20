package processor.pipeline;
import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.Event.EventType;
import generic.Instruction;
import generic.Instruction.OperationType;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.MemoryWriteEvent;
import generic.Simulator;
import processor.Clock;
import processor.Processor;

public class MemoryAccess implements Element{
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	//boolean ma_pass=true;
	Instruction instruction_ma;
	int x_31_value;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch,IF_OF_LatchType iF_OF_Latch,OF_EX_LatchType oF_EX_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_OF_Latch=iF_OF_Latch;
		this.OF_EX_Latch=oF_EX_Latch;
	}
	public void performMA()
	{
		//TODO
		// System.out.println("MA: "+EX_MA_Latch.instruction);
		if(!(MA_RW_Latch.getisprogramfinished()))
		{
			if(EX_MA_Latch.is_ma_busy)
			{
				MA_RW_Latch.setinstruction(null);
				return;
			}
			//System.out.println("MA:"+EX_MA_Latch.instruction);
			if(EX_MA_Latch.getinstruction()==null)
			{
				//System.out.println("in ex in performing ex with expass");
				MA_RW_Latch.setinstruction(null);
				IF_OF_Latch.setMAinstruction(null);

			}
			else if(EX_MA_Latch.getinstruction()!=null)
			{
				Instruction instruction=EX_MA_Latch.getinstruction();
				int result=EX_MA_Latch.getresult();
				int ans;
				OperationType operation=instruction.getOperationType();

				if(operation==OperationType.load)
				{
					// result=containingProcessor.getMainMemory().getWord(result);
					Simulator.getEventQueue().addEvent(new MemoryReadEvent(Clock.getCurrentTime()+Configuration.L1d_latency,this,containingProcessor.getL1d(),result,true,this,0));
					// System.out.println(EX_MA_Latch.getinstruction()+"============================================================================================");

					MA_RW_Latch.setinstruction(null);
					EX_MA_Latch.setinstruction(null);
					EX_MA_Latch.set_ma_busy(true);
					OF_EX_Latch.setEX_busyduetoMA(true);
					OF_EX_Latch.setOF_busyduetoMA(true);
				}
				else if (operation==OperationType.store)
				{
					ans=instruction.getSourceOperand1().getValue();
					// System.out.println("MA STORE IS GOING IN AT TIME: "+Clock.getCurrentTime());
					// containingProcessor.getMainMemory().setWord(result, ans);
					Simulator.getEventQueue().addEvent(new MemoryWriteEvent(Clock.getCurrentTime()+Configuration.L1d_latency,this,containingProcessor.getL1d(),result,ans));
					MA_RW_Latch.setinstruction(null);
					EX_MA_Latch.setinstruction(null);
					EX_MA_Latch.set_ma_busy(true);
					OF_EX_Latch.setEX_busyduetoMA(true);
					OF_EX_Latch.setOF_busyduetoMA(true);
				}
				else 
				{
					if(operation==OperationType.end)
					{
						EX_MA_Latch.setisprogramfinished(true);
					}
					MA_RW_Latch.setx31_value(EX_MA_Latch.getx31_value());
					MA_RW_Latch.setinstruction(instruction);
					MA_RW_Latch.setresult(result);
					IF_OF_Latch.setMAinstruction(instruction);
				}
				instruction_ma=instruction;
				x_31_value=EX_MA_Latch.getx31_value();

				
						
			}
		}
	}
	@Override
	public void handleEvent(Event event) {
		if(event.getEventType()==EventType.MemoryResponse)
		{
			MemoryResponseEvent event_mre =(MemoryResponseEvent) event;
			int load_result=event_mre.getValue();
			// Instruction inst=EX_MA_Latch.getinstruction();
			// int x_31_value=MA_RW_Latch.getx31_value();
			MA_RW_Latch.setx31_value(x_31_value);
			MA_RW_Latch.setresult(load_result);
			MA_RW_Latch.setinstruction(instruction_ma);
			
			//MA_RW_Latch.setRW_enable(true);

		}
		//MA_RW_Latch.set_ma_busy(false);
		EX_MA_Latch.set_ma_busy(false);
		OF_EX_Latch.setEX_busyduetoMA(false);
		
	}
}
