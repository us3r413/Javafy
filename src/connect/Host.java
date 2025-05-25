package src.connect;
import java.io.*;
import java.net.*;
public class Host {
    private static final int PORT = 12345;
    private String Username = "Host";
    private ServerSocket TCPHost;
    private DataInputStream input = null;
    private DataOutputStream output = null;
    Host(String Name){
        try {
            this.Username = Name;
            this.TCPHost = new ServerSocket(PORT);
            System.out.println("Server started. Waiting for client...");
            System.out.println("IP Address: " + getIPAddress() + '\n' + "Send this to the client.");
            Socket socket = TCPHost.accept();
            System.out.println("Client connected");
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            output.writeUTF(Username); // Send the host's username to the client
            output.writeInt(0);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public String getUsername() {
        return Username;
    }
    public String getIPAddress() {
        InetAddress local = null;
        try {
            local = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return local != null ? local.getHostAddress() : "Unknown Host";
    }
    public void getMessage(){//run this on a thread
        try {
            System.out.println(input.readUTF());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
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
            TCPHost.close();
            input = null;
            output = null;
            System.out.println("Server closed.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public boolean isclosed(){
        return TCPHost.isClosed();
    }
    public boolean outputNull(){
        return output == null;
    }
}
