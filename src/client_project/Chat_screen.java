/*
This class is for the client's main chat. After a successful login user will 
enter the chat screen. The "Chat_screen" class handles the communication with the server.
It sends server objects of "Messeges" class containing necessary information
and also receives objects of "Messeges" class from server.
*/

package client_project;

import static client_project.Methods.out;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import messages.Messages;


public class Chat_screen extends javax.swing.JFrame {
   
    private Thread client_thread;
    private boolean thread_running = true;
    private File selected_file;
    

    
    /*
    The constructor is used to initialize some values to desired values.
    */
    public Chat_screen() {
        initComponents();
      
        showText.setText("");
        showText.setEditable(false);
        file_text_field.setEditable(false);
        file_send_button.setEnabled(false);
       
        heading.setText("User Name : "+Methods.myName); 
        
        /*
        The GripLayout() method is used to slice the "active_list" panel into desired pieces.
        In this case the parameters passed are 0,1,6,6.They respectively represent the the number of rows,
        the number of ccolumns,space betweeen rows and space between columns. Number of rows are set to 0 (zero).
        It means there can be any number of rows.
        Without making the grid layout the JRadioButtons cannot be added to the active list in the 
        run time of the project
        */
        
        active_list.setLayout(new GridLayout(0, 1, 6, 6));
        
        
        open();
    }
          
