/*
The "Methods" class is made to hold methods that are used in other classes to
lessen the amoount of messiness in the code and to make a developer-friendly 
code to allow future improvement of the project.
The methods of this class are static. So they can be directly called via the name of the 
class there in that is the "Methods" class.
*/

package client_project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.*;
import messages.Messages;

public class Methods {
   
    public static ObjectOutputStream out;
    public static ObjectInputStream in;
    public static String myName;
    public static Socket socket;
    public static ArrayList<JRadioButton> check_buttons = new ArrayList<JRadioButton>();
   
    /*
    The add_checkButtons() method takes a strng as parameter which is the name of a client.
    Then usnig that name as the text for a JRadioButton it creates a new JRadioButton 
    obejct and adds it to the active list panel
    */
    public static void add_checkButton(String friendName)
    {     
        JRadioButton temp_button = new JRadioButton(friendName,false);
        temp_button.setVisible(true);
        
        check_buttons.add(temp_button);
        
        Chat_screen.active_list.add(temp_button);
        Chat_screen.active_list.revalidate();
        Chat_screen.active_list.repaint();
    }
    
    /*
    The remove_checkButton() method takes a strng as parameter which is the name of
    a signing out client.It searches the JradioButton list for that client's Radio Button
    and removes it. Then all the elements inside the active list JPanel is deleted and 
    re-inserted without the recently deleted cleint's JRadioButton
    
    */
    
    public static void remove_checkButton(String friendName)
    {        
        Chat_screen.active_list.removeAll();
        Chat_screen.active_list.revalidate();
        Chat_screen.active_list.repaint();
      
        for(JRadioButton temp_button : check_buttons)
        {
            if(temp_button.getText().equals(friendName))
            {
               check_buttons.remove(temp_button);
               break;
            }
        }
        
        for(JRadioButton temp_button : check_buttons)
        {
            Chat_screen.active_list.add(temp_button);
        }
        
        Chat_screen.active_list.revalidate();
        Chat_screen.active_list.repaint();       
    }
    
    /*
    The connect() method takes the name of the user and the IP adreess of the server(string)
    as parameters and sets up a connection with the serve through socket.
    It initializes an object output stream and an object input stream.
    */
    public static void connect(String userName,String IP) throws IOException
    {
        try
        {         
            socket = new Socket(IP,7000);
        
            out = new ObjectOutputStream(socket.getOutputStream());
            
            in = new ObjectInputStream(socket.getInputStream());
      
            Messages ms = new Messages();
         
            ms.setStatus("new");
          
            ms.setName(userName);
           
            out.writeObject(ms);
            out.flush();

            myName = userName;
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        
    }
    
    /*
    send_message() method takes the messege (string) to be sent as a parameter.
    Then it creats an objet of "Messages" class and sets up the sender name and the 
    message that is to be sent along with the receivers list.
    */
    public static void send_message(String message) throws IOException
    {
        
        Messages ms = new Messages();
        ms.setStatus("message");
        ms.setMessage(message);
        ms.setName(myName);
        ms.setReceiverList(make_reciever_list());
        
        
        out.writeObject(ms);
        out.flush();
        
    }
    
    /*
    send_file() method takes the file to be sent as a parameter.
    
    first the file to be sent is accessed via a fileInputStream and 
    all the files data is kept in a array of bytes.
    
    Then it creats an objet of "Messages" class and sets up the sender name and the 
    file(which is now an array of bytes) that is to be sent along with the receivers list.
    
    */
    
    public static void send_file(File file) 
    {
        
        try
        {
            FileInputStream inFile = new FileInputStream(file);
            byte temp_data[] = new byte[inFile.available()];
            inFile.read(temp_data);
            inFile.close();
            
            Messages ms = new Messages();
            ms.setStatus("file");
            ms.setData(temp_data);
            ms.setName(myName);
            ms.setReceiverList(make_reciever_list());
            ms.setFileName(file.getName());
           
            out.writeObject(ms);
            out.flush();
        }
        
        catch(Exception ex)
        {
            System.out.println(ex);
        }

    }
    
    /*
    the make_receiver_list() methocreates a receiver_list arrayist in the run time 
    and checks which JradioButton is turned on or selected.Only the seleced one's names are
    then added to the  selected receiver's list.This list is later returned.
    this list is used to determine which of the clients wil receive the sender's message.
    This checking and sending is done by the server.
    */
    public static ArrayList<String> make_reciever_list()
    {
        ArrayList<String> receiver_list = new ArrayList<String>();
        
        for(JRadioButton temp_button : check_buttons)
        {
            if(temp_button.isSelected())
            {
                receiver_list.add(temp_button.getText());
            }
        }
        return receiver_list;
    }  
}
