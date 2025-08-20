package processor.pipeline;

public class IF_EnableLatchType {
	
	boolean islocked;
	boolean isIF_busy;

	public boolean isIF_busy() {
		return isIF_busy;
	}

	public void setIF_busy(boolean isIF_busy) {
		this.isIF_busy = isIF_busy;
	}

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
