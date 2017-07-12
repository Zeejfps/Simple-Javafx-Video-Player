package videoplayer.views;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import videoplayer.App;
import videoplayer.model.Video;
import videoplayer.utils.Utils;

import java.io.File;
import java.net.URI;

/**
 * Created by zeejfps on 7/6/2017.
 */
public class VideoImagePane extends StackPane {

    private final ImageView imageView;
    private final BorderPane layoutPane;

    private Video video;

    public VideoImagePane(Video video) {
        imageView = new ImageView();
        layoutPane = buildLayout();
        setVideo(video);
        getChildren().addAll(imageView, layoutPane);
    }

    public void setVideo(Video video) {
        this.video = video;
        imageView.setImage(new Image(video.image, true));
    }

    public Video getVideo() {
        return video;
    }

    @Override
    protected void layoutChildren() {
        layoutPane.resize(getWidth(), getHeight());
        imageView.setFitWidth(getWidth());
        imageView.setFitHeight(getHeight());
    }

    private BorderPane buildLayout() {
        Label playButton = new Label("\uF144");
        playButton.setId("playButton");
        playButton.setScaleX(0.75f);
        playButton.setScaleY(0.75f);
        return new BorderPane(playButton);
    }
}
