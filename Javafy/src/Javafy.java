package src;

import src.UI.GUI;
import javax.swing.*;
import javafx.application.Platform;
import java.awt.*;
import java.io.File;
import java.nio.file.*;
import java.util.ArrayList;


public class Javafy {
    private GUI gui;
    public static void main(String[] args) {
        Platform.startup(() -> {});
        SwingUtilities.invokeLater(() -> {
            Javafy javafy = new Javafy();
            javafy.gui = new GUI();
            GUI.frame.setVisible(true);
            GUI.frame.setMinimumSize(new Dimension(1000,700));
            new Thread(() -> {
                try {
                    WatchService watchService = FileSystems.getDefault().newWatchService();
                    Path path = Paths.get("music");
                    path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                            StandardWatchEventKinds.ENTRY_DELETE,
                            StandardWatchEventKinds.ENTRY_MODIFY);
                    while (true) {
                        WatchKey key = watchService.take();
                        for (WatchEvent<?> event : key.pollEvents()) {
                            //Path changed = (Path) event.context();
                            if (javafy.gui.isInternalChange()) {
                                System.out.println("Internal change detected, ignoring...");
                                continue;
                            }
                            javax.swing.SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(null, "Change detected, please manually restart the application", "Change Detected", JOptionPane.INFORMATION_MESSAGE);
                                shutDown();
                            });
                            return;
                        }
                        key.reset();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }
    private static void shutDown(){
        try {
            GUI.frame.dispose();
            GUI.frame.setVisible(false);
            GUI.frame = null;
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void restartApplication() {
        try {
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
