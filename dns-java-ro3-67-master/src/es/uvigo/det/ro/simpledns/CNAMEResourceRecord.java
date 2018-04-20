package es.uvigo.det.ro.simpledns;

import static es.uvigo.det.ro.simpledns.RRType.CNAME;

public class CNAMEResourceRecord extends ResourceRecord {

	private final DomainName cname;

	public CNAMEResourceRecord(DomainName domain, int ttl, DomainName cname) {
		super(domain, CNAME, ttl, cname.toByteArray());
		this.cname = cname;
	}

	protected CNAMEResourceRecord(ResourceRecord decoded, final byte[] message) {
		super(decoded);
		cname = new DomainName(getRRData(), message);
		
	}

	public final DomainName getCname() {
		return cname;
	}
	
	
	
	
}