import java.net.*;
import java.io.*;
import java.util.*;


public class Servidor extends Thread{

	private static List<String> lista_mensajes = new ArrayList<String>();
	
	public static void main(String[] args) throws IOException {
		int serverPort;
		String mensaje;
		String mensaje_recibido = "";
		byte[] bufer = new byte[256];
		String fichero_mensajes;
		serverPort = Integer.parseInt(args[0]);
		fichero_mensajes = args[1];
		crearLista(fichero_mensajes);
		HiloTCP hiloTPC = new HiloTCP(serverPort, lista_mensajes);
		hiloTPC.start();
		
		// UDP
		DatagramSocket socket = new DatagramSocket(serverPort);
		while(true) {
			try {
				Random r = new Random();
				mensaje = lista_mensajes.get(r.nextInt(lista_mensajes.size()));
				DatagramPacket packet = new DatagramPacket(bufer, bufer.length);
				socket.receive(packet);
				DatagramPacket envpacket = new DatagramPacket(mensaje.getBytes(), mensaje.length(), packet.getAddress(), packet.getPort());
				socket.send(envpacket);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static void crearLista(String fichero_mensajes) throws IOException {
		String mensaje;
		File fichero = new File(fichero_mensajes);
		FileReader fr = new FileReader(fichero);
		BufferedReader br = new BufferedReader(fr);
		while((mensaje = br.readLine()) != null) {
			lista_mensajes.add(mensaje);
		}
		br.close();
		fr.close();
	}

}
