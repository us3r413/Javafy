package src.connect;
import java.util.Scanner;
public class TCPHostTest {
    public static void main(String[] args) {
        System.out.println("Enter your name: ");
        Scanner input = new Scanner(System.in);
        Host host = new Host(input.nextLine());
        try {
            Thread read = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    host.getMessage();
                }
            });
            read.start();
            while (true) {
                String message = input.nextLine();
                if (message.equalsIgnoreCase("exit")) {
                    host.sendMessage("Close the connection (12500)");
                    break;
                }
                host.sendMessage(host.getUsername() + ": " + message);

            }
            read.interrupt();
            read.join();
            host.terminateConnection();
        } catch (InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
