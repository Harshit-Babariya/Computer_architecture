package processor.pipeline;

public class EX_IF_LatchType {

	boolean  ex_if_enable;
	int branchaddress;
	public EX_IF_LatchType()
	{
		ex_if_enable=false;
	}
	public void setEX_IF_Latch(boolean value)
	{
		ex_if_enable=value;
	}
	public boolean isEx_IF_enable()
	{
		return ex_if_enable;
	}
	public int getbranchaddress()
	{
		return branchaddress;
	}
	public void setbranchaddress(int address)
	{
		branchaddress=address;
	}
}
