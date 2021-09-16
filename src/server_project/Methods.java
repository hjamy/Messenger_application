/*
This class holds the Methods that are required throught out the server's 
process. In this case the arraylist that holds the list of all the clients connected 
to the server through sockets is declared here along with some function to access it.
The variable has a private access modifier and the functions are of public access modifier.
*/
package server_project;

import java.util.ArrayList;

public class Methods 
{
    
    private static ArrayList<Clients> clientSet = new ArrayList<Clients>();
    
    /*
    Getter function that returns the whole arraylist of clients 
    */
    public static ArrayList<Clients> getClients()   
    {
        return clientSet;
    }

    /*
    Function that takes a Clients type obejct as parameter and then adds that object to the arraylist that
    holds the list of all connected clients
    */
    public static void addClients(Clients aClients) 
    {
        clientSet.add(aClients);
    }
    
    /*
    Function that takes the Client type object that indicates to the Client that signed out or closed chat screen
    and removes that client from the list of clients which is an arraylist
    */
    
    public static void removeClients(Clients aClients) 
    {
        clientSet.remove(aClients);
    }
    
}
