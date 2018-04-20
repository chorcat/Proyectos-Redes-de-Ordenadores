package es.uvigo.det.ro.simpledns;

public class SOAResourceRecord extends ResourceRecord {
	
	private int pos;

	private final DomainName mname;
	private final DomainName rname;
	private long serial;
	private long refresh;
	private long retry;
	private long expire;
	private long minimum;

	public SOAResourceRecord(ResourceRecord decoded, final byte[] message) {
		super(decoded);
		
		pos = 0;

		mname = new DomainName(getRRData(), message);
		
		pos += mname.getEncodedLength();
		
		byte[] rrdata2 = new byte[getRDLength() - pos];
		System.arraycopy(getRRData(), pos, rrdata2, 0, getRDLength() - pos);
		
		rname = new DomainName(rrdata2, message);
		
		pos += rname.getEncodedLength();

		serial = readU32(getRRData());
		refresh = readU32(getRRData());
		retry = readU32(getRRData());
		expire = readU32(getRRData());
		minimum = readU32(getRRData());
	}
	
	private long readU32(byte[] array) {
		int b1 = array[pos++] & 0xFF;
		int b2 = array[pos++] & 0xFF;
		int b3 = array[pos++] & 0xFF;
		int b4 = array[pos++] & 0xFF;
		return (((long)b1 << 24) + (b2 << 16) + (b3 << 8) + b4);
	}
	
	public DomainName getMname() {
		return mname;
	}

	public DomainName getRname() {
		return rname;
	}

	public long getSerial() {
		return serial;
	}

	public void setSerial(long serial) {
		this.serial = serial;
	}

	public long getRefresh() {
		return refresh;
	}

	public void setRefresh(long refresh) {
		this.refresh = refresh;
	}

	public long getRetry() {
		return retry;
	}

	public void setRetry(long retry) {
		this.retry = retry;
	}

	public long getExpire() {
		return expire;
	}

	public void setExpire(long expire) {
		this.expire = expire;
	}

	public long getMinimum() {
		return minimum;
	}

	public void setMinimum(long minimum) {
		this.minimum = minimum;
	}
	
	public String toString() {
		return mname + " " + rname + " " + serial + " " + refresh + " " + retry + " " + expire + " " + minimum;
	}
}
