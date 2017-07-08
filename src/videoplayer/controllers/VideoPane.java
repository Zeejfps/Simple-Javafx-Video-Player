package videoplayer.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import videoplayer.model.Video;

import java.io.IOException;

/**
 * Created by zeejfps on 7/7/2017.
 */
public class VideoPane extends StackPane {

    @FXML
    private MediaView mediaView;

    @FXML
    private Slider mediaSlider;

    private MediaPlayer mediaPlayer;
    private Media media;


    public VideoPane() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load(getClass().getClassLoader().getResourceAsStream("videoplayer/views/VideoPane.fxml"));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        mediaSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (media == null) return;
            double dx = Math.abs(newValue.doubleValue() - oldValue.doubleValue());
            if (mediaSlider.isValueChanging() || dx > 0.5) {
                mediaPlayer.seek(Duration.seconds(newValue.doubleValue() * media.getDuration().toSeconds() / 100));
            }
        });
    }

    public void playVideo(Video video) {
        media = new Media(video.video);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.currentTimeProperty().addListener(observable -> {
            Duration currentTime = mediaPlayer.getCurrentTime();
            if (!mediaSlider.isValueChanging()) {
                mediaSlider.setValue(currentTime.divide(media.getDuration().toMillis()).toMillis() * 100.0);
            }
        });
        mediaPlayer.setAutoPlay(true);
        mediaView.setMediaPlayer(mediaPlayer);
    }

    @Override
    protected void layoutChildren() {
        mediaView.setFitWidth(getWidth());
        mediaView.setFitHeight(getHeight());
        super.layoutChildren();
    }
}
