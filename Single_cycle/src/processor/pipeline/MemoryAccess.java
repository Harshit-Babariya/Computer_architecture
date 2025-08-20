package processor.pipeline;

import generic.Instruction;
import generic.Instruction.OperationType;
import processor.Processor;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performMA()
	{
		//TODO
		if(EX_MA_Latch.isMA_enable())
		{
			Instruction instruction=EX_MA_Latch.getinstruction();
			int result=EX_MA_Latch.getresult();
			int ans;
			OperationType operation=instruction.getOperationType();
			if(operation==OperationType.load)
			{
				result=containingProcessor.getMainMemory().getWord(result);
			}
			else if (operation==OperationType.store)
			{
				ans=instruction.getSourceOperand1().getValue();
				//System.out.println("ans="+ans);
				//System.out.println("result="+result);
				containingProcessor.getMainMemory().setWord(result, ans);
				
			}
			MA_RW_Latch.setx31_value(EX_MA_Latch.getx31_value());
			MA_RW_Latch.setinstruction(instruction);
			MA_RW_Latch.setresult(result);
			EX_MA_Latch.setMA_enable(false);
			MA_RW_Latch.setRW_enable(true);
			
		}
	}

}
