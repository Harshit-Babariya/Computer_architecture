package processor.pipeline;

import generic.Instruction;
import generic.Simulator;
import processor.Processor;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch,IF_OF_LatchType iF_OF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch=iF_OF_Latch;
	}
	
	public void performRW()
	{
		//System.out.println("RW: "+MA_RW_Latch.instruction);
		if(MA_RW_Latch.getinstruction()!=null)
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
						IF_EnableLatch.setislocked(true);
						MA_RW_Latch.setisprogramfinished(true);
						break;
				default:
						containingProcessor.getRegisterFile().setValue(instruction.getDestinationOperand().getValue(), MA_RW_Latch.getresult());
						break;
			}
			IF_OF_Latch.setRWinstruction(MA_RW_Latch.getinstruction());
			containingProcessor.getRegisterFile().setValue(31, MA_RW_Latch.getx31_value());
		}
		else
		{
			IF_OF_Latch.setRWinstruction(null);
		}
		MA_RW_Latch.setinstruction(null);
	}
}
