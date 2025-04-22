package src.concept;
import javafx.application.Application;
import javafx.stage.Stage;
import java.util.Scanner;

public class ConsoleVer extends Application {
    public MusicPlayer mp;
    @Override
    public void start(Stage primaryStage) {
        Core core = new Core();
        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (true) {
                System.out.print(">> ");
                String cmd = sc.nextLine().trim().toLowerCase();
                mp = new MusicPlayer(core.fullTrack.nextTrack());
                switch (cmd) {
                    case "load" -> {
                        System.out.print("Music name >> ");
                        String name = sc.nextLine().trim().toLowerCase();
                        Music music = core.fullTrack.findMusicByName(name);
                        mp = new MusicPlayer(music);
                    }
                    case "next" -> {
                        mp.terminate();
                        mp = new MusicPlayer(core.fullTrack.nextTrack());
                    }
                    case "list" -> {
                        System.out.println(core.fullTrack.getMusicList());
                    }
                    case "play" -> {
                        if(mp == null) {
                            System.out.println("Music not loaded");
                        }else mp.play();
                    }
                    case "pause" -> mp.pause();
                    case "exit" -> {
                        System.exit(0);
                        return;
                    }
                    default -> System.out.println("Unknown command");
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
