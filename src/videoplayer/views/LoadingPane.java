package videoplayer.views;

import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;

/**
 * Created by zeejfps on 7/12/2017.
 */
public class LoadingPane extends BorderPane {

    public final ProgressBar progressBar;

    public LoadingPane() {
        setStyle("-fx-background-color: black");
        progressBar = new ProgressBar();
        setCenter(progressBar);
    }

}
