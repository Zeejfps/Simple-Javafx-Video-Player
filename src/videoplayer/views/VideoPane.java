package videoplayer.views;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import videoplayer.model.Video;

/**
 * Created by zeejfps on 7/7/2017.
 */
public class VideoPane extends StackPane {

    private final MediaView mediaView;
    private final Video video;
    private MediaPlayer mediaPlayer;

    public VideoPane(Video video) {
        this.video = video;
        mediaView = new MediaView(new MediaPlayer(new Media(video.video)));
        setStyle("-fx-background-color: black;");
        getChildren().addAll(mediaView);
    }

    private class LoadVideoTask extends Task<MediaPlayer> {
        @Override
        protected MediaPlayer call() throws Exception {
            System.out.println("Wat");
            Media media = new Media(video.video);
            System.out.println("After");
            return new MediaPlayer(media);
        }
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        mediaView.setFitWidth(getWidth());
        mediaView.setFitHeight(getHeight());
    }
}
