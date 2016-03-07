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
                Socket socketConexion = socketPrincipal.accept();
                HiloServidorAgrogestion hilo = new HiloServidorAgrogestion(socketConexion, info, conectados++);
                hilo.start();
            }
        } catch (Exception ex)
        {
            System.out.println("Fallo al crear la conexion");
        }
    }
}
