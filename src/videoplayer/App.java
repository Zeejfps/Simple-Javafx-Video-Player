package videoplayer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import videoplayer.model.Video;
import videoplayer.views.VideoGridPane;
import videoplayer.views.VideoWallPane;

import java.io.File;
import java.util.Random;

public class App extends Application {

    public static final Random R;
    public static final Font FONT_AWESOME;
    static {
        R = new Random(System.nanoTime());
        FONT_AWESOME = Font.loadFont(App.class.getClassLoader()
                .getResourceAsStream("fontawesome-webfont.ttf"), 100);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Video Directory");
        File chosenDir = directoryChooser.showDialog(primaryStage);

        // If the chosen dir is null we are done
        if (chosenDir == null) {
            System.exit(0);
        }

        // Disable the escape hint
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        // Add event listener for escape key
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                primaryStage.close();
            }
        });

        VideoWallPane videoWallPane = new VideoWallPane(loadVideos(chosenDir));

        VideoGridPane gridPane = videoWallPane.gridPane;
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case LEFT:
                    gridPane.translateRowLeft();
                    break;
                case RIGHT:
                    gridPane.translateRowRight();
                    break;
                case UP:
                    gridPane.translateColUp();
                    break;
                case DOWN:
                    gridPane.translateColDown();
                    break;
            }
        });

        primaryStage.setScene(new Scene(videoWallPane));
        primaryStage.setTitle("Video Player");
        primaryStage.setMaximized(true);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }


    private Video[] loadVideos(File vidDir) {
        // Get our video files
        File[] videoFiles = vidDir.listFiles((dir, name) -> name.endsWith(".mp4"));
        if (videoFiles == null) {
            return new Video[0];
        }
        // Get the video and thumbnail paths and store them in a Video object
        Video[] videos = new Video[videoFiles.length];
        for (int i = 0; i < videos.length; i++) {
            String videoPath = videoFiles[i].toURI().toString();
            String imagePath = videoPath.replace(".mp4", ".png");
            videos[i] = new Video(videoPath, imagePath);
        }

        return videos;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
