import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class HiloTCP extends Thread{
	
	private int serverPort;
	private List<String> lista_mensajes;
	
	public HiloTCP(int serverPort, List<String> lista_mensajes) {
		this.serverPort = serverPort;
		this.lista_mensajes = lista_mensajes;
	}
	
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(serverPort);
			while(true) {
				Socket socket;
				socket = serverSocket.accept();
				HiloServidor hilo = new HiloServidor(socket, lista_mensajes);
				hilo.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