    private void open()
    {            
        /*
        A new thread is created inside the open() method to allow the "chat_screen" to wait and take inputs 
        from the server in a different thread as it will create contradictions with the output stream.
        */
        try
        {
            client_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (thread_running) {
                        try {
                            Messages ms = (Messages) Methods.in.readObject();
                            String status = ms.getStatus();
                            
                            /*
                            An object of "Messeges" class is taken input from the input stream and checked for the status (object variable).
                            Conditions are placed according to various values of the status. Currently there are 5 different statuses.
                            */
                            
                            
                            
                            /*
                            the "new" string as status means that a new client has entered the server and 
                            all pther clients are being informed about it.                            
                            */
                            if(status.equals("new"))
                            {                              
                                newFriend(ms.getName());
                            }
                            
                            /*
                            the "message" string as status means that a client has sent a message to the server and 
                            the server is forwarding it to this clients chat_screen.                            
                            */
                            
                            if(status.equals("message"))
                            {
                                receive_message(ms.getName(),ms.getMessage());
                            }
                            
                            /*
                            the "file" string as status means that a client has sent a file to the server and 
                            the server is forwarding it to this clients chat_screen.                            
                            */
                            
                            if(status.equals("file"))
                            {
                                receive_file(ms);
                            }
                            
                            /*
                            the "signOut" string as status means that a client is signing out and the this client's chat_screen should
                            remove that client from it's friend list
                            */
                            
                            
                            if(status.equals("signOut"))
                            {
                                friendLeft(ms.getName());
                            }
                     
                                                   
                        } catch (Exception ex) {
                            
                        }                   
               
                        
                    }   
                }
            });
            client_thread.start();
              
        }
        
        catch(Exception e)
        {
            System.out.println(e);
        }
      
    }
    /*
    The friendLeft() method is called when the Chat_scrren receives a "Messeges" class
    object from the server containing "signOut" string. It takes the name(string) of the 
    signed out client as a parameter and invokes the static remove_checkButton() method
    located in thee Methods class
    */
    private void friendLeft(String friendName)
    {

        Methods.remove_checkButton(friendName);        
    }
    
    /*
    The newFriend() method is called when the Chat_scrren receives a "Messeges" class
    object from the server containing "new" string. It takes the name(string) of the 
    new client as a parameter and invokes the static add_checkButton() method
    located in thee Methods class
    */
    
    
    private void newFriend(String friendName)
    {

        Methods.add_checkButton(friendName);        
    }
    
    /*
    The receive_message() method is called when the Chat_scrren receives a "Messeges" class
    object from the server containing "message" string. It takes the name(string) of the 
    sender client and the message (string) as a parameter and appends them in a specific form 
    in the JTextArea object "showText"
    */
    
    private void receive_message(String friendName,String message)
    {
        showText.append(friendName + " : " + message + "\n");
    }
    
    /*
    The receive_file method is called when the Chat_scrren receives a "Messeges" class
    object from the server containing "file" string. It takes the whole "Messages" object 
    as a parameter.It prompts the user if it wants to sav the file. When confirmed it 
    opens a file chooser window to let the user choose the destination for the new file .
    If the file already exists in that directory or the file name matches with anotherfile's name
    then the user is promt if it wants to replace the previously residing file.When confirmed 
    a new file will be created with that name and the data will be saved in that file.
    */
    
    private void receive_file(Messages ms) 
    {    
        int temp_option_pane = JOptionPane.showConfirmDialog(this,"\""+ ms.getName()+ "\" sent [ "+ms.getFileName()+" ]\nDo you want to save?", "File Received", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (temp_option_pane == JOptionPane.YES_OPTION)
        {     
            try 
            {                                       
                JFileChooser chooser_window = new JFileChooser();
              
                chooser_window.setSelectedFile(new File(ms.getFileName()));
                             
                int val = chooser_window.showSaveDialog(this);
                        
                if (val == JFileChooser.APPROVE_OPTION) 
                {             
                    File file = chooser_window.getSelectedFile();
             
                    if (file.exists()) 
                    {
                        int click = JOptionPane.showConfirmDialog(this, "A file with this name already exists\nDo you want to replace it?", "File Already Exists", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (click != JOptionPane.YES_OPTION) 
                        {
                            showText.append("Me : File rejected from \""+ms.getName()+"\" [" + ms.getFileName() + "]\n");
                            return;
                        }
                    }
           
                    String pathWay = file.getAbsolutePath();
                             
                    System.out.println(pathWay);
                          
                    File saveFile = new File(pathWay); 
                    FileOutputStream out_file = new FileOutputStream(saveFile);
                    out_file.write(ms.getData());
                    out_file.close();
                    showText.append("Me : File received from \""+ms.getName()+"\" [" + ms.getFileName() + "]\n");
                } 
            }
            
            catch (Exception e) 
            {
                System.out.println(e);          
            }
        }
        
        else 
        {
            showText.append("Me : File rejected from \""+ms.getName()+"\" [" + ms.getFileName() + "]\n");
        }
    }
 
    /*
    the signOut() method is called by a client when the "Sign Out" button is pressed on the chat_scrren.
    This method sends a "Messages" class object to the server containg the status "signOut". The server then 
    forwards this to all other clients to let them know that one of the clients have left     
    */
    public void sign_out() throws Exception
    {
        int c = JOptionPane.showConfirmDialog(this, "Are you sure you want to sign out", "Signing out", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (c == JOptionPane.YES_OPTION) {
            
            Messages ms = new Messages();
            ms.setStatus("signOut");
            ms.setName(Methods.myName); 
            Methods.out.writeObject(ms);
            Methods.out.flush();
            
            try 
            {
                Methods.socket.close();
            } 
            catch (IOException ex) 
            {
                System.out.println(ex);
            }
            
            thread_running = false;
            this.dispose();
            Login.main(null);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jRadioButton1 = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jRadioButton2 = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        jSpinner1 = new javax.swing.JSpinner();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        showText = new javax.swing.JTextArea();
        inputText = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        heading = new javax.swing.JLabel();
        messege_sender = new javax.swing.JButton();
        sign_out = new javax.swing.JButton();
        file_choose_button = new javax.swing.JButton();
        active_list_label = new javax.swing.JLabel();
        file_text_field = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        file_send_button = new javax.swing.JButton();
        all_select_button = new javax.swing.JButton();
        all_deselect_button = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        active_list = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable1);

        jRadioButton1.setText("jRadioButton1");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jRadioButton2.setText("jRadioButton2");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 41, 63));

        showText.setColumns(20);
        showText.setRows(5);
        jScrollPane1.setViewportView(showText);

        jLabel1.setFont(new java.awt.Font("Malgun Gothic", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Messeges");

        jLabel2.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Type your Messege :");

        heading.setFont(new java.awt.Font("Malgun Gothic", 1, 14)); // NOI18N
        heading.setForeground(new java.awt.Color(255, 255, 255));
        heading.setText("User name");

        messege_sender.setBackground(new java.awt.Color(255, 255, 255));
        messege_sender.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        messege_sender.setIcon(new javax.swing.ImageIcon(getClass().getResource("/client_project/images/red paper-plane.png"))); // NOI18N
        messege_sender.setText("Send");
        messege_sender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                messege_senderActionPerformed(evt);
            }
        });

        sign_out.setBackground(new java.awt.Color(255, 255, 255));
        sign_out.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        sign_out.setIcon(new javax.swing.ImageIcon(getClass().getResource("/client_project/images/red sign-out-option.png"))); // NOI18N
        sign_out.setText("Sign Out");
        sign_out.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sign_outActionPerformed(evt);
            }
        });

        file_choose_button.setBackground(new java.awt.Color(255, 255, 255));
        file_choose_button.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        file_choose_button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/client_project/images/red paper-clip.png"))); // NOI18N
        file_choose_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                file_choose_buttonActionPerformed(evt);
            }
        });

        active_list_label.setFont(new java.awt.Font("Malgun Gothic", 1, 14)); // NOI18N
        active_list_label.setForeground(new java.awt.Color(255, 255, 255));
        active_list_label.setText("Friends Active");

        jLabel3.setFont(new java.awt.Font("Malgun Gothic", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Browse and Select File to Send :");

        file_send_button.setBackground(new java.awt.Color(255, 255, 255));
        file_send_button.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        file_send_button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/client_project/images/file_text_document_page_paper_checkmark_complete_finished_done_checked_selected-512.png"))); // NOI18N
        file_send_button.setText("Send File");
        file_send_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                file_send_buttonActionPerformed(evt);
            }
        });

        all_select_button.setBackground(new java.awt.Color(255, 255, 255));
        all_select_button.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        all_select_button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/client_project/images/green-check-mark-double-hi-300x277.png"))); // NOI18N
        all_select_button.setText("Select All");
        all_select_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                all_select_buttonActionPerformed(evt);
            }
        });

        all_deselect_button.setBackground(new java.awt.Color(255, 255, 255));
        all_deselect_button.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        all_deselect_button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/client_project/images/not-met-red-500px.png"))); // NOI18N
        all_deselect_button.setText("Select None");
        all_deselect_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                all_deselect_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout active_listLayout = new javax.swing.GroupLayout(active_list);
        active_list.setLayout(active_listLayout);
        active_listLayout.setHorizontalGroup(
            active_listLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 266, Short.MAX_VALUE)
        );
        active_listLayout.setVerticalGroup(
            active_listLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 161, Short.MAX_VALUE)
        );

        jScrollPane5.setViewportView(active_list);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/client_project/images/red message.png"))); // NOI18N

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/client_project/images/red user.png"))); // NOI18N

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/client_project/images/red multiple-users-silhouette.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 519, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(205, 205, 205)
                                    .addComponent(sign_out))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(35, 35, 35)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel6)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(active_list_label, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(0, 0, Short.MAX_VALUE))
                                        .addComponent(jScrollPane5)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(all_select_button)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(all_deselect_button)))))
                            .addGap(16, 16, 16)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(file_text_field, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(file_choose_button, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(file_send_button))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(inputText, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(messege_sender))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(heading, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(heading, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(active_list_label, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)))
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputText, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(messege_sender, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(all_select_button, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(all_deselect_button, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(file_text_field, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(file_choose_button, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(file_send_button, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sign_out, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /*
    This is the "Select None" button. It de-seleccts all the radio button that is inside the active list panel
    */
    private void all_deselect_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_all_deselect_buttonActionPerformed
        
        for(JRadioButton temp_button : Methods.check_buttons)
        {
            temp_button.setSelected(false);
        }
    }//GEN-LAST:event_all_deselect_buttonActionPerformed

    /*
    This is the "Select All" button. It selects all the radio button in the active list panel 
    */
    private void all_select_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_all_select_buttonActionPerformed
        
        for(JRadioButton temp_button : Methods.check_buttons)
        {
            temp_button.setSelected(true);
        }
    }//GEN-LAST:event_all_select_buttonActionPerformed

    /*
    The "Send File" button is for sending the files. After the file has beeen choosed by the user this button will be enabled.
    Pressing it will send the selected file to the selected clients n the active list.This is done by calling the 
    send_file(0 method located in the Methods class.
    */
    private void file_send_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_file_send_buttonActionPerformed

        showText.append("Me : File sent [" + selected_file.getName() + "]\n");
        Methods.send_file(selected_file);

        file_send_button.setEnabled(false);
        file_text_field.setText("");
    }//GEN-LAST:event_file_send_buttonActionPerformed

    /*
    this is the "..." button. This button opens JFileChooser window that allows the user/client to choose the desired 
    file to send to other clients. When the file is choosen, the "Send File" button becomes activates.
    */
    private void file_choose_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_file_choose_buttonActionPerformed

        JFileChooser chooser_window = new JFileChooser();

        int val = chooser_window.showOpenDialog(this);
        if (val == JFileChooser.APPROVE_OPTION) {

            file_send_button.setEnabled(true);
            selected_file = chooser_window.getSelectedFile();

            file_text_field.setText(selected_file.getAbsolutePath());
        }
    }//GEN-LAST:event_file_choose_buttonActionPerformed

    /*
    The "sign Out" button calles the sign_out() method when pressed.
    */
    private void sign_outActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sign_outActionPerformed
        try 
        {
            sign_out();
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }
    }//GEN-LAST:event_sign_outActionPerformed

    /*
    The "Send" button checks if the textField has any text then sends the text to the selected clients in the
    client-lits/activer-user-list.
    */
    private void messege_senderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_messege_senderActionPerformed

        if(!inputText.getText().trim().equals(""))
        {
            try
            {
                String tempMessage;
                tempMessage = inputText.getText().trim();
                Methods.send_message(tempMessage);
                inputText.setText("");

                showText.append("Me : " + tempMessage + "\n");
            }
            catch (IOException ex)
            {
                System.out.println(ex);
            }
        }
    }//GEN-LAST:event_messege_senderActionPerformed

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
            java.util.logging.Logger.getLogger(Chat_screen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Chat_screen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Chat_screen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Chat_screen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Chat_screen().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JPanel active_list;
    private javax.swing.JLabel active_list_label;
    private javax.swing.JButton all_deselect_button;
    private javax.swing.JButton all_select_button;
    private javax.swing.JButton file_choose_button;
    private javax.swing.JButton file_send_button;
    private javax.swing.JTextField file_text_field;
    private javax.swing.JLabel heading;
    private javax.swing.JTextField inputText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton messege_sender;
    private javax.swing.JTextArea showText;
    private javax.swing.JButton sign_out;
    // End of variables declaration//GEN-END:variables
}
