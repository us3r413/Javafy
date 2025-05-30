package src.AdvConnect;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Host {
    private static final int PORT = 12345;
    private String Username = "Host";
    private String globalString = "hi";
    private ServerSocket TCPHost;
    private StringBuilder message = null;
    public ArrayList<UsersforHost> clients = null;
    private final Object messageLock = new Object();
    public Host(String Name){
        try {
            this.Username = Name;
            this.TCPHost = new ServerSocket(PORT);
            this.clients = new ArrayList<>();
            message = new StringBuilder();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error: Host Startup Failed -> Reason: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    public ArrayList<UsersforHost> getClients (){
        return clients;
    }
    public int getClientIndex(String name){
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getUsername().equals(name)) {
                return i;
            }
        }
        return -1;
    }
    public String getM() {
        synchronized (messageLock) {
            String opt = message.toString();
            message.setLength(0);
            if (opt.isEmpty()) {
                return null;
            }
            return opt;
        }
    }
    public int getClientCount() {
        return clients.size();
    }
    public void listenforClient(){
        try {
            Socket newClient = TCPHost.accept();
            UsersforHost user = new UsersforHost(newClient);
            user.getOutput().writeUTF(Username); // Send the host's username to the client
            user.getOutput().writeInt(clients.size());
            for (UsersforHost client : clients) {
                user.getOutput().writeUTF(client.getUsername());
                client.getOutput().writeUTF("new");
                client.getOutput().writeUTF(user.getUsername());
            }
            synchronized (messageLock) {
                message.append(user.getUsername() + " has joined the chat\n");
            }
            clients.add(user);
            Thread read = new Thread(() -> {
                try{
                    while (!Thread.currentThread().isInterrupted()) {
                        String clientMessage = getClientMessage(user.getOutput(),user.getInput());
                        synchronized (messageLock) {
                            if (clientMessage.equals("code1")) {
                                message.append(globalString + " received from " + user.getUsername() + "\n");
                            }else if (clientMessage.equals("code2")) {
                                message.append(user.getUsername() + " has transferred a file through you\n");
                            } else {
                                for(UsersforHost client : clients){
                                    if(client != user) {
                                        client.getOutput().writeUTF(clientMessage);
                                    }
                                }
                                message.append(clientMessage + "\n");
                            }
                        }
                    }
                }catch(Exception e){
                    try {
                        user.getOutput().close();
                        user.getInput().close();
                        user.getClientSocket().close();
                    }catch (Exception ex) {
                        System.err.println("Error closing client connection: " + ex.getMessage());
                    }
                    clients.remove(user);
                    for(UsersforHost client : clients){
                        try {
                            client.getOutput().writeUTF("left");
                            client.getOutput().writeUTF(user.getUsername());
                        }catch(Exception ex){
                            System.err.println("Error notifying other clients: " + ex.getMessage());
                        }
                    }
                    Thread.currentThread().interrupt();
                }
            });
            read.start();
        } catch (IOException e){
            System.err.println(e.getMessage());
        }
    }
    public String getUsername() {
        return Username;
    }
    public String getIPAddress() {
        try {
            ProcessBuilder builder = new ProcessBuilder("ipconfig");
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("IPv4") || line.contains("IPv4")) {
                    int colonIndex = line.indexOf(':');
                    if (colonIndex != -1 && colonIndex + 1 < line.length()) {
                        return line.substring(colonIndex + 1).trim();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return "None";
    }
    private String getClientMessage(DataOutputStream output,DataInputStream input){//run this on a thread
        try {
            String message = input.readUTF();
            if(message.equals("MP3")){
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
                globalString = filename;
                return "code1";
            }else if(message.equals("Transfer")){
                System.out.println("Transfer request received");
                String target = input.readUTF();
                String fileName = input.readUTF();
                long fileSize = input.readLong();
                int targetIndex = -1;
                int i = 0;
                for(UsersforHost client : clients){
                    if(client.getUsername().equals(target)){
                        targetIndex = i;
                        break;
                    }
                    i++;
                }
                File dir = new File("music");
                fileName = new File(fileName).getName();
                FileOutputStream fos = new FileOutputStream(new File(dir, fileName));
                byte[] buffer = new byte[4096];
                long bytesReceived = 0;
                while (bytesReceived < fileSize) {
                    int bytes = input.read(buffer, 0, (int) Math.min(buffer.length, fileSize - bytesReceived));
                    if (bytes == -1) break;
                    fos.write(buffer, 0, bytes);
                    bytesReceived += bytes;
                }
                fos.close();
                sendMP3(targetIndex, "music" + File.separator + fileName);
                return "code2";
            }else {
                return message;
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return null;
    }
    public void sendMessage( String message) {
        try {
            for (UsersforHost client : clients) {
                client.getOutput().writeUTF(Username + " : " + message);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void terminateConnection(){
        for(UsersforHost client : clients){
            try {
                client.getOutput().writeUTF("Connection terminated by host");
                client.getInput().close();
                client.getOutput().close();
                client.getClientSocket().close();
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        try {
            TCPHost.close();
            clients.clear();
            clients = null;
            System.out.println("Server closed.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void killClient(int index){
        try {
            clients.get(index).getOutput().writeUTF("You have been kicked by the host");
            clients.get(index).getInput().close();
            clients.get(index).getOutput().close();
            clients.get(index).getClientSocket().close();
            clients.remove(index);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public boolean isclosed(){
        return TCPHost.isClosed();
    }
    public void sendMP3(int index, String filePath) {
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            byte[] buffer = new byte[(int) file.length()];
            int bytesRead = 0;
            DataOutputStream output = clients.get(index).getOutput();
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