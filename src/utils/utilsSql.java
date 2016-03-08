/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import conexion.ConexionBBDD;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author David López González
 */
public class utilsSql
{

    public static String loginUser(String user, String pass) throws ClassNotFoundException, SQLException
    {
        ConexionBBDD c = new ConexionBBDD();
        String DNI = null;
        String bbddPass = null;
        String tipo = null;
        try
        {
            ResultSet rs = c.hacerConsulta("SELECT DNI, Contrasenya,Tipo FROM TTrabajador WHERE DNI = \"" + user + "\";");
            rs.next();
            DNI = rs.getString(1);
            bbddPass = rs.getString(2);
            tipo = rs.getString(3);
            if (pass.equals(bbddPass) && tipo.equals("root"))
            {
                return "root";
            }
            else if (pass.equals(bbddPass) && tipo.equals("normal"))
            {
                return "normal";
            }
        } catch (Exception e)
        {
            return "fallo";
        }
        return "fallo";
    }
}
