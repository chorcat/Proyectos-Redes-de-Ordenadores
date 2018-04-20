package es.uvigo.det.ro.simpledns;

public class MXResourceRecord extends ResourceRecord {
	private int preference;
	private DomainName mx;
	private int pos;

	protected MXResourceRecord(ResourceRecord decoded, final byte[] message) {
		super(decoded);
		mx = new DomainName(getRRData(), message);
		pos = 0;
		preference = readU16(getRRData());
		byte[] rrdata2 = new byte[getRDLength() - pos];
		System.arraycopy(getRRData(), pos, rrdata2, 0, getRDLength() - pos);
		mx = new DomainName(rrdata2, message);
	}

	private int readU16(byte[] array) {
		int b1 = array[pos++] & 0xFF;
		int b2 = array[pos++] & 0xFF;
		return ((b1 << 8) + b2);
	}
	
	public String toString(){
		return preference +" "+ mx;
	}

}
