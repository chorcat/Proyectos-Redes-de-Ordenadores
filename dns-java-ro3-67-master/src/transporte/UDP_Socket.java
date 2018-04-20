package transporte;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import es.uvigo.det.ro.simpledns.Message;

/**
 * En caso de seleccionar como primer parametro de entrada -u (UDP), la conexion
 * con el servidor raiz se realizara a traves de un Socket UDP.
 * 
 * @author Borja Gonzalez Enriquez - ro71
 * @author David Quintas Vilanova - ro67
 * @see TCP_Socket
 *
 */

public class UDP_Socket {
	private InetAddress DNS_RAIZ;
	private int DNS_PORT;

	/**
	 * Constructor al que se le pasa solo la IP del DNS raiz. Pasa al segundo
	 * constructor dicha IP y el puerto 53 para el Socket.
	 * 
	 * @param DNS_RAIZ
	 *            IP del DNS raiz con el que se establece el Socket UDP.
	 */

	public UDP_Socket(InetAddress DNS_RAIZ) {
		this(DNS_RAIZ, 53);
	}

	/**
	 * Constructor al que ya se le pasa la IP del DNS raiz y tambien el puerto
	 * para el Socket.
	 * 
	 * @param DNS_RAIZ
	 *            IP del DNS raiz con el que se establece el Socket UDP.
	 * @param DNS_PORT
	 *            Puerto para establecer el Socket con el servidor.
	 */

	public UDP_Socket(InetAddress DNS_RAIZ, int DNS_PORT) {
		this.DNS_RAIZ = DNS_RAIZ;
		this.DNS_PORT = DNS_PORT;
	}

	/**
	 * Este metodo es llamado desde la clase {@link Resolver}, en especefico
	 * desde el metodo {@link Resolver#getResponse(Message)}.
	 * 
	 * Se encarga de establecer el Socket UDP con el DNS raiz a traves de los
	 * parametros {@link #DNS_RAIZ} y {@link #DNS_PORT}. Una vez envia el
	 * mensaje de consulta, se queda esperando a recibir el mensaje de respuesta
	 * del DNS raiz, lo pasa a un array de bytes y crea el nuevo {@link Message}
	 * de respuesta a traves del constructor {@link Message#Message(byte[])}.
	 * 
	 * @param requestMessage
	 *            {@link Message} creado en {@link Resolver} que se envia al
	 *            Servidor DNS raiz.
	 * @return {@link Message} creado a traves de la respuesta en bytes del DNS
	 *         raiz.
	 */

	public Message sendQuery(Message requestMessage) {
		Message responseMessage = null;
		try {
			byte[] messageBytes = requestMessage.toByteArray();
			DatagramPacket requestPacket = new DatagramPacket(messageBytes, messageBytes.length, DNS_RAIZ, DNS_PORT);

			try (DatagramSocket socket = new DatagramSocket()) {
				socket.send(requestPacket);

				byte[] buf = new byte[1024];
				DatagramPacket responsePacket = new DatagramPacket(buf, buf.length);
				socket.receive(responsePacket);

				byte[] responseBytes = new byte[responsePacket.getLength()];
				System.arraycopy(responsePacket.getData(), responsePacket.getOffset(), responseBytes, 0,
						responsePacket.getLength());

				responseMessage = new Message(responseBytes);
				return responseMessage;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseMessage;
	}
}
