package videoplayer;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import videoplayer.model.Video;
import videoplayer.utils.Utils;
import videoplayer.views.LoadingPane;
import videoplayer.views.VideoWallPane;

import java.io.File;
import java.net.URI;
import java.util.Random;

public class App extends Application {

    public static final Random R;
    static {
        R = new Random(System.nanoTime());
        Font.loadFont(App.class.getClassLoader()
                .getResourceAsStream("fontawesome-webfont.ttf"), 12);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
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

        Video[] videos = loadVideos(chosenDir);
        LoadingPane loadingPane = new LoadingPane();
        Scene scene = new Scene(loadingPane);
        scene.getStylesheets().add("styles.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Video Player");
        primaryStage.setMaximized(true);
        primaryStage.setFullScreen(true);
        primaryStage.show();

        GenThumbnailsTask task = new GenThumbnailsTask(videos);
        task.setOnSucceeded(event -> scene.setRoot(new VideoWallPane(videos)));
        Thread t = new Thread(task);
        t.setDaemon(false);
        t.start();
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

    private static class GenThumbnailsTask extends Task<Void> {

        private final Video[] videos;

        public GenThumbnailsTask(Video[] videos) {
            this.videos = videos;
        }

        @Override
        protected Void call() throws Exception {
            for (Video video : videos) {
                Utils.genThumb(video.video, video.image);
            }
            System.out.println("Thumbnails generated!");
            return null;
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
