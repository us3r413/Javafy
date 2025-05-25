package src.AdvConnect;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Client {
    private static final int PORT = 12345;
    private String Username = "Client";
    private Socket TCPClient;
    private DataInputStream input = null;
    private DataOutputStream output = null;
    private ArrayList<String> otherUsers;
    public Client(String IPAddress, String Name){
        try {
            this.otherUsers = new ArrayList<>();
            this.Username = Name;
            this.TCPClient = new Socket(IPAddress, PORT);
            input = new DataInputStream(TCPClient.getInputStream());
            output = new DataOutputStream(TCPClient.getOutputStream());
            output.writeUTF(Username);
            otherUsers.add(input.readUTF()); //host index is 0
            int clients = input.readInt(); //read the number of clients connected
            for(int i = 0; i < clients; i++) {
                String user = input.readUTF();
                otherUsers.add(user);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public String getUsername() {
        return Username;
    }
    public String getMessage(){//run this on a thread
        try {
            String message = input.readUTF();
            if(message.equals("MP3")) {
                String filename = input.readUTF();
                long size = input.readLong();
                File dir = new File("music");
                filename = new File(filename).getName();
                FileOutputStream fos = new FileOutputStream(new File(dir, filename));
                byte[] buffer = new byte[4096];
                long bytesReceived = 0;
                while (bytesReceived < size) {
                    int bytes = input.read(buffer, 0, (int) Math.min(buffer.length, size - bytesReceived));
                    if (bytes == -1) break;
                    fos.write(buffer, 0, bytes);
                    bytesReceived += bytes;
                }
                fos.close();
                return "MP3 received: " + filename;
            }else if(message.equals("new")){
                String client = input.readUTF();
                otherUsers.add(client);
                return client + " has joined the chat";
            }else {
                return message;
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }catch(Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("Connection reset")) {
                System.out.println("Server disconnected");
                try {
                    terminateConnection();
                } catch (Exception ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        }
        return null;
    }
    public void sendMessage(String message) {
        try {
            output.writeUTF(Username + " : " + message);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void terminateConnection(){
        try {
            if(output!= null) {
                output.writeUTF(Username + " has left the chat");
                output.close();
            }
            if(input != null) {
                input.close();
            }
            if(TCPClient!= null) {
                TCPClient.close();
            }
            System.out.println("Server closed.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public boolean isclosed(){
        return TCPClient.isClosed();
    }
    public void sendMP3(String filePath) {
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            byte[] buffer = new byte[(int) file.length()];
            int bytesRead = 0;
            output.writeUTF(Username+" has sent an MP3 file -> " + file.getName());
            output.writeUTF("MP3");
            output.writeUTF(file.getName());
            output.writeLong(file.length());
            while ((bytesRead = bis.read(buffer, 0, buffer.length)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            output.flush();
            bis.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}