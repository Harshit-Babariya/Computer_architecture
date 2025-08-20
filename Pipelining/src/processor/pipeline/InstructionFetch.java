package processor.pipeline;

import processor.Processor;


public class InstructionFetch {
	
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
		if(!(IF_OF_Latch.getisprogramfinished()))
		{
			if(EX_IF_Latch.isEx_IF_enable())
			{
				int address=EX_IF_Latch.getbranchaddress();
				//System.out.println(address);
				containingProcessor.getRegisterFile().setProgramCounter(address);
				EX_IF_Latch.setEX_IF_Latch(false);
			}
			int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
			int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
			if(!(IF_OF_Latch.islock()))
			{
				IF_OF_Latch.setInstruction(newInstruction);
				containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);
				IF_OF_Latch.setisinstructionpresent(true);
			}
			//System.out.println(newInstruction+"\n\n\n\n\n\n\n\n");
		}	
	}
}
