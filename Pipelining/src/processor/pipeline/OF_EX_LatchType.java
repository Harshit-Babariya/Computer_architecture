package processor.pipeline;

import generic.Instruction;

public class OF_EX_LatchType {
	
	boolean EX_enable;
	Instruction instruction;
	int branch_hazard_counter=0;
	boolean isprogramfinished=false;

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
