package es.uvigo.det.ro.simpledns;

public class TXTResourceRecord extends ResourceRecord{
	
	private final String txt_data;

	public TXTResourceRecord(ResourceRecord decoded, final byte[] message) {
		super(decoded);
		
		txt_data = new String(getRRData());
	}
	
	public String getTXT_data() {
		return this.txt_data;
	}

}
