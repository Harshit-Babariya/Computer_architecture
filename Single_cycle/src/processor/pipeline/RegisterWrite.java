package processor.pipeline;

import generic.Instruction;
import generic.Simulator;
import processor.Processor;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	public void performRW()
	{
		if(MA_RW_Latch.isRW_enable())
		{
			//TODO
			
			// if instruction being processed is an end instruction, remember to call Simulator.setSimulationComplete(true);
			Instruction instruction=MA_RW_Latch.getinstruction();
			Instruction.OperationType operation=instruction.getOperationType();
			
			switch(operation)
			{
				case store:
				case jmp:
				case beq:
				case bgt:
				case blt:
				case bne:
						break;
				case end:
						Simulator.setSimulationComplete(true);
						break;
				default:
						containingProcessor.getRegisterFile().setValue(instruction.getDestinationOperand().getValue(), MA_RW_Latch.getresult());
						//System.out.println("\n\nin register write\n\n");
						//System.out.println("MA_RW_Latch.getresult()="+MA_RW_Latch.getresult());
						//System.out.println("instruction.getDestinationOperand().getValue()="+instruction.getDestinationOperand().getValue());
						break;
			}

			containingProcessor.getRegisterFile().setValue(31, MA_RW_Latch.getx31_value());
			MA_RW_Latch.setRW_enable(false);
			IF_EnableLatch.setIF_enable(true);
		}
	}

}
