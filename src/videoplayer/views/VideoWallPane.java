package videoplayer.views;

import javafx.animation.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import videoplayer.App;
import videoplayer.controllers.VideoPane;
import videoplayer.model.Video;

/**
 * Created by zeejfps on 7/7/2017.
 */
public class VideoWallPane extends StackPane {

    public static final Duration OPEN_DURATION = Duration.millis(175);

    private final Timeline animator;
    private VideoPane videoPane;
    public final VideoGridPane gridPane;

    public VideoWallPane(Video[] videos) {
        gridPane = new VideoGridPane(5, 5, videos);
        videoPane = new VideoPane();

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
        videoPane.setVisible(false);
        setupVideoPanes();
        getChildren().addAll(gridPane, videoPane);
        animator.play();
    }

    private void setupVideoPanes() {
        VideoImagePane[][] videoImagePanes = gridPane.getVideoPanes();
        for (int i = 0; i < videoImagePanes.length; i++) {
            for(int j = 0; j < videoImagePanes[i].length; j++) {
                VideoImagePane vip = videoImagePanes[i][j];
                vip.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

                    videoPane.setVisible(true);
                    videoPane.playVideo(vip.getVideo());
                    gridPane.setVisible(false);

                    /*
                    animator.stop();

                    VideoPane vp = new VideoPane(vip.getVideo());
                    //vp.setMinSize(vip.getWidth(), vip.getHeight());
                   // vp.setPrefSize(vip.getWidth(), vip.getHeight());
                    vp.relocate(vip.getLayoutX(),vip.getLayoutY());
                    getChildren().add(vp);

                    double sx = getWidth() / vip.getWidth();
                    double sy = getHeight() / vip.getHeight();

                    Timeline timeline = new Timeline();
                    timeline.getKeyFrames().add(new KeyFrame(OPEN_DURATION,
                        new KeyValue(vp.layoutXProperty(), (getWidth()-vip.getWidth())*0.5, Interpolator.EASE_IN),
                        new KeyValue(vp.layoutYProperty(), (getHeight()-vip.getHeight())*0.5, Interpolator.EASE_IN)
                    ));

                    ScaleTransition st = new ScaleTransition(OPEN_DURATION, vp);
                    st.setFromY(0.1f);
                    st.setFromX(0.1f);
                    st.setToX(sx);
                    st.setToY(sy);

                    ParallelTransition pt = new ParallelTransition(timeline, st);
                    pt.setOnFinished(event1 -> {
                        //vp.setMinSize(getWidth(), getHeight());
                        //vp.setPrefSize(getWidth(), getHeight());
                        //System.out.println(getWidth() + ", " + getHeight());
                        vp.setScaleX(1f);
                        vp.setScaleY(1f);
                        //vp.relocate(0, 0);
                        //vp.requestLayout();

                        vp.relocate(-(getWidth()-vip.getWidth())*0.5, -(getHeight()-vip.getHeight())*0.5);
                        vp.setMinSize(getWidth(), getHeight());
                        vp.setPrefSize(getWidth(), getHeight());
                        vp.setMaxSize(getWidth(), getHeight());
                        //requestLayout();
                    });
                    pt.play();*/
                });
            }
        }
    }

}
