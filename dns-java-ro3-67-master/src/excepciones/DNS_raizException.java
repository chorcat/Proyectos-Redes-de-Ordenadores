package excepciones;

import java.util.StringTokenizer;

/**
 * 
 * Excepcion que salta cuando se introduce mal el segundo parametro de entrada,
 * el cual corresponde con la IP del DNS raiz.
 * 
 * @author Borja Gonzalez Enriquez - ro71
 * @author David Quintas Vilanova - ro67
 * @see ListaErrores
 *
 */

public class DNS_raizException extends Exception {

	public DNS_raizException() {
		// TODO Auto-generated constructor stub
	}

	public DNS_raizException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public DNS_raizException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public DNS_raizException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public DNS_raizException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}
	
	public static void isCorrect(String DNS_RAIZ) throws DNS_raizException {
		if(DNS_RAIZ.isEmpty() || !isIPValid(DNS_RAIZ)) {
			System.err.println("ERROR " + ListaErrores.DNS_raizException.getidError() + ": Se ha introducido de forma"
					+ "incorrecta el segundo parametro, referente a la direccion IP del DNS raiz.");
			throw new DNS_raizException();
		}
	}
	
	private static boolean isIPValid(String ip) {
		StringTokenizer st = new StringTokenizer(ip, ".");
		
		if(st.countTokens() != 4)
			return false;
		
		while(st.hasMoreTokens()) {
			String nro = st.nextToken();
			int nroInt = 0;
			
			if(nro.length() > 3 || nro.length() < 1)
				return false;
			
			try {
				nroInt = Integer.parseInt(nro);
			} catch (NumberFormatException e) {
				return false;
			}
			
			if(nroInt < 0 || nroInt > 255) {
				return false;
			}
		}
		
		return true;
	}

}
