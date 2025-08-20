package processor.pipeline;

import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.Simulator;
import generic.Statistics;
import processor.Clock;
import processor.Processor;


public class InstructionFetch implements Element {
	
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performIF()
	{
		//System.out.println("IF: "+containingProcessor.getMainMemory().getWord(containingProcessor.getRegisterFile().getProgramCounter()));
		if(!(IF_OF_Latch.getisprogramfinished()))
		{
			if(IF_EnableLatch.isIF_busy())
			{
				return;
			}
			if(EX_IF_Latch.isEx_IF_enable())
			{
				int address=EX_IF_Latch.getbranchaddress();
				//System.out.println(address);
				containingProcessor.getRegisterFile().setProgramCounter(address);
				EX_IF_Latch.setEX_IF_Latch(false);
			}
			int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
			//int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
			if(!(IF_OF_Latch.islock()))
			{
				// IF_OF_Latch.setInstruction(newInstruction);
				containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);
				// IF_OF_Latch.setisinstructionpresent(true);
				Simulator.getEventQueue().addEvent(new MemoryReadEvent(Clock.getCurrentTime()+Configuration.mainMemoryLatency,this,containingProcessor.getMainMemory(),currentPC));

				IF_OF_Latch.setisinstructionpresent(false);
				IF_EnableLatch.setIF_busy(true);
			

			}
			//System.out.println(newInstruction+"\n\n\n\n\n\n\n\n");
		}	
	}
	@Override
	public void handleEvent(Event event) {
		if(IF_OF_Latch.islock())
		{
			event.setEventTime(Clock.getCurrentTime()+1);
			Simulator.getEventQueue().addEvent(event);
		}
		else if (EX_IF_Latch.isEx_IF_enable())
		{
			//System.out.println("hi in event handler");
			IF_EnableLatch.setIF_busy(false);
			IF_OF_Latch.setisinstructionpresent(false);
		}
		else
		{
			//System.out.println("hi in event handler");
			MemoryResponseEvent event_mre = (MemoryResponseEvent) event;
			//System.out.println(event_mre.getValue()+"MRE EVENT VALUE");
			Statistics.setnumber_of_instructions(Statistics.getnumber_of_instructions()+1);
			IF_OF_Latch.setInstruction(event_mre.getValue());
			IF_EnableLatch.setIF_busy(false);
			IF_OF_Latch.setisinstructionpresent(true);	
		}
	}
}
