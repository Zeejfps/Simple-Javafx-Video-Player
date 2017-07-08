package videoplayer.views;

import javafx.animation.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import videoplayer.App;
import videoplayer.model.Video;

/**
 * Created by zeejfps on 7/7/2017.
 */
public class VideoWallPane extends Pane {

    public static final Duration OPEN_DURATION = Duration.millis(175);

    private final Timeline animator;
    public final VideoGridPane gridPane;

    public VideoWallPane(Video[] videos) {
        gridPane = new VideoGridPane(5, 5, videos);
        animator = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            int dir = App.R.nextInt(4);
            switch (dir) {
                case 0:
                    gridPane.translateRowLeft();
                    break;
                case 1:
                    gridPane.translateRowRight();
                    break;
                case 2:
                    gridPane.translateColDown();
                    break;
                case 3:
                    gridPane.translateColUp();
                    break;
            }
        }));
        animator.setCycleCount(Animation.INDEFINITE);
        setupVideoPanes();
        getChildren().add(gridPane);
        animator.play();
    }

    private void setupVideoPanes() {
        VideoImagePane[][] videoImagePanes = gridPane.getVideoPanes();
        for (int i = 0; i < videoImagePanes.length; i++) {
            for(int j = 0; j < videoImagePanes[i].length; j++) {
                VideoImagePane vip = videoImagePanes[i][j];
                int lol = i;
                vip.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

                    animator.stop();

                    VideoPane vp = new VideoPane(vip.getVideo());
                    vp.setMinSize(vip.getWidth(), vip.getHeight());
                    vp.setMaxSize(vip.getWidth(), vip.getHeight());
                    vp.relocate(vip.getLayoutX(),vip.getLayoutY());
                    getChildren().add(vp);

                    double sx = getWidth() / vip.getWidth();
                    double sy = getHeight() / vip.getHeight();

                    Timeline timeline = new Timeline();
                    KeyValue xVal = new KeyValue(vp.layoutXProperty(), (getWidth()-vip.getWidth())*0.5, Interpolator.EASE_IN);
                    KeyValue yVal = new KeyValue(vp.layoutYProperty(), (getHeight()-vip.getHeight())*0.5, Interpolator.EASE_IN);
                    timeline.getKeyFrames().add(new KeyFrame(OPEN_DURATION, xVal, yVal));

                    ScaleTransition st = new ScaleTransition(OPEN_DURATION, vp);
                    st.setToX(sx);
                    st.setToY(sy);

                    ParallelTransition pt = new ParallelTransition(timeline, st);
                    pt.play();
                });
            }
        }
    }

}
