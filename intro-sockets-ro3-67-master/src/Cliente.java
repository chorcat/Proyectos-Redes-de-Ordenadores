import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {

	public static void main(String[] args) throws UnknownHostException {
		int serverPort;
		String serverHost;
		byte[] bufer = new byte[256];
		String parametro;
		DataInputStream in;
		DataOutputStream out;
		Socket socket;
		DatagramSocket udp_socket;
		DatagramPacket envpacket, packet;

		parametro = args[0];
		serverHost = args[1];
		serverPort = Integer.parseInt(args[2]);
		InetAddress address = InetAddress.getByName(serverHost);
		System.out.println(address);

		if (parametro.equals("-udp")) {
			envpacket = new DatagramPacket("".getBytes(), "".length(), address, serverPort);
			packet = new DatagramPacket(bufer, bufer.length);
			try {
				udp_socket = new DatagramSocket();
				udp_socket.send(envpacket);
				udp_socket.receive(packet);
				System.out.println(new String(bufer));
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {

			try {
				Scanner entrada = new Scanner(System.in);
				socket = new Socket(serverHost, serverPort);
				int opcion;
				do {
					opcion = 0;
					System.out.println("1.- Pedir proverbio.");
					System.out.println("2.- Salir.");
					System.out.println("Escoja opción: ");
					if (entrada.hasNextLine()) {
						opcion = Integer.parseInt(entrada.nextLine());
						out = new DataOutputStream(socket.getOutputStream());
						out.writeInt(opcion);
						if (opcion != 2) {
							in = new DataInputStream(socket.getInputStream());
							System.out.println(in.readUTF());
						}
					}
				} while (opcion != 2);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
