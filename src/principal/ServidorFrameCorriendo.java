package principal;

import java.net.ServerSocket;
import java.net.Socket;
import server.FrameInfoServer;
import server.HiloServidorAgrogestion;

/**
 *
 * @author David López González
 */
public class ServidorFrameCorriendo
{

    public static void main(String argv[])
    {
        try
        {
            int conectados = 0;
            ServerSocket socketPrincipal = new ServerSocket(6789);
            //Muestra la pantalla de información de los clientes conectados y desconectados
            FrameInfoServer info = new FrameInfoServer();
            info.setVisible(true);

            //Para cada conexión nueva crea un hilo servidor que atenderá a ese cliene concreto
            while (true)
            {
                System.out.println("servidor frame 0");
                Socket socketConexion = socketPrincipal.accept();
                System.out.println("servidor frame 1");
                HiloServidorAgrogestion hilo = new HiloServidorAgrogestion(socketConexion, info, conectados++);
                System.out.println("servidor frame 2");
                hilo.start();
                System.out.println("servidor frame 3");
            }
        } catch (Exception ex)
        {
            System.out.println("Fallo al crear la conexion");

        }
    }
}
