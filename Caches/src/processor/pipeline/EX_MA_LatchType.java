package processor.pipeline;

import generic.Instruction;

public class EX_MA_LatchType {
	
	boolean MA_enable;
	int result;
	Instruction instruction;
	int x31_value;
	boolean is_ma_busy=false;

	public boolean is_ma_busy() {
		return is_ma_busy;
	}

	public void set_ma_busy(boolean is_ma_busy) {
		this.is_ma_busy = is_ma_busy;
	}
	
	boolean isprogramfinished=false;

	public boolean getisprogramfinished() {
		return isprogramfinished;
	}

	public void setisprogramfinished(boolean isprogramfinished) {
		this.isprogramfinished = isprogramfinished;
	}
	
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
