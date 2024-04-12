import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ClienteUDP {
    public static void main(String[] args) throws SocketException, UnknownHostException, IOException {
        String mensaje = new String("Dame la hora local");
        String server = new String("localhost");

        int puerto = 8080;
        int retardo = 5000;

        DatagramSocket socketUDP = new DatagramSocket();
        InetAddress hostservidor = InetAddress.getByName(server);

        DatagramPacket peticion = new DatagramPacket(mensaje.getBytes(), mensaje.getBytes().length, hostservidor, puerto);
        socketUDP.setSoTimeout(retardo);
        System.out.println("Esperamos datos en un maximo de: "+ retardo+ "ms");
        socketUDP.send(peticion);

        try{
            byte[] buffer = new byte[1024];
            DatagramPacket respuesta = new DatagramPacket(buffer, buffer.length);
            socketUDP.receive(respuesta);

            String strText = new String(respuesta.getData(), 0, respuesta.getLength());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime horaServidor = LocalDateTime.parse(strText, formatter);
            
            LocalDateTime now = LocalDateTime.now();
            String strFecha = now.format(formatter);

            System.out.println("La hora del cliente es: " + strFecha);
            System.out.println("La hora del server es: "+ horaServidor.format(formatter));

            socketUDP.close();

        } catch(SocketTimeoutException s){
            System.out.println("Tiempo de espera alcanzado "+ s.getMessage());
        }
    }
}
