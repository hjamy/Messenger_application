/*
RemoteMethod classss impleaments the RMI class and defines the abstract 
method that is checkName().This class extends UnicastRemoteObject, using this it 
sends a remote object to JRMP(JAVA Remote Method Protocol) which is later 
accessed by other clients through RMI
*/

package Rmi_pack;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import server_project.Clients;
import server_project.Methods;

public class RemoteMethod extends UnicastRemoteObject implements RMI{
    
    public RemoteMethod() throws RemoteException {
    }

    /*
    the checkName() method takes a name (String) as parameter and matches the 
    name to all other clients name. If a match is found then it returns false and 
    if not , it returns true.
    */
    @Override
    public boolean checkName(String userName) throws RemoteException {
        for (Clients client : Methods.getClients()) 
        {
            String name = client.getUserName();

            if (name.equalsIgnoreCase(userName))  
                return false;     
        }       
        return true;
    }  
}
