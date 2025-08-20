package processor.pipeline;

import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.Simulator;
import processor.Clock;
import processor.Processor;


public class InstructionFetch implements Element {
	
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch,OF_EX_LatchType oF_EX_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		this.OF_EX_Latch=oF_EX_Latch;
	}
	
	public void performIF()
	{
		// System.out.println("IF: "+containingProcessor.getRegisterFile().getProgramCounter());
		if(!(IF_OF_Latch.getisprogramfinished()))
		{
			if(IF_EnableLatch.isIF_busy())
			{
				return;
			}
			if(EX_IF_Latch.isEx_IF_enable())
			{
				int address=EX_IF_Latch.getbranchaddress();
				// System.out.println("hi in ex if enable");
				containingProcessor.getRegisterFile().setProgramCounter(address);
				EX_IF_Latch.setEX_IF_Latch(false);
			}
			int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
			//int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
			if(!(IF_OF_Latch.islock()))
			{
				// IF_OF_Latch.setInstruction(newInstruction);
				
				// IF_OF_Latch.setisinstructionpresent(true);
				Simulator.getEventQueue().addEvent(new MemoryReadEvent(Clock.getCurrentTime()+Configuration.L1i_latency,this,containingProcessor.getL1i(),currentPC,true,this,0));
				// System.out.println("pushing this into if: "+currentPC);
				containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);
				IF_OF_Latch.setisinstructionpresent(false);
				IF_EnableLatch.setIF_busy(true);
			

			}
			//System.out.println(newInstruction+"\n\n\n\n\n\n\n\n");
		}	
	}
	@Override
	public void handleEvent(Event event) {
		if(IF_OF_Latch.islock()||OF_EX_Latch.isOF_busyduetoEX()||OF_EX_Latch.isOF_busyduetoMA())
		{
			event.setEventTime(Clock.getCurrentTime()+1);
			Simulator.getEventQueue().addEvent(event);
		}
		else if (EX_IF_Latch.isEx_IF_enable())
		{
			// System.out.println("hi in event handler with ex if enable");
			IF_EnableLatch.setIF_busy(false);
			IF_OF_Latch.setisinstructionpresent(false);
		}
		else
		{
			//System.out.println("hi in event handler");
			MemoryResponseEvent event_mre = (MemoryResponseEvent) event;
			//System.out.println(event_mre.getValue()+"MRE EVENT VALUE");
			// Statistics.setnumber_of_instructions(Statistics.getnumber_of_instructions()+1);
			IF_OF_Latch.setInstruction(event_mre.getValue());

			// System.out.println("IF: "+IF_OF_Latch.getInstruction()+" "+event_mre.getValue());
			IF_EnableLatch.setIF_busy(false);
			IF_OF_Latch.setisinstructionpresent(true);	
		}
	}
}
