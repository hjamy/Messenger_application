/*
RMI stands for Remote Method Invocation.
This class/interface allows to remoetely access and invoke methods in 
an object running in another JVM .
In this project , the clients will access an object of the RemoteMethod Class
which is running in the server.
The RMI interface is later impleamented in the RemoteMethod Class

As multiple inheritence in not supported by JAVA as it creates contradiction,
so multi level inheritance was applied through RMI class and RemoteMethod class
*/
package Rmi_pack;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMI extends Remote{   
    public boolean checkName(String name) throws RemoteException;
    
}
