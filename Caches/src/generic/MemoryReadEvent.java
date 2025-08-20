package generic;

public class MemoryReadEvent extends Event {

	int addressToReadFrom;
	boolean wasRead;
	Element previousElement;
	int datatobewritten;
	public MemoryReadEvent(long eventTime, Element requestingElement, Element processingElement, int address,boolean wasRead,Element previousElement,int datatobewritten) {
		super(eventTime, EventType.MemoryRead, requestingElement, processingElement);
		this.addressToReadFrom = address;
		this.wasRead=wasRead;
		this.previousElement=previousElement;
		this.datatobewritten=datatobewritten;
	}
	public int getdatatobewritten() {
		return datatobewritten;
	}
	public Element getPreviousElement() {
		return previousElement;
	}
	public boolean wasRead() {
		return wasRead;
	}

	public int getAddressToReadFrom() {
		return addressToReadFrom;
	}

	public void setAddressToReadFrom(int addressToReadFrom) {
		this.addressToReadFrom = addressToReadFrom;
	}
}
