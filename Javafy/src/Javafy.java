package src;

import src.UI.GUI;
import javax.swing.*;
import javafx.application.Platform;

public class Javafy {
    private GUI gui;
    public static void main(String[] args) {
        Platform.startup(() -> {});
        SwingUtilities.invokeLater(() -> {
            Javafy javafy = new Javafy();
            javafy.gui = new GUI();
            javafy.gui.setVisible(true);
        });
    }
}
