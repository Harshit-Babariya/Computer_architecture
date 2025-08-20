package processor.pipeline;

import generic.Instruction;

public class OF_EX_LatchType {
	
	boolean EX_enable;
	Instruction instruction;
	int branch_hazard_counter=0;
	boolean isprogramfinished=false;
	boolean isexbusy=false;
	boolean EX_busyduetoMA=false;
	boolean OF_busyduetoMA=false;
	boolean OF_busyduetoEX=false;

	public boolean isEX_busyduetoMA() {
		return EX_busyduetoMA;
	}

	public void setEX_busyduetoMA(boolean eX_busyduetoMA) {
		EX_busyduetoMA = eX_busyduetoMA;
	}

	public boolean isOF_busyduetoMA() {
		return OF_busyduetoMA;
	}

	public void setOF_busyduetoMA(boolean oF_busyduetoMA) {
		OF_busyduetoMA = oF_busyduetoMA;
	}

	public boolean isOF_busyduetoEX() {
		return OF_busyduetoEX;
	}	

	public void setOF_busyduetoEX(boolean oF_busyduetoEX) {
		OF_busyduetoEX = oF_busyduetoEX;
	}
	
	public boolean isexbusy() {
		return isexbusy;
	}

	public void setexbusy(boolean isexbusy) {
		this.isexbusy = isexbusy;
	}

	public boolean getisprogramfinished() {
		return isprogramfinished;
	}

	public void setisprogramfinished(boolean isprogramfinished) {
		this.isprogramfinished = isprogramfinished;
	}
	public int getbranch_hazard_counter() {
		return this.branch_hazard_counter;
	}

	public void setbranch_hazard_counter(int branch_hazard_counter) {
		this.branch_hazard_counter = branch_hazard_counter;
	}

	public Instruction getinstruction()
	{
		return this.instruction;
	}
	public void setinstruction(Instruction inst)
	{
		this.instruction=inst;
	}
	public OF_EX_LatchType()
	{
		EX_enable = false;
	}

	public boolean isEX_enable() {
		return EX_enable;
	}

	public void setEX_enable(boolean eX_enable) {
		EX_enable = eX_enable;
	}
	

}
