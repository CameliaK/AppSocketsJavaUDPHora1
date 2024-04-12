import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.text.DateFormatter;

public class ServidorUDP {

    public static void main(String[] args) throws SocketException, IOException {
        int puerto = 8080;
        int retardo = 6000;
        
        try(DatagramSocket socketUDP = new DatagramSocket(puerto)){
            byte[] buffer = new byte[1024];
            System.out.println("Server escuchando en el puerto: " + puerto + " ...");
            while(true){
                DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);
                socketUDP.receive(peticion);
                System.out.println("Dataram recibido: " + peticion.getAddress());
                System.out.println("desde el puerto remoto: " + peticion.getPort());
                System.out.println("Datos recibidos: " + new String(peticion.getData()));


                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String strFecha = now.format(formatter);
                System.out.println("La hora del server es: " + strFecha);

                try{
                    System.out.println("Simulamos un retardo "+ retardo);
                    Thread.sleep(retardo);
                } catch (InterruptedException ex){
                    ex.printStackTrace();
                }

                DatagramPacket respuesta =  new DatagramPacket(strFecha.getBytes(), strFecha.getBytes().length, peticion.getAddress(), peticion.getPort());
                
                socketUDP.send(respuesta);
                System.out.println("Datos enviados");

            }
        }
    }
}