package processor.pipeline;

public class IF_EnableLatchType {
	
	boolean islocked;
	
	public IF_EnableLatchType()
	{
		islocked = false;
	}

	public boolean islocked() {
		return islocked;
	}

	public void setislocked(boolean islock) {
		islocked = islock;
	}

}
