package src;

import src.UI.GUI;
import javax.swing.*;
import javafx.application.Platform;

import java.awt.*;



public class Javafy {
    private GUI gui;
    public static void main(String[] args) {
        Platform.startup(() -> {});
        SwingUtilities.invokeLater(() -> {
            Javafy javafy = new Javafy();
            javafy.gui = new GUI();
            GUI.frame.setVisible(true);
            GUI.frame.setMinimumSize(new Dimension(1000,700));
        });
    }
}
