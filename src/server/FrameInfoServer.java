package server;

import java.awt.Color;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author David López González
 */
public class FrameInfoServer extends javax.swing.JFrame
{

    // Datos referentes a los equipos conectados
    String[] nombreColumnasCon ={"Direccion_IP","Nombre usuario", "Hora_conexion"};
    Object[][] datosConectados ={};
    DefaultTableModel dtmConectados = new DefaultTableModel(datosConectados, nombreColumnasCon);

    // Datos referentes a los equipos desconectados
    String[] nombreColumnasDesc ={"Direccion_IP","Nombre usuario", "Hora_conexion"};
    Object[][] datosDesconectados ={};
    DefaultTableModel dtmDesconectados = new DefaultTableModel(datosDesconectados, nombreColumnasDesc);

    public FrameInfoServer()
    {
        initComponents();
        tClientesConectados.setModel(dtmConectados);
        tClientesDesconectados.setModel(dtmDesconectados);

        tClientesConectados.setBackground(Color.GREEN);
        tClientesDesconectados.setBackground(Color.RED);
    }

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tClientesConectados = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tClientesDesconectados = new javax.swing.JTable();
        bFinalizar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
                cerrarConexionFrameCerrado(evt);
            }
        });

        tClientesConectados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String []
            {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tClientesConectados);

        tClientesDesconectados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String []
            {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tClientesDesconectados);

        bFinalizar.setText("Finalizar");
        bFinalizar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                bFinalizarActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Monospaced", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Servidor Agrogestión");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addComponent(bFinalizar)
                .addGap(280, 280, 280))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bFinalizar))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 773, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bFinalizarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_bFinalizarActionPerformed
    {//GEN-HEADEREND:event_bFinalizarActionPerformed
        int sinAcabar = tClientesConectados.getRowCount() - tClientesDesconectados.getRowCount();

        if (sinAcabar > 0)
        {
            int confirmado = JOptionPane.showConfirmDialog(this.getParent(), "Hay clientes abiertos, ¿desea cerrar?");
            if (JOptionPane.OK_OPTION == confirmado)
            {
                try
                {
                    guardarLogSalir();
                } catch (IOException ex)
                {
                    Logger.getLogger(FrameInfoServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if (sinAcabar == 0)
        {
            try
            {
                guardarLogSalir();
            } catch (IOException ex)
            {
                Logger.getLogger(FrameInfoServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_bFinalizarActionPerformed

    private void cerrarConexionFrameCerrado(java.awt.event.WindowEvent evt)//GEN-FIRST:event_cerrarConexionFrameCerrado
    {//GEN-HEADEREND:event_cerrarConexionFrameCerrado
        JOptionPane.showMessageDialog(null,"Gracias por trabajar con nosotros");
    }//GEN-LAST:event_cerrarConexionFrameCerrado

    // Añade una nueva fila a la tabla de clientes conectados (dirección ip y fecha de conexión)
    public void nuevaFilaConectados(String ip, String usu,String fecha)
    {
        Object[] fila =
        {
            ip,usu, fecha
        };
        dtmConectados.addRow(fila);
    }

    // Añade una nueva fila a la tabla de clientes desconectados (dirección ip y fecha de desconexión)
    public void nuevaFilaDesconectados(String ip, String usu, String fecha)
    {
        Object[] fila =
        {
            ip,usu, fecha
        };
        dtmDesconectados.addRow(fila);
    }

    private void guardarLogSalir() throws FileNotFoundException, IOException
    {
        JFileChooser explorador = new JFileChooser();
        if (JFileChooser.APPROVE_OPTION == explorador.showSaveDialog(this))
        {
            String ruta = explorador.getSelectedFile().getPath();
            JOptionPane.showMessageDialog(null, "Guardando en:\n" + ruta);

            try
            {
                DataOutputStream out = new DataOutputStream(new FileOutputStream(ruta));
                out.writeBytes("FICHERO DE INFORMACION DE LAS CONEXIONES ESTABLECIDAS POR LOS CLIENTES" + '\n' + '\n');
                out.writeBytes("DIRECCIONES IP Y FECHA/HORA DE CONEXION DE LOS CLIENTES" + '\n');

                for (int i = 0; i < tClientesConectados.getRowCount(); i++)
                {
                    for (int j = 0; j < tClientesConectados.getColumnCount(); j++)
                    {
                        String valor = dtmConectados.getValueAt(i, j).toString();
                        out.writeBytes(valor + '\t' + '\t');
                    }
                    out.writeBytes("" + '\n');
                }

                out.writeBytes("" + '\n' + '\n');
                out.writeBytes("DIRECCIONES IP Y FECHA/HORA DE DESCONEXION DE LOS CLIENTES" + '\n');

                for (int i = 0; i < tClientesDesconectados.getRowCount(); i++)
                {
                    for (int j = 0; j < tClientesDesconectados.getColumnCount(); j++)
                    {
                        String valor = dtmDesconectados.getValueAt(i, j).toString();
                        out.writeBytes(valor + '\t' + '\t');
                    }
                    out.writeBytes("" + '\n');
                }

                out.close();
            } catch (Exception e)
            {
                JOptionPane.showMessageDialog(this.getParent(), "Error");
            }
            System.exit(0);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Exception
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(FrameInfoServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(FrameInfoServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(FrameInfoServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(FrameInfoServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new FrameInfoServer().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bFinalizar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tClientesConectados;
    private javax.swing.JTable tClientesDesconectados;
    // End of variables declaration//GEN-END:variables
}
