package transporte;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import es.uvigo.det.ro.simpledns.Message;

/**
 * En caso de seleccionar como primer parametro de entrada -t (TCP), la conexion
 * con el servidor raiz se realizara a traves de un Socket TCP.
 * 
 * @author Borja Gonzalez Enriquez - ro71
 * @author David Quintas Vilanova - ro67
 * @see UDP_Socket
 *
 */

public class TCP_Socket {

	private final InetAddress server;
	private final int port;

	/**
	 * Constructor al que se le pasa solo la IP del DNS raiz. Pasa al segundo
	 * constructor dicha IP y el puerto 53 para el Socket.
	 * 
	 * @param server IP del DNS raiz con el que se establece el Socket TCP.
	 */
	
	public TCP_Socket(InetAddress server) {
		this(server, 53);
	}

	/**
	 * Constructor al que ya se le pasa la IP del DNS raiz y tambien el puerto
	 * para el Socket.
	 * 
	 * @param server IP del DNS raiz con el que se establece el Socket TCP.
	 * @param port Puerto para establecer el Socket con el servidor.
	 */
	
	public TCP_Socket(InetAddress server, int port) {
		this.server = server;
		this.port = 53;
	}
	
	/**
	 * Este metodo es llamado desde la clase {@link Resolver}, en especifico
	 * desde el metodo {@link Resolver#getResponse(Message)}.
	 * 
	 * Se encarga de establecer el Socket TCP con el DNS raiz a traves de los
	 * parametros {@link #DNS_RAIZ} y {@link #DNS_PORT}. Una vez envia el
	 * mensaje de consulta, se queda esperando a recibir el mensaje de respuesta
	 * del DNS raiz, lo pasa a un array de bytes y crea el nuevo {@link Message}
	 * de respuesta a traves del constructor {@link Message#Message(byte[])}.
	 * 
	 * @param request
	 *            {@link Message} creado en {@link Resolver} que se envia al
	 *            Servidor DNS raiz.
	 * @return {@link Message} creado a traves de la respuesta en bytes del DNS
	 *         raiz.
	 */

	public Message sendQuery(Message request) {
		Message responseMessage = null;
		try {
			byte[] messageBytes;
			messageBytes = request.toByteArray();

			Socket socket = new Socket(server, 53);
			OutputStream out = socket.getOutputStream();
			InputStream in = socket.getInputStream();
			int requestLength = messageBytes.length;

			out.write(new byte[] { (byte) ((requestLength >> 8) & 0xFF), (byte) requestLength });
			out.write(messageBytes);
			out.flush();

			int b1 = in.read();
			if (b1 == -1) {
				throw new IOException("Didn't receive any input.");
			}

			int b2 = in.read();
			if (b2 == -1) {
				throw new IOException("Didn't receive second length byte.");
			}

			int length = ((b1 & 0xFF) << 8) | (b2 & 0xFF);
			byte[] replyBytes = new byte[length];
			byte[] buf = new byte[4096];
			int readTotal = 0;
			while (readTotal < length) {
				int read = in.read(buf);
				if (read == -1) {
					throw new IOException("Stream closed but only read " + readTotal + " of " + length + " bytes.");
				}
				System.arraycopy(buf, 0, replyBytes, readTotal, read);
				readTotal += read;
			}
			
			responseMessage = new Message(replyBytes);
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseMessage;
	}
}