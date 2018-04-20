package excepciones;

/**
 * 
 * @author Borja Gonzalez Enriquez - ro71
 * @author David Quintas Vilanova - ro67
 * @see ArgsLengthException
 * 
 */

public enum ListaErrores {
	ArgsLengthException(1),
	TransportProtocolException(2),
	DNS_raizException(3);
	
	private final int idError;
	
	ListaErrores(int idError) {
		this.idError = idError;
	}
	
	public int getidError() {
		return idError;
	}
}
