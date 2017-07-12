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

    private  Timeline animator;
    private VideoPlayerPane playerPane;
    public  VideoGridPane gridPane;

    public VideoWallPane(Video[] videos) {
        gridPane = new VideoGridPane();
        gridPane.showVideos(5, 5, videos);
        playerPane = new VideoPlayerPane();
        playerPane.setVisible(false);
        playerPane.setCallback(() -> {
            gridPane.setVisible(true);
            FadeTransition ft = new FadeTransition(OPEN_DURATION, playerPane);
            ft.setToValue(0f);
            ft.setOnFinished(event -> {
                playerPane.setVisible(false);
            });
            ft.play();
            animator.play();
        });

        animator = new Timeline(new KeyFrame(Duration.seconds(2.5), event -> {
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
        getChildren().addAll(gridPane, playerPane);
        animator.play();
    }

    private void setupVideoPanes() {
        VideoImagePane[][] videoImagePanes = gridPane.getVideoPanes();
        for (int i = 0; i < videoImagePanes.length; i++) {
            for(int j = 0; j < videoImagePanes[i].length; j++) {
                VideoImagePane vip = videoImagePanes[i][j];
                vip.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

                    vip.setVisible(false);

                    animator.stop();
                    playerPane.setOpacity(1.0);
                    playerPane.setVisible(true);
                    playerPane.playVideo(vip.getVideo());

                    KeyFrame startFrame = new KeyFrame(Duration.ZERO,
                            new KeyValue(playerPane.layoutXProperty(), vip.getLayoutX(), Interpolator.LINEAR),
                            new KeyValue(playerPane.layoutYProperty(), vip.getLayoutY(), Interpolator.LINEAR),
                            new KeyValue(playerPane.minWidthProperty(), vip.getWidth(), Interpolator.LINEAR),
                            new KeyValue(playerPane.minHeightProperty(), vip.getHeight(), Interpolator.LINEAR),
                            new KeyValue(playerPane.maxWidthProperty(), vip.getWidth(), Interpolator.LINEAR),
                            new KeyValue(playerPane.maxHeightProperty(), vip.getHeight(), Interpolator.LINEAR)
                    );

                    KeyFrame endFrame = new KeyFrame(OPEN_DURATION,
                            new KeyValue(playerPane.layoutXProperty(), 0, Interpolator.EASE_OUT),
                            new KeyValue(playerPane.layoutYProperty(), 0, Interpolator.EASE_OUT),
                            new KeyValue(playerPane.minWidthProperty(), getWidth(), Interpolator.EASE_OUT),
                            new KeyValue(playerPane.minHeightProperty(), getHeight(), Interpolator.EASE_OUT),
                            new KeyValue(playerPane.maxWidthProperty(), getWidth(), Interpolator.EASE_OUT),
                            new KeyValue(playerPane.maxHeightProperty(), getHeight(), Interpolator.EASE_OUT)
                    );
                    Timeline timeline = new Timeline(startFrame, endFrame);
                    timeline.setOnFinished(event1 -> {
                        gridPane.setVisible(false);
                        vip.setVisible(true);
                    });
                    timeline.playFromStart();
                });
            }
        }
    }

}
