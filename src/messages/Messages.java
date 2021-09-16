/*
The "Messages" class works as a template for the message that is going to 
be sent from the client to the server and later from the server to the clients.
It is required to be converted into a JAR file and added to both the client_project and the 
server_project or it will create a problem with the class paths for the receiving end.

The required private instances are created along with their getter and setter methods to 
access them.This methods are public.

The class is made to impleament "Serializable" as obejcts sent through 
ObjectOutputStream and recieved by ObjectInputStream neeeds to to serialized.
*/

package messages;

import java.io.Serializable;
import java.util.ArrayList;

public class Messages  implements Serializable{

    private String status;
    private String name;
    private String message;
    private byte data[];
    private String fileName;
    private ArrayList<String> receiver_list;
    
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }


    public void setFileName(String newFileName)
    {
        fileName = newFileName;
    }
    
    public String getFileName()
    {
        return fileName;
    }
    
    public void setReceiverList(ArrayList<String> list)
    {
        receiver_list = list;
    }
    
    public ArrayList<String> getReceiverList()
    {
        return receiver_list;
    }
}
