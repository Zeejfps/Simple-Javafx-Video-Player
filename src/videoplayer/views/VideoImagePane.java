package videoplayer.views;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import videoplayer.App;
import videoplayer.model.Video;

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
        playButton.setStyle("-fx-text-fill: rgba(255, 255, 255, 0.30);");
        playButton.setScaleX(0.75f);
        playButton.setScaleY(0.75f);
        playButton.setFont(App.FONT_AWESOME);
        return new BorderPane(playButton);
    }

}
