package src.AdvConnect;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class UsersforHost {
    private Socket clientSocket;
    private String username;
    private DataInputStream input;
    private DataOutputStream output;
    UsersforHost(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            this.input = new DataInputStream(clientSocket.getInputStream());
            this.output = new DataOutputStream(clientSocket.getOutputStream());
            this.username = input.readUTF();
        } catch (Exception e) {
            System.err.println("Error initializing UsersforHost: " + e.getMessage());
        }
    }
    public String getUsername() {
        return username;
    }
    public DataInputStream getInput() {
        return input;
    }
    public DataOutputStream getOutput() {
        return output;
    }
    public Socket getClientSocket() {
        return clientSocket;
    }
}
