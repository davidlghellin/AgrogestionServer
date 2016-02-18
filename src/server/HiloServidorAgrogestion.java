package server;

import conexion.ConexionBBDD;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;

/**
 *
 * @author David López González
 */
public class HiloServidorAgrogestion extends Thread
{

    /**
     * Socket para la conexión con el cliente
     */
    Socket socketCliente;
    /**
     * Formulario donde conectaremos y mostraremos datos
     */
    FrameInfoServer vInfo;
    /**
     * numero de clientes conectados
     */
    int numConectados;
    /**
     * control para el while de ejecución
     */
    boolean finalizar = false;
    /**
     * nombre de usuario
     */
    String user;
    /**
     * password de usuario
     */
    String pass;

    public HiloServidorAgrogestion(Socket socketConexion, FrameInfoServer info, int conectados)
    {
        socketCliente = socketConexion;
        vInfo = info;
        numConectados = conectados;
    }

    //Ejecución del protocolo de comunicación con un cliente
    public void run()
    {
        String fechaInicio, fechaFin;
        String comando;
        String mensaje;
        String select;
        String tablaConsulta;

        try
        {
            BufferedReader entradaSocket = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
            DataOutputStream salidaSocket = new DataOutputStream(socketCliente.getOutputStream());

            comando = entradaSocket.readLine();

            if (comando.compareToIgnoreCase("INICIO") == 0)
            {
                fechaInicio = entradaSocket.readLine();
                if (numConectados >= 10)
                {
                    salidaSocket.writeBytes(("ERROR_INICIO") + '\n');
                    finalizar = true;
                } else
                {
                    user = entradaSocket.readLine();
                    System.out.println(user);
                    pass = entradaSocket.readLine();
                    System.out.println(pass);

                    vInfo.nuevaFilaConectados(socketCliente.getInetAddress().getHostAddress(), user, fechaInicio);
                    salidaSocket.writeBytes(("OK_INICIO") + '\n');

                    while (finalizar == false)
                    {
                        comando = entradaSocket.readLine();

                        if (comando.compareToIgnoreCase(("CONSULTA")) == 0)
                        {
                            System.out.println("CONSULTA");
                            select = entradaSocket.readLine();
                            tablaConsulta = entradaSocket.readLine();

                            salidaSocket.writeBytes(("OK_DATOS") + '\n');

                            ConexionBBDD con = new ConexionBBDD();
                            try
                            {
                                ResultSet rs;
                                rs = con.hacerConsulta(select);
                                String codigo, nombre, ciudad, descripcion;
                                ObjectOutputStream resultadoCon = new ObjectOutputStream(socketCliente.getOutputStream());

                                if (tablaConsulta.compareToIgnoreCase(("ESTADISTICA")) == 0)
                                {
                                    rs.next();
                                    resultadoCon.writeObject(rs.getString("GESTIONES"));
                                } else
                                {
                                    if (rs == null)
                                    {
                                        resultadoCon.writeObject(new Integer(0));
                                    } else
                                    {
                                        int numRegistros = 0;
                                        while (rs.next())
                                        {
                                            numRegistros++;
                                        }

                                        resultadoCon.writeObject(new Integer(numRegistros));

                                        rs.beforeFirst();

                                        if (tablaConsulta.compareToIgnoreCase(("PROYECTO")) == 0)
                                        {
                                            while (rs.next())
                                            {
                                                resultadoCon.writeObject(rs.getString("CODIGO"));
                                                resultadoCon.writeObject(rs.getString("NOMBRE"));
                                                resultadoCon.writeObject(rs.getString("CIUDAD"));
                                                resultadoCon.writeObject(rs.getString("DESCRIPCION"));
                                            }
                                        } else if (tablaConsulta.compareToIgnoreCase(("PIEZA")) == 0)
                                        {
                                            while (rs.next())
                                            {
                                                resultadoCon.writeObject(rs.getString("CODIGO"));
                                                resultadoCon.writeObject(rs.getString("NOMBRE"));
                                                resultadoCon.writeObject(rs.getString("PRECIO"));
//                        resultadoCon.writeInt(rs.getInt(res.getString("PRECIO")));
                                                resultadoCon.writeObject(rs.getString("DESCRIPCION"));
                                            }
                                        } else if (tablaConsulta.compareToIgnoreCase(("GESTION")) == 0)
                                        {
                                            while (rs.next())
                                            {
                                                resultadoCon.writeObject(rs.getString("CODIGO"));
                                                resultadoCon.writeObject(rs.getString("PROVEEDOR"));
                                                resultadoCon.writeObject(rs.getString("PIEZA"));
                                                resultadoCon.writeObject(rs.getString("PROYECTO"));
//                        resultadoCon.writeInt(rs.getInt(res.getString("CANTIDAD")));
                                                resultadoCon.writeObject(rs.getString("CANTIDAD"));
                                            }
                                        } else if (tablaConsulta.compareToIgnoreCase(("PROVEEDOR")) == 0)
                                        {
                                            while (rs.next())
                                            {
                                                resultadoCon.writeObject(rs.getString("CODIGO"));
                                                resultadoCon.writeObject(rs.getString("NOMBRE"));
                                                resultadoCon.writeObject(rs.getString("APELLIDOS"));
                                                resultadoCon.writeObject(rs.getString("DIRECCION"));
                                                resultadoCon.writeObject(rs.getString("POBLACION"));
                                                resultadoCon.writeObject(rs.getString("PROVINCIA"));
                                                resultadoCon.writeObject(rs.getString("TELEFONO"));
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e)
                            {
                                finalizar = true;
                                salidaSocket.writeBytes(("ERROR_DATOS"));
                                System.out.println(e.toString());
                            }
                        } else if (comando.compareToIgnoreCase(("FIN")) == 0)
                        {
                            finalizar = true;
                            fechaFin = entradaSocket.readLine();
                            vInfo.nuevaFilaDesconectados(socketCliente.getInetAddress().getHostAddress(), user, fechaFin);

                            salidaSocket.writeBytes(("OK_FIN") + '\n');
                            salidaSocket.close();
                        }
                    }
                }
            }
        } catch (Exception e)
        {
            finalizar = true;
            System.out.println(e.toString());
        }
    }

}
