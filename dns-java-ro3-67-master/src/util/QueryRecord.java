package util;

import java.net.InetAddress;

import es.uvigo.det.ro.simpledns.DomainName;
import es.uvigo.det.ro.simpledns.RRType;

/**
 * Clase que almacena los par�metros de las Consultas al DNS ra�z y que son
 * necesarias para luego imprimirlas.
 * 
 * @author Borja Gonzalez Enriquez - ro71
 * @author David Quintas Vilanova - ro67
 * @see AnswerRecord
 *
 */

public class QueryRecord {
	private String protocol;
	private InetAddress servidor;
	private RRType type;
	private DomainName nombre;
	private boolean isCache;

	public QueryRecord(RRType type, DomainName nombre) {
		this.type = type;
		this.nombre = nombre;
	}

	public QueryRecord(String protocol, InetAddress servidor, RRType type, DomainName nombre) {
		this.protocol = protocol;
		this.servidor = servidor;
		this.type = type;
		this.nombre = nombre;
	}

	public RRType getType() {
		return type;
	}

	public DomainName getNombre() {
		return nombre;
	}

	public String toString() {
		if (isCache()) {
			return "Q: cache " + servidor.getHostAddress() + " " + type + " " + nombre;
		} else {
			return "Q: " + protocol + " " + servidor.getHostAddress() + " " + type + " " + nombre;
		}
	}

	public boolean isCache() {
		return isCache;
	}

	public void setCache(boolean isCache) {
		this.isCache = isCache;
	}
}
