package zoghpiclient;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AutherativeServer {

    static ServerSocket ss;
    static Socket s;
    static DatagramSocket serverSocket;
    static DatagramPacket recvdpkt;
    static byte[] receivebuffer = new byte[1024];
    static byte[] sendbuffer = new byte[1024];

    public static void main(String[] args) throws IOException {
        System.out.println(" welcom in main ");
        serverSocket = new DatagramSocket(4441);
        while (true) {
            try {                
                recvdpkt = new DatagramPacket(receivebuffer, receivebuffer.length);
                serverSocket.receive(recvdpkt);
                String clientdata = new String(receivebuffer, 0, receivebuffer.length);
                System.out.println(" clientdata " + clientdata);
                InetAddress IP = recvdpkt.getAddress();
                int portno = recvdpkt.getPort();
                System.out.println(" portno " + portno);
                clientdata = clientdata.trim();

                String toSend = getAutherative(clientdata);
                System.out.println(" toSend " + toSend);

                //if (toSend.equals("notFound")) {
                    /*System.out.println("Entered if condition not");
                    DatagramSocket serverSocket2 = new DatagramSocket(5555);
                    byte[] receivebuffer2 = new byte[1024];
                    byte[] sendbuffer2 = new byte[1024];
                    //send to 2nd server
                    sendbuffer2 = clientdata.getBytes();
                    System.out.println("sendbuffer2 = " + sendbuffer2);
                    DatagramPacket sendPacket2 = new DatagramPacket(sendbuffer2, sendbuffer2.length, IP, 5551);
                    System.out.println("sendPacket2 = " + sendbuffer2);
                    serverSocket.send(sendPacket2);
                    System.out.println("serverSocket = " + serverSocket);

                    //recieve from 2nd server
                    DatagramPacket recvdpkt2 = new DatagramPacket(receivebuffer2, receivebuffer2.length);
                    serverSocket.receive(recvdpkt2);
                    String server2data = new String(receivebuffer2, 0, receivebuffer2.length);

                    System.out.println(" server2data = " + server2data);
                    InetAddress IP2 = recvdpkt2.getAddress();
                    int portno2 = recvdpkt2.getPort();
                    System.out.println(" portno2 " + portno2);

                    sendbuffer = server2data.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendbuffer, sendbuffer.length, IP, portno);//portno of client
                    serverSocket.send(sendPacket);
                    
                    serverSocket2.close();*/
                    
                sendbuffer = toSend.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendbuffer, sendbuffer.length, IP, portno);
                String serverData = new String(sendPacket.getData());
                System.out.println(" serverData " + serverData);
                serverSocket.send(sendPacket);
                
                //serverSocket.close();            
//                }
                    /*else {

                    sendbuffer = toSend.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendbuffer, sendbuffer.length, IP, portno);
                    String serverData = new String(sendPacket.getData());
                    System.out.println(" serverData " + serverData);
                    serverSocket.send(sendPacket);
                }*/

            } catch (SocketException ex) {
                Logger.getLogger(rootServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

    public static String getAutherative(String receive) throws IOException {
        String toSend = "notFound";
        try (RandomAccessFile local = new RandomAccessFile("authoritative_dns_table.txt", "r")) {
            while (true) {
                String line = local.readLine();
                String[] split = (line.split(" "));

                if (line == null) {
                    break;
                } else if (split[0].equals(receive)) {
                    toSend = "Reply from Server is : URL=" + split[0] + "\n"
                            + "IP Address=" + split[1] + "\n"
                            + "Quary Type = A,NS" + "\n" + "Server name = Autherative DNS \n";

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
