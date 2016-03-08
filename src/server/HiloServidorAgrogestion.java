package server;

import conexion.ConexionBBDD;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
                }
                else
                {
                    user = entradaSocket.readLine();
                    System.out.println(user);
                    pass = entradaSocket.readLine();
                    System.out.println(pass);

                    String resultadoLogin = null;
                    ///prueba de login
                    try
                    {
                        resultadoLogin = utils.utilsSql.loginUser(user, pass);
                    } catch (ClassNotFoundException | SQLException ex)
                    {
                        Logger.getLogger(HiloServidorAgrogestion.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (resultadoLogin.equals("normal"))
                    {
                        System.out.println("normal");
                    }
                    else if (resultadoLogin.equals("root"))
                    {
                        System.out.println("root");
                    }
                    else if (resultadoLogin.equals("fallo"))
                    {
                        System.out.println("fallo");
                    }
                    if (resultadoLogin.equals("normal") || resultadoLogin.equals("root"))
                    {
                        vInfo.nuevaFilaConectados(socketCliente.getInetAddress().getHostAddress(), user, fechaInicio);
                        salidaSocket.writeBytes(("OK_INICIO") + '\n');

                        while (finalizar == false)
                        {
                            System.out.println("Estamos esperando para leer comando hiloAgrogestionServer");
                            comando = entradaSocket.readLine();
                            System.out.println("comando leido hiloAgrogestionServer");

                            System.out.println("Antes de consulta a consulta");
                            if (comando.compareToIgnoreCase(("CONSULTA")) == 0)
                            {
                                System.out.println("Entramos a consulta");
                                select = entradaSocket.readLine();
                                System.out.println("Imprimiomos la consulata" + select);
                                tablaConsulta = entradaSocket.readLine();
                                System.out.println("Imprimiomos la tabla con: " + tablaConsulta);

                                salidaSocket.writeBytes(("OK_DATOS") + '\n');

                                ConexionBBDD con = new ConexionBBDD();
                                try
                                {
                                    ResultSet rs;
                                    rs = con.hacerConsulta(select);
                                    ObjectOutputStream resultadoCon = new ObjectOutputStream(socketCliente.getOutputStream());

                                    //metadata
                                    ResultSetMetaData rsmd = rs.getMetaData();
                                    int cantidadColumnas = rsmd.getColumnCount();
                                    if (rs == null)
                                    {
                                        resultadoCon.writeObject(new Integer(0));
                                    }
                                    else
                                    {
                                        int numRegistros = 0;
                                        while (rs.next())
                                        {
                                            numRegistros++;
                                        }

                                        resultadoCon.writeObject(new Integer(numRegistros));
                                        rs.beforeFirst();

                                        if (tablaConsulta.compareToIgnoreCase(("FINCA")) == 0)
                                        {
                                            while (rs.next())
                                            {
                                                for (int i = 0; i < cantidadColumnas; i++)
                                                {
                                                    resultadoCon.writeObject(rs.getObject(i + 1));
                                                }
                                                /*resultadoCon.writeObject(rs.getString("Nombre"));
                                                 resultadoCon.writeObject(rs.getString("Localizacion"));
                                                 resultadoCon.writeObject(rs.getString("Descripcion"));*/
                                            }
                                            System.out.println("fin de consulta finca");
                                        } ////finmio
                                        else if (tablaConsulta.compareToIgnoreCase(("PARCELA")) == 0)
                                        {
                                            while (rs.next())
                                            {
                                                for (int i = 0; i < cantidadColumnas; i++)
                                                {
                                                    resultadoCon.writeObject(rs.getObject(i + 1));
                                                }
                                                /*resultadoCon.writeObject(rs.getString("IdParcela"));
                                                 resultadoCon.writeObject(rs.getString("IdFinca"));
                                                 resultadoCon.writeObject(rs.getString("Descripcion"));*/
                                            }
                                        }
                                        else if (tablaConsulta.compareToIgnoreCase(("TIPO")) == 0)
                                        {
                                            while (rs.next())
                                            {
                                                for (int i = 0; i < cantidadColumnas; i++)
                                                {
                                                    resultadoCon.writeObject(rs.getObject(i + 1));
                                                }
                                                /*resultadoCon.writeObject(rs.getString("Nombre"));
                                                 resultadoCon.writeObject(rs.getString("Descripcion"));*/
                                            }
                                        }
                                        else if (tablaConsulta.compareToIgnoreCase(("VARIEDAD")) == 0)
                                        {
                                            while (rs.next())
                                            {
                                                for (int i = 0; i < cantidadColumnas; i++)
                                                {
                                                    resultadoCon.writeObject(rs.getObject(i + 1));
                                                }
                                                /*resultadoCon.writeObject(rs.getString("Nombre"));
                                                 resultadoCon.writeObject(rs.getString("IdTipo"));*/
                                            }
                                        }
                                        else if (tablaConsulta.compareToIgnoreCase(("CULTIVAR")) == 0)
                                        {
                                            while (rs.next())
                                            {
                                                for (int i = 0; i < cantidadColumnas; i++)
                                                {
                                                    resultadoCon.writeObject(rs.getObject(i + 1));
                                                }
                                                /*resultadoCon.writeObject(rs.getString("Id"));
                                                 resultadoCon.writeObject(rs.getString("FechaInicio"));
                                                 resultadoCon.writeObject(rs.getString("FechaFin"));
                                                 resultadoCon.writeObject(rs.getString("IdVariedad"));
                                                 resultadoCon.writeObject(rs.getString("IdParcela"));
                                                 resultadoCon.writeObject(rs.getString("Unidades"));*/

                                            }
                                        }
                                        else if (tablaConsulta.compareToIgnoreCase(("INGRESOVENTA")) == 0)
                                        {
                                            while (rs.next())
                                            {
                                                for (int i = 0; i < cantidadColumnas; i++)
                                                {
                                                    resultadoCon.writeObject(rs.getObject(i + 1));
                                                }
                                                /*resultadoCon.writeObject(rs.getString("Id"));
                                                 resultadoCon.writeObject(rs.getString("Fecha"));
                                                 resultadoCon.writeObject(rs.getString("NombreCliente"));
                                                 resultadoCon.writeObject(rs.getString("PrecioUnidad"));
                                                 resultadoCon.writeObject(rs.getString("Cantidad"));
                                                 resultadoCon.writeObject(rs.getString("Total"));
                                                 resultadoCon.writeObject(rs.getString("IdCultivar"));
                                                 resultadoCon.writeObject(rs.getString("Cobrado"));*/

                                            }

                                        }
                                        else if (tablaConsulta.compareToIgnoreCase(("INGRESOOTRO")) == 0)
                                        {
                                            while (rs.next())
                                            {
                                                for (int i = 0; i < cantidadColumnas; i++)
                                                {
                                                    resultadoCon.writeObject(rs.getObject(i + 1));
                                                }
                                            }
                                        }
                                        else if (tablaConsulta.compareToIgnoreCase(("TRABAJADOR")) == 0)
                                        {
                                            while (rs.next())
                                            {
                                                for (int i = 0; i < cantidadColumnas; i++)
                                                {
                                                    resultadoCon.writeObject(rs.getObject(i + 1));
                                                }
                                            }
                                        }
                                        else if (tablaConsulta.compareToIgnoreCase(("GASTOMANOOBRA")) == 0)
                                        {
                                            while (rs.next())
                                            {
                                                for (int i = 0; i < cantidadColumnas; i++)
                                                {
                                                    resultadoCon.writeObject(rs.getObject(i + 1));
                                                }
                                            }
                                        }
                                        else if (tablaConsulta.compareToIgnoreCase(("GASTOPRODUCTO")) == 0)
                                        {
                                            while (rs.next())
                                            {
                                                for (int i = 0; i < cantidadColumnas; i++)
                                                {
                                                    resultadoCon.writeObject(rs.getObject(i + 1));
                                                }
                                            }
                                        }
                                        else if (tablaConsulta.compareToIgnoreCase(("GASTOOTRO")) == 0)
                                        {
                                            while (rs.next())
                                            {
                                                for (int i = 0; i < cantidadColumnas; i++)
                                                {
                                                    resultadoCon.writeObject(rs.getObject(i + 1));
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
                            }
                            else if (comando.compareToIgnoreCase(("FIN")) == 0)
                            {
                                finalizar = true;
                                fechaFin = entradaSocket.readLine();
                                vInfo.nuevaFilaDesconectados(socketCliente.getInetAddress().getHostAddress(), user, fechaFin);

                                salidaSocket.writeBytes(("OK_FIN") + '\n');
                                salidaSocket.close();
                                System.out.println("socket cerrado");
                            }
                        }//fin while
                    }else{
                    //ERROR_INICIO
                         salidaSocket.writeBytes(("ERROR_INICIO") + '\n');
                          salidaSocket.close();
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
