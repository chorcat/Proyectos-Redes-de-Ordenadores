package excepciones;

/**
 * 
 * Excepcion que salta cuando se introducen mal el numero de
 * parametros de entrada.
 * 
 * @author Borja Gonzalez Enriquez - ro71
 * @author David Quintas Vilanova - ro67
 * @see ListaErrores
 *
 */

public class ArgsLengthException extends Exception {

	public ArgsLengthException() {
		// TODO Auto-generated constructor stub
	}

	public ArgsLengthException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ArgsLengthException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public ArgsLengthException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ArgsLengthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}
	
	public static void isCorrect(String[] args) throws ArgsLengthException {
		if(args.length != 2) {
			System.err.println("ERROR " + ListaErrores.ArgsLengthException.getidError() + ": Se han introducido mal el numero"
					+ " de parametros de entrada. La forma correcta de ejecucion seria (2 parametros): dnsclient -t/-u ip_dns_raiz");
			throw new ArgsLengthException();
		}
	}

}
