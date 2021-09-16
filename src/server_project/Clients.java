/*
    This class extends Thread. So whenever an object of this class is called
    it creates a new thread. A Clients class object is created whenever a 
    socket type object from the client_project connect to a server-socket type
    object in the server_project. Each client is handled in a different thread
    and all the threads are running simultamiously. So one client can not 
    interfare with the data, work and code of other clients.
*/
package server_project;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import messages.Messages;

public class Clients extends Thread
{
    /*
    name        - holds the name for each client as a string
    socket      - the socket which connect to the server socket of the server
    out         - object output stream for the client
    in          - object input stream for th client
    thread run  - a boolean variable that determines for how long the thread 
                  will run if it becomes "false", the while loop will break and 
                  the thread will end.
    */
    private String name;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean thread_run =true;
        
    /*
    The contructor takes a socket type object. The client is connected to the 
    server through this socket. This obejct is then saved the obejct "socket"
    declared in this class.
    */          
    public Clients(Socket socket) 
    {
        this.socket = socket;        
    }
    
    /*
    The overriden run function for the thread creates an objct output stream and an object
    input stream. The server has an arraylist that holds the list of Clients 
    currently connected to it via socket. The run function adds the new client
    to that list. Then it takes input from the input stream and proccesses 
    the data aaccording to it's type.
    */
    @Override
    public void run()
    {
        try
        {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            
            Methods.addClients(this);
               
            while(thread_run)
            {
                Messages ms = (Messages) in.readObject();

                String status = ms.getStatus();
                /*
                This condition checks if the client is new to the server through the status. If a client is new then it has to 
                get the list of other clients that are connected to the server. Also other clients need to receive the information about 
                this client connecting to the server and acknowledge it's presence.
                */
                if (status.equals("new"))
                {
                    name = ms.getName();
                    Mpage.server_status.append(ms.getName()+" has joined the server.\n");
                        
                    /*
                    This loop sends the information about this client to all the clients in the client list excluding this client.
                    The server holds the list of all the clients connected.
                    */
                    for(Clients client : Methods.getClients())
                    {
                        if(client != this)
                        {
                            ms = new Messages();
                            ms.setName(name);
                            ms.setStatus("new");
                            client.getOut().writeObject(ms);
                            client.getOut().flush();
                        }
                    }
                    /*
                    This loop sends the information of all the clients of the client list to the current client.
                    The current/new client doen's receive it's own information as a condition was placed to prohibit 
                    it from doing so.
                    */
                    for(Clients client : Methods.getClients())
                    {      
                        if(client != this)
                        {
                            ms = new Messages();
                            ms.setName(client.getUserName());
                            ms.setStatus("new");
                            out.writeObject(ms);
                            out.flush();
                        }                                                    
                    }     
                }
                
                /*
                This condition checks if the status is indicating that a messege was received. If the server receives a messege
                from one of the clients with this status then it forwards that messege to the other clients that are in the receiver list sent 
                with the messges by the sender client.
                */
                if(status.equals("message"))
                {                       
                    for(Clients client : Methods.getClients())
                    {
                        if(client != this)
                        {               
                            if(ms.getReceiverList().contains(client.getUserName()))
                            {
                                client.getOut().writeObject(ms);
                                client.getOut().flush();
                            }                               
                        }
                    }                       
                }
                /*
                This condition checks if the status is indicationg that a file was received.If the server receives a messege
                from one of the clients with this status then it forwards that messege to the other clients that are in the receiver list sent 
                with the messges by the sender client.
                */
                if(status.equals("file"))
                {
                    for(Clients client : Methods.getClients())
                    {
                        if(client != this)
                        {   
                            if(ms.getReceiverList().contains(client.getUserName()))
                            {
                                client.getOut().writeObject(ms);
                                client.getOut().flush();
                            }                               
                        }
                    }
                }
                /*
                This condition checks if the status is indicationg that a client is signing out.If the server receives a messege
                from one of the clients with this status then it forwards that messege to all the other clients telling them that 
                a client is signing out along with that clients name.
                */   
                if(status.equals("signOut"))
                {         
                    for(Clients client : Methods.getClients())
                    {
                        if(client != this)
                        {                              
                            client.getOut().writeObject(ms);
                            client.getOut().flush();
                        }
                    }
                        
                    Mpage.server_status.append(ms.getName()+" has left the server.\n");
                    Methods.removeClients(this);
                    thread_run=false;
                }                   
            }
        }
        
        /*
        This exception deals with the client using the "cross"  sign on the top right to close it's window.
        This will throw a socket connection exception that will be dealt by sending all other client the name 
        of the client that closed the chat window.
        */
        catch(Exception e)
        {
            Messages ms=new Messages();
            ms.setName(getUserName());
            ms.setStatus("signOut");
                      
            for(Clients client : Methods.getClients())
                    {
                        if(client != this)
                        {                              
                            try 
                            {
                                client.getOut().writeObject(ms);
                                client.getOut().flush();
                            } 
                            catch (Exception ex) 
                            {
                                System.out.println(ex);
                            }
                        }
                    }               
            Mpage.server_status.append(ms.getName()+" has left the server.\n");
            Methods.removeClients(this);
            thread_run = false;
        }
    }
            
    /*
    getter for object output stream and user name of the current client
    */   
    public ObjectOutputStream getOut()
    {
        return out;
    }
        
    public String getUserName()
    {
        return name;
    }
        
}
