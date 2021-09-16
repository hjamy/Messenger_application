/*
The logic page for the Clients.It prompts the user to enter a name and the ip of
the server.After that it connects withh the server through a socket and disposes 
this page and calls the Chat_screen.
*/
package client_project;

import Rmi_pack.*;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import messenger_application.Home;

public class Login extends javax.swing.JFrame {

    public Login() 
    {
        initComponents();
    }
    
    private String un;
    private String IP;

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        userName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        userIP = new javax.swing.JTextField();
        Login_button = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        name_stat = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();

        jButton1.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Leelawadee UI Semilight", 1, 18)); // NOI18N
        jLabel1.setText("Username");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 160, 59));

        userName.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        userName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userNameActionPerformed(evt);
            }
        });
        jPanel1.add(userName, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 292, 59));

        jLabel2.setFont(new java.awt.Font("Leelawadee UI Semilight", 1, 18)); // NOI18N
        jLabel2.setText("IP Address");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, 140, 59));

        userIP.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        userIP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userIPActionPerformed(evt);
            }
        });
        jPanel1.add(userIP, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, 293, 59));

        Login_button.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Login_button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/client_project/images/stat.gif"))); // NOI18N
        Login_button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Login_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Login_buttonActionPerformed(evt);
            }
        });
        jPanel1.add(Login_button, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 430, 130, 120));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/client_project/images/lockkey.gif"))); // NOI18N
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 350, 60, 60));

        jPanel2.setBackground(new java.awt.Color(51, 170, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Leelawadee UI Semilight", 1, 32)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Log in");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(139, 14, -1, -1));

        jLabel6.setFont(new java.awt.Font("Perpetua", 1, 22)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Connect with others...");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(107, 68, 244, -1));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/client_project/images/login.png"))); // NOI18N
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 0, 100, 70));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 420, 140));

        name_stat.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jPanel1.add(name_stat, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 270, 290, 40));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/client_project/images/login2.gif"))); // NOI18N
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 190, 90, 100));

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setFont(new java.awt.Font("Leelawadee UI Semilight", 1, 11)); // NOI18N
        jButton2.setText("Back");
        jButton2.setBorder(null);
        jButton2.setBorderPainted(false);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 540, 80, 30));

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 430, 30, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 590));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /*
    The "Login" button checks if the userName fullfils the required conditions.
    */
    private void Login_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Login_buttonActionPerformed

        try
        {
            if(userName.getText().trim().equals("") )
                name_stat.setText("Please enter a name");
            
            else if(userName.getText().trim().length()>15)
                name_stat.setText("Name must have less than 15 character");
                           
            else
            {
                un = userName.getText().trim();
                IP = userIP.getText().trim();
                
                if(IP.equals(""))
                    IP = "127.0.0.1";
                
                /*
                The server created a registry in in the 7001 port.
                so a new Registry class obejct was created with the server's IP and 
                the server's registry was fount with the getRegistry() method
                */
                Registry re = LocateRegistry.getRegistry(IP, 7001);
                
                /*
                A new RMI obejct created and using the lookup() method. The server's 
                "Remote Method" class obejct was bind to a Registry class object with the 
                string "taja". So, when the lookup() function takes "taja" as parameter it will
                locate and connect with that specific "Remote Method" class obejct of the server
                and can access any information in that obejct let that be variables or function.              
                */
                RMI rmi = (RMI) re.lookup("taja");
                     
                /*
                The checkName() method takes a string as parameter and returns false
                if that string(name of the user) already exists in the server and true if it does not.
                If it is true then the static connect() method in the "Methods" class is called 
                to set up a connection with the server through socket
                */
                if (rmi.checkName(un)) 
                {                    
                    Methods.connect(un,IP);
                
                    Chat_screen.main(null);
                    
                    this.dispose();                    
                } 
                
                else
                   name_stat.setText("User name already exists");          
            }
        }

        catch (Exception e) 
        {          
            System.out.println(e);
        }

    }//GEN-LAST:event_Login_buttonActionPerformed

    private void userNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_userNameActionPerformed

    private void userIPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userIPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_userIPActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        this.dispose();
        Home.main(null);
    }//GEN-LAST:event_jButton2ActionPerformed


    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Login_button;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel name_stat;
    private javax.swing.JTextField userIP;
    private javax.swing.JTextField userName;
    // End of variables declaration//GEN-END:variables
}
