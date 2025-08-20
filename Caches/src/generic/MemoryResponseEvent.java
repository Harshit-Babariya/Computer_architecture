package generic;

public class MemoryResponseEvent extends Event {

	int value;
	boolean wasRead;
	Element previousElement;
	int address;
	int datatobewritten;
	public MemoryResponseEvent(long eventTime, Element requestingElement, Element processingElement, int value,boolean wasRead,Element previousElement,int address,int datatobewritten) {
		super(eventTime, EventType.MemoryResponse, requestingElement, processingElement);
		this.value = value;
		this.wasRead=wasRead;
		this.previousElement=previousElement;
		this.address=address;
		this.datatobewritten=datatobewritten;
	}
	public int getdatatobewritten() {
		return datatobewritten;
	}
	public int getaddress() {
		return address;
	}
	public Element getPreviousElement() {
		return previousElement;
	}
	public boolean wasRead() {
		return wasRead;
	}


	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
