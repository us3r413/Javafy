package src.connect;
import java.util.Scanner;
public class TCPClientTest {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the IP address of the host: ");
        String ipAddress = input.nextLine();
        System.out.print("Enter your name: ");
        String name = input.nextLine();
        Client client = new Client(ipAddress, name);
        client.sendMessage(name + " has joined the chat");
        try {
            Thread read = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    client.getMessage();
                    if(client.isclosed()){
                        break;
                    }
                }
            });
            read.start();
            while (true) {
                if(client.isclosed()){
                    break;
                }
                String message = input.nextLine();
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
                client.sendMessage(client.getUsername() + ": " + message);
            }
            client.sendMessage(client.getUsername()+" has left the chat");
            read.interrupt();
            read.join();
            client.terminateConnection();
        } catch (InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
