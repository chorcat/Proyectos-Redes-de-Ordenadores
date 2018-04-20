package excepciones;

/**
 * 
 * Excepcion que salta cuando se introduce de forma incorrecta el primer
 * parametro de entrada, el cual corresponde con el del protocolo de transporte.
 * Parametros validos:
 * TCP => -t
 * UDP => -u
 * Aviso: No se admite el omitir dicho parametro.
 * 
 * @author Borja Gonzalez Enriquez - ro71
 * @author David Quintas Vilanova - ro67
 * @see ListaErrores
 *
 */

public class TransportProtocolException extends Exception {

	public TransportProtocolException() {
		// TODO Auto-generated constructor stub
	}

	public TransportProtocolException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public TransportProtocolException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public TransportProtocolException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public TransportProtocolException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}
	
	public static void isCorrect(String transport_protocol) throws TransportProtocolException {
		if(!(transport_protocol.equals("UDP") || transport_protocol.equals("TCP"))) {
			System.err.println("ERROR " + ListaErrores.TransportProtocolException.getidError() + ": Se ha introducido de forma"
					+ "incorrecta el primer parametro, referente al protocolo de transporte. Introduzca -u/-t");
			throw new TransportProtocolException();
		}
	}

}
