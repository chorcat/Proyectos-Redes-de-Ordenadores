package transporte;

import java.net.InetAddress;

import es.uvigo.det.ro.simpledns.Message;
import es.uvigo.det.ro.simpledns.RRClass;
import es.uvigo.det.ro.simpledns.RRType;
import transporte.TCP_Socket;
import transporte.UDP_Socket;

/**
 * El lado del cliente de DNS se denomina "DNS Resolver". El Resolver es el
 * responsable de iniciar y secuenciar las consultas que finalmente a una
 * resolucion completa (traduccion) del recurso buscado, por ejemplo, la
 * traduccion de un nombre de dominio a una direccion IP.
 * 
 * @author Borja Gonzalez Enriquez - ro71
 * @author David Quintas Vilanova - ro67
 * @see Message
 * @see UDP_Socket
 * @see TCP_Socket
 *
 */

public class Resolver {
	private static final int DNS_PORT = 53;
	private InetAddress DNS_RAIZ;
	private boolean useUdp = true;

	/**
	 * Constructor que recibe el InetAdress del Servidor DNS Raiz que fue
	 * introducido por parometros en dnsclient.
	 * 
	 * @param DNS_RAIZ
	 */

	public Resolver(InetAddress DNS_RAIZ) {
		this.DNS_RAIZ = DNS_RAIZ;
	}

	/**
	 * En caso de que useUdp sea true (por defecto), se usara UDP como protocolo
	 * de transporte para el Socket. En caso de que useUdp sea false, se usara
	 * TCP como protocolo de transporte para el Socket.
	 * 
	 * @param useUdp
	 */

	public void setUseUdp(boolean useUdp) {
		this.useUdp = useUdp;
	}

	/**
	 * Peticion del cliente a un dominio en concreto, sin especificar el RRType.
	 * En dicho caso, se pediran todos los RRTypes al DNS raiz del dominio. RFC
	 * 1035 - RRType * = 255
	 * 
	 * @param domain
	 * @return {@link Message} Mensaje de Respuesta del servidor DNS.
	 */

	public Message request(String domain) {
		return this.request(domain, RRType.ALL);
	}

	/**
	 * Peticion del cliente a un dominio y con un RRType especifico. Al no
	 * especificarse el valor de la Clase, se usa por defecto la clase referente
	 * a Internet => IN.
	 * 
	 * @param domain
	 * @param questionType
	 * @return {@link Message} Mensaje de Respuesta del servidor DNS.
	 */

	public Message request(String domain, RRType questionType) {
		return this.request(domain, questionType, RRClass.IN);
	}

	/**
	 * Peticion del cliente a un dominio en concreto, con un RRType especifico y
	 * una Clase definida. Tambien es llamado desde anteriores request como
	 * metodo request final. Crea el {@link Message} llamando al metodo
	 * {@link Resolver#createMessage(String, RRType, RRClass)} y pasandole los 3
	 * parametros de entrada. El mismo Mensaje se le pasa al metodo
	 * {@link Resolver#getResponse(Message)}.
	 * 
	 * @param domain
	 * @param questionType
	 * @param questionClass
	 * @return {@link Message} Mensaje de Respuesta del servidor DNS.
	 */

	public Message request(String domain, RRType questionType, RRClass questionClass) {
		Message requestMessage = createMessage(domain, questionType, questionClass);
		return getResponse(requestMessage);
	}

	/**
	 * Segun se haya introducido en los parametros de entrada, TCP o UDP se
	 * creara un socket distinto. Si se introdujo TCP, la variable global useUdp
	 * valdra false, en caso contrario valdra true.
	 * 
	 * @param requestMessage
	 * @return {@link Message} Mensaje de Respuesta del servidor DNS.
	 */

	protected Message getResponse(Message requestMessage) {
		UDP_Socket udp_socket;
		TCP_Socket tcp_socket;

		if (useUdp) {
			udp_socket = new UDP_Socket(DNS_RAIZ);
			return udp_socket.sendQuery(requestMessage);
		} else {
			tcp_socket = new TCP_Socket(DNS_RAIZ);
			return tcp_socket.sendQuery(requestMessage);
		}

	}

	/**
	 * Se crea el nuevo Mensaje de peticion pasandole al cosntructor de
	 * {@link Message},
	 * {@link Message#Message(es.uvigo.det.ro.simpledns.DomainName, RRType, boolean)}
	 * los 3 parametros de entrada.
	 * 
	 * @param domain
	 * @param questionType
	 * @param questionClass
	 * @return {@link Message} Mensaje de Peticion al servidor DNS.
	 */

	private Message createMessage(String domain, RRType questionType, RRClass questionClass) {
		Message requestMessage = new Message(domain, questionType, true);
		return requestMessage;
	}
}
