package zoghpiclient;

import java.net.*;
import java.io.*;

public class Server {

    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket(1234);

        while (true) {
            Socket s = null;

            try {
                s = ss.accept();

                System.out.println("A new client is connected : " + s);

                System.out.println("Assigning new thread for this client");

                Thread t = new ClientHandler(s);

                t.start();

            } catch (Exception e) {
                s.close();
                ss.close();
                e.printStackTrace();
                // TODO: handle exception
            }
        }

    }
}

class ClientHandler extends Thread {

    final Socket s;

    public ClientHandler(Socket s) {
        this.s = s;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void run() {
        try {
            DatagramSocket serverSocket = new DatagramSocket(1234);

            // int c=5;
            while (true) // instead of c i want to use true
            {
                byte[] receivebuffer = new byte[1024];
                byte[] sendbuffer = new byte[1024];
                DatagramPacket recvdpkt = new DatagramPacket(receivebuffer, receivebuffer.length);
                serverSocket.receive(recvdpkt);
                String clientdata = new String(receivebuffer, 0, receivebuffer.length);

                InetAddress IP = recvdpkt.getAddress();
                int portno = recvdpkt.getPort();
                System.out.println(" portno " + portno);
                // Kda Ana Gebt El-Client Data
				/*String clientdata = new String(recvdpkt.getData());*/
                clientdata = clientdata.trim();
                /*sendbuffer = clientdata.getBytes();
                 DatagramPacket sendPacket = new DatagramPacket(sendbuffer, sendbuffer.length, IP, portno);
                 serverSocket.send(sendPacket);
                 */
                System.out.println("Server Received : " + clientdata);

                if (clientdata.equals("quit")) {
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    break;
                }

                String toSend = getLocal(clientdata);

                
                //to send to the second server
                if (toSend.equals("notFound")) {
                    System.out.println("Entered if condition not");
                    DatagramSocket serverSocket2 = new DatagramSocket(2222);
                    byte[] receivebuffer2 = new byte[1024];
                    byte[] sendbuffer2 = new byte[1024];
                    //send to 2nd server
                    sendbuffer2 = clientdata.getBytes();
                    System.out.println("sendbuffer2 = "+sendbuffer2);
                    DatagramPacket sendPacket2 = new DatagramPacket(sendbuffer2, sendbuffer2.length, IP, 2221);
                    System.out.println("sendPacket2 = "+sendbuffer2);
                    serverSocket.send(sendPacket2);
                    System.out.println("serverSocket = "+serverSocket);

                    //recieve from 2nd server
                    DatagramPacket recvdpkt2 = new DatagramPacket(receivebuffer2, receivebuffer2.length);
                    serverSocket.receive(recvdpkt2);
                    String server2data = new String(receivebuffer2, 0, receivebuffer2.length);
                    
                    System.out.println(" server2data = "+server2data);                    
                    InetAddress IP2 = recvdpkt2.getAddress();
                    int portno2 = recvdpkt2.getPort();
                    System.out.println(" portno2 " + portno2);
                    
                    //send to client the result
                    sendbuffer = server2data.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendbuffer, sendbuffer.length, IP, portno);//portno of client
                    serverSocket.send(sendPacket);
                    
                    serverSocket2.close();
                }else{

                    sendbuffer = toSend.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendbuffer, sendbuffer.length, IP, portno);
                    serverSocket.send(sendPacket);
                }
		// here the check condition for serverdata which must be bye
		/*
                 * if (serverdata.equalsIgnoreCase("bye")) {
                 * System.out.println("connection ended by server"); break; }
                 */

            }
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
    }

    public String getLocal(String receive) throws IOException {
        String toSend = "notFound";
        try (RandomAccessFile local = new RandomAccessFile("local_dns_table.txt", "r")) {
            while (true) {
                String line = local.readLine();
                String[] split = (line.split(" "));

                if (line == null) {
                    break;
                } else if (split[0].equals(receive)) {
                    toSend = "Reply from Server is : URL=" + split[0] + "\n"
                            + "IP Address=" + split[1] + "\n"
                            + "Quary Type = A,NS" + "\n" + "Server name = local DNS \n";

                    System.out.println("Client Requested : " + split[0] + "\n" + "URL=" + split[0] + "\n"
                            + "IP Address=" + split[1] + "\n"
                            + "Quary Type = A,NS" + "\n");
                    break;
                }
            }
        } catch (Exception e) {

        }
        return toSend;
    }

}
