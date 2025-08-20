package processor.pipeline;

import generic.Instruction;

public class EX_MA_LatchType {
	
	boolean MA_enable;
	int result;
	Instruction instruction;
	int x31_value;
	
	public EX_MA_LatchType()
	{
		MA_enable = false;
	}

	public boolean isMA_enable() {
		return MA_enable;
	}

	public void setMA_enable(boolean mA_enable) {
		MA_enable = mA_enable;
	}

	public void setresult(int result)
	{
		this.result=result;
	}

	public int getresult()
	{
		return result;
	}
	

	public Instruction getinstruction()
	{
		return instruction;
	}
	public void setinstruction(Instruction inst)
	{
		instruction=inst;
	}
	public void setx31_value(int value)
	{
		this.x31_value = value;
	}
	public int getx31_value()
	{
		return x31_value;
	}
}
