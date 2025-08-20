package processor.pipeline;

import generic.Instruction;

public class MA_RW_LatchType {
	
	boolean RW_enable;
	int result;
	Instruction instruction;
	int x31_value;
	boolean isprogramfinished=false;

	public boolean getisprogramfinished() {
		return isprogramfinished;
	}

	public void setisprogramfinished(boolean isprogramfinished) {
		this.isprogramfinished = isprogramfinished;
	}
	public MA_RW_LatchType()
	{
		RW_enable = false;
	}

	public boolean isRW_enable() {
		return RW_enable;
	}

	public void setRW_enable(boolean rW_enable) {
		RW_enable = rW_enable;
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
		this.x31_value=value;
	}
	public int getx31_value()
	{
		return x31_value;
	}

}
