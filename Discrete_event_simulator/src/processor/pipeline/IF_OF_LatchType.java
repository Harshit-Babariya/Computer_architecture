package processor.pipeline;
import generic.Instruction;
public class IF_OF_LatchType {
	
	boolean OF_enable;
	int instruction;
	boolean islock;
	boolean isprogramfinished=false;
	boolean isinstructionpresent=false;
	Instruction MAinstruction;
	Instruction EXinstruction;
	Instruction RWinstruction;
	

	public Instruction getRWinstruction() {
		return RWinstruction;
	}

	public void setRWinstruction(Instruction RWinstruction) {
		this.RWinstruction = RWinstruction;
	}
	public Instruction getEXinstruction() {
		return EXinstruction;
	}

	public void setEXinstruction(Instruction EXinstruction) {
		this.EXinstruction = EXinstruction;
	}
	public Instruction getMAinstruction() {
		return MAinstruction;
	}

	public void setMAinstruction(Instruction MAinstruction) {
		this.MAinstruction = MAinstruction;
	}


	public boolean getisinstructionpresent() {
		return isinstructionpresent;
	}

	public void setisinstructionpresent(boolean isinstructionpresent) {
		this.isinstructionpresent = isinstructionpresent;
	}

	public boolean getisprogramfinished() {
		return isprogramfinished;
	}

	public void setisprogramfinished(boolean isprogramfinished) {
		this.isprogramfinished = isprogramfinished;
	}
	
	public boolean islock() {
		return islock;
	}
	public void setislock(boolean islock) {
		this.islock = islock;
	}
	
	public IF_OF_LatchType()
	{
		OF_enable = false;
	}

	public boolean isOF_enable() {
		return OF_enable;
	}

	public void setOF_enable(boolean oF_enable) {
		OF_enable = oF_enable;
	}

	public int getInstruction() {
		return this.instruction;
	}

	public void setInstruction(int inst) {
		this.instruction = inst;
	}

}
