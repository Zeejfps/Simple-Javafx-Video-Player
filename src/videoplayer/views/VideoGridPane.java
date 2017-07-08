package videoplayer.views;

import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.util.Duration;
import videoplayer.App;
import videoplayer.model.Video;

/**
 * Created by zeejfps on 7/6/2017.
 */
public class VideoGridPane extends Pane {

    public static final Duration TRANSITION_DURATION = Duration.millis(500);

    private final Video[] videos;
    private final int numRows, numCols;
    private final int numRowsPadded, numColsPadded;
    private double cellWidth, cellHeight;

    private VideoImagePane[][] children;

    public VideoGridPane(int numRows, int numCols, Video[] videos) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.videos = videos;
        this.numRowsPadded = numRows + 2;
        this.numColsPadded = numCols + 2;

        children = new VideoImagePane[numColsPadded][numRowsPadded];

        calculateCellSize();
        addChildren();
    }

    public void addChild(VideoImagePane videoImagePane, int row, int col) {
        children[row][col] = videoImagePane;
        getChildren().add(videoImagePane);
    }

    public VideoImagePane getChild(int row, int col) {
        return children[col][row];
    }

    private void calculateCellSize() {
        // Get the screen bounds
        Rectangle2D bounds = Screen.getPrimary().getBounds();

        // Calculate the width and height of each video pane
        cellWidth = bounds.getWidth() / numCols;
        cellHeight = bounds.getHeight() / numRows;
    }

    // TODO: select a proper video index
    private void addChildren() {
        // Create a video pane for each row and col
        for (int i = 0; i < numRowsPadded; i++) {
            for (int j = 0; j < numColsPadded; j++) {
                VideoImagePane videoImagePane = new VideoImagePane(videos[App.R.nextInt(videos.length)]);
                videoImagePane.setMinSize(cellWidth, cellHeight);
                videoImagePane.setMaxSize(cellWidth, cellHeight);
                addChild(videoImagePane, j, i);
            }
        }
    }

    public void translateRowLeft() {
        int row = App.R.nextInt(numRows) + 1;
        ParallelTransition pt = new ParallelTransition();
        for (int i = 0; i < numColsPadded; i++) {
            VideoImagePane pane = getChild(row, i);
            TranslateTransition tt = new TranslateTransition(TRANSITION_DURATION, pane);
            tt.setToX(-cellWidth);
            pt.getChildren().add(tt);
        }
        pt.setOnFinished(event -> {
            VideoImagePane[] newRow = new VideoImagePane[numColsPadded];
            for (int j = 0; j < numColsPadded; j++) {
                int x = j - 1;
                if (x < 0) x = numColsPadded-1;
                VideoImagePane child = children[j][row];
                child.setTranslateX(0);
                newRow[x] = child;
            }
            for (int j = 0; j < numColsPadded; j++) {
                if (j == 0 || j == numRowsPadded-1) {
                    newRow[j].setVideo(videos[App.R.nextInt(videos.length)]);
                }
                children[j][row] = newRow[j];
            }
            requestLayout();
        });
        pt.play();
    }

    public void translateRowRight() {
        int row = App.R.nextInt(numRows) + 1;
        ParallelTransition pt = new ParallelTransition();
        for (int i = 0; i < numColsPadded; i++) {
            VideoImagePane pane = getChild(row, i);
            TranslateTransition tt = new TranslateTransition(TRANSITION_DURATION, pane);
            tt.setToX(cellWidth);
            pt.getChildren().add(tt);
        }
        pt.setOnFinished(event -> {
            VideoImagePane[] newRow = new VideoImagePane[numColsPadded];
            for (int j = 0; j < numColsPadded; j++) {
                int x = j + 1;
                if (x >= numColsPadded) x = 0;
                VideoImagePane child = children[j][row];
                child.setTranslateX(0);
                newRow[x] = child;
            }
            for (int j = 0; j < numColsPadded; j++) {
                if (j == 0 || j == numRowsPadded-1) {
                    newRow[j].setVideo(videos[App.R.nextInt(videos.length)]);
                }
                children[j][row] = newRow[j];
            }
            requestLayout();
        });
        pt.play();
    }

    public void translateColDown() {
        int col = App.R.nextInt(numCols) + 1;
        ParallelTransition pt = new ParallelTransition();
        for (int i = 0; i < numRowsPadded; i++) {
            VideoImagePane pane = getChild(i, col);
            TranslateTransition tt = new TranslateTransition(TRANSITION_DURATION, pane);
            tt.setToY(cellHeight);
            pt.getChildren().add(tt);
        }
        pt.setOnFinished(event -> {
            VideoImagePane[] newCol = new VideoImagePane[numRowsPadded];
            for (int j = 0; j < numRowsPadded; j++) {
                int y = j + 1;
                if (y >= numRowsPadded) y = 0;
                VideoImagePane child = children[col][j];
                child.setTranslateY(0);
                newCol[y] = child;
            }
            for (int j = 0; j < numRowsPadded; j++) {
                if (j == 0 || j == numRowsPadded-1) {
                    newCol[j].setVideo(videos[App.R.nextInt(videos.length)]);
                }
                children[col][j] = newCol[j];
            }
            requestLayout();
        });
        pt.play();
    }

    public void translateColUp() {
        int col = App.R.nextInt(numCols) + 1;
        ParallelTransition pt = new ParallelTransition();
        for (int i = 0; i < numRowsPadded; i++) {
            VideoImagePane pane = getChild(i, col);
            TranslateTransition tt = new TranslateTransition(TRANSITION_DURATION, pane);
            tt.setToY(-cellHeight);
            pt.getChildren().add(tt);
        }
        pt.setOnFinished(event -> {
            VideoImagePane[] newCol = new VideoImagePane[numRowsPadded];
            for (int j = 0; j < numRowsPadded; j++) {
                int y = j - 1;
                if (y < 0) y = numRowsPadded-1;
                VideoImagePane child = children[col][j];
                child.setTranslateY(0);
                newCol[y] = child;
            }
            for (int j = 0; j < numRowsPadded; j++) {
                if (j == 0 || j == numRowsPadded-1) {
                    newCol[j].setVideo(videos[App.R.nextInt(videos.length)]);
                }
                children[col][j] = newCol[j];
            }
            requestLayout();
        });
        pt.play();
    }

    @Override
    protected void layoutChildren() {
        int y = -1;
        for (int i = 0; i < numRowsPadded; i++, y++) {
            int x = -1;
            for (int j = 0; j < numColsPadded; j++, x++) {
                VideoImagePane child = children[j][i];
                child.resizeRelocate(x*cellWidth, y*cellHeight, cellWidth, cellHeight);
            }
        }
    }

    public VideoImagePane[][] getVideoPanes() {
        return children;
    }

}
