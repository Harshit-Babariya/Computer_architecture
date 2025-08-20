package processor.pipeline;
import generic.Instruction;
import generic.Instruction.OperationType;
import processor.Processor;
public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	IF_OF_LatchType IF_OF_Latch;
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch,IF_OF_LatchType iF_OF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_OF_Latch=iF_OF_Latch;
	}
	public void performMA()
	{
		//TODO
		if(!(MA_RW_Latch.getisprogramfinished()))
		{
			//System.out.println("MA:"+EX_MA_Latch.instruction);
			if(EX_MA_Latch.getinstruction()==null)
			{
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
					result=containingProcessor.getMainMemory().getWord(result);
				}
				else if (operation==OperationType.store)
				{
					ans=instruction.getSourceOperand1().getValue();
					containingProcessor.getMainMemory().setWord(result, ans);
					
				}
				else if(operation==OperationType.end)
				{
					EX_MA_Latch.setisprogramfinished(true);
				}
				MA_RW_Latch.setx31_value(EX_MA_Latch.getx31_value());
				MA_RW_Latch.setinstruction(instruction);
				MA_RW_Latch.setresult(result);
				IF_OF_Latch.setMAinstruction(instruction);		
			}
		}
	}
}
