package src.connect;
import java.io.*;
import java.net.*;
public class Client {
    private static final int PORT = 12345;
    private String Username = "Client";
    private Socket TCPClient;
    private DataInputStream input = null;
    private DataOutputStream output = null;
    Client(String IPAddress, String Name){
        try {
            this.Username = Name;
            this.TCPClient = new Socket("localhost", PORT);
            input = new DataInputStream(TCPClient.getInputStream());
            output = new DataOutputStream(TCPClient.getOutputStream());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public String getUsername() {
        return Username;
    }
    public void getMessage(){//run this on a thread
        try {
            String message = input.readUTF();
            if(message.equals("Close the connection (12500)")){
                terminateConnection();
            }
            System.out.println(message);

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            terminateConnection();
        }
    }
    public void sendMessage(String message) {
        try {
            output.writeUTF(message);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void terminateConnection(){
        try {
            output.writeUTF("Connection terminated.");
            TCPClient.close();
            System.out.println("Server closed.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public boolean isclosed(){
        return TCPClient.isClosed();
    }
}
