
package zoghpiclient;

import java.net.*;
import java.io.*;

public class Client {

	public static void main(String[] args) throws SocketException, IOException {

		try {

			InetAddress ip = InetAddress.getByName("localhost");

			Socket s = new Socket(ip, 1234);

			BufferedReader clientRead = new BufferedReader(new InputStreamReader(System.in));

			InetAddress IP = InetAddress.getByName("127.0.0.1");
			// int c=5;
			DatagramSocket clientSocket = new DatagramSocket();
			while (true) // true
			{
				System.out.println("\n Enter Name / Enter 'quit' to exit");
				byte[] sendbuffer = new byte[1024];
				byte[] receivebuffer = new byte[1024];

				System.out.print("\nClient: ");
				String clientData = clientRead.readLine();
				sendbuffer = clientData.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendbuffer, sendbuffer.length, IP, 1234);
				clientSocket.send(sendPacket);
				if (clientData.equals("quit")) {
					System.out.println("Closing this connection : " + s); 
                    s.close(); 
                    System.out.println("Connection closed");
					break;
				}
				DatagramPacket receivePacket = new DatagramPacket(receivebuffer, receivebuffer.length);
				clientSocket.receive(receivePacket);
				String serverData = new String(receivePacket.getData());
				System.out.print("\nServer: \n" + serverData);

				// checking condition for equals with serverData which is my string
				// c--;
			}
			clientSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

}
