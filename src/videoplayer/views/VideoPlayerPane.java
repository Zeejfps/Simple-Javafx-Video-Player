package videoplayer.views;

import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import videoplayer.model.Video;

/**
 * Created by zeejfps on 7/7/2017.
 */
public class VideoPlayerPane extends AnchorPane {

    interface Callback {
        void onClose();
    }

    private MediaView mediaView;
    private Slider mediaSlider;
    private Button closeButton;
    private MediaPlayer mediaPlayer;
    private Media media;
    private Callback callback;

    public VideoPlayerPane() {
        createView();
    }

    protected void createView() {

        setStyle("-fx-background-color: black;");

        mediaView = new MediaView();
        mediaView.setPreserveRatio(false);
        closeButton = new Button("X");
        closeButton.setId("closeButton");
        mediaSlider = new Slider(0, 100, 0);
        mediaSlider.setId("mediaSlider");

        AnchorPane.setTopAnchor(closeButton, 10.0);
        AnchorPane.setRightAnchor(closeButton, 10.0);
        closeButton.setText("\uF00D");
        closeButton.setOnAction(event -> {
            if (mediaPlayer != null)
                mediaPlayer.stop();
            if (callback != null) {
                callback.onClose();
            }
        });

        mediaSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (media == null) return;
            double dx = Math.abs(newValue.doubleValue() - oldValue.doubleValue());
            if (mediaSlider.isValueChanging() || dx > 0.5) {
                mediaPlayer.seek(Duration.seconds(newValue.doubleValue() * media.getDuration().toSeconds() / 100));
            }
        });

        Pane mediaViewContainer = new Pane(mediaView);
        mediaView.fitWidthProperty().bind(mediaViewContainer.widthProperty());
        mediaView.fitHeightProperty().bind(mediaViewContainer.heightProperty());

        BorderPane layoutPane = new BorderPane();
        layoutPane.setCenter(mediaViewContainer);
        layoutPane.setBottom(mediaSlider);

        AnchorPane.setLeftAnchor(layoutPane, 0.0);
        AnchorPane.setTopAnchor(layoutPane, 0.0);
        AnchorPane.setRightAnchor(layoutPane, 0.0);
        AnchorPane.setBottomAnchor(layoutPane, 0.0);

        getChildren().addAll(layoutPane, closeButton);
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

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
