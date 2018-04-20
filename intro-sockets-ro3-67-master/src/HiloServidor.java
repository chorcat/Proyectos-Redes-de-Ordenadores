import java.net.*;
import java.io.*;
import java.util.*;

public class HiloServidor extends Thread {
	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	private List<String> lista_mensajes;

	public HiloServidor(Socket soc, List<String> lista_mensajes) {
		socket = soc;
		this.lista_mensajes = lista_mensajes;
	}

	public void run() {
		String mensaje;
		int opcion_cliente = 0;
		try {
			while (opcion_cliente != 2) {
				out = new DataOutputStream(socket.getOutputStream());
				in = new DataInputStream(socket.getInputStream());
				opcion_cliente = in.readInt();
				Random r = new Random();
				mensaje = lista_mensajes.get(r.nextInt(lista_mensajes.size()));
				if(opcion_cliente != 2)
					out.writeUTF(mensaje);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Un cliente ha finalizado su ejecucion");
		}
	}
}
