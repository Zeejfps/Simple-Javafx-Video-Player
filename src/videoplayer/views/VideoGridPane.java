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

    private Video[] videos;
    private int rows, columns;
    private int rowsPadded, columnsPadded;
    private double cellWidth, cellHeight;
    private VideoImagePane[][] children;

    public void showVideos(int rows, int cols, Video[] videos) {
        this.videos = videos;
        this.rows = rows;
        this.columns = cols;
        rowsPadded = rows+2;
        columnsPadded = cols+2;
        calculateCellSize();
        children = new VideoImagePane[rowsPadded][columnsPadded];
        for (int i = 0; i < rowsPadded; i++) {
            for (int j = 0; j < columnsPadded; j++) {
                Video video = videos[App.R.nextInt(videos.length)];
                addVideoImagePane(new VideoImagePane(video), i, j);
            }
        }
        requestLayout();
    }

    public void addVideoImagePane(VideoImagePane videoImagePane, int row, int col) {
        videoImagePane.setMinSize(cellWidth, cellHeight);
        videoImagePane.setMaxSize(cellWidth, cellHeight);
        children[row][col] = videoImagePane;
        getChildren().add(videoImagePane);
    }

    public VideoImagePane getChild(int row, int col) {
        return children[row][col];
    }

    private void calculateCellSize() {
        // Get the screen bounds
        Rectangle2D bounds = Screen.getPrimary().getBounds();

        // Calculate the width and height of each video pane
        cellWidth = bounds.getWidth() / columns;
        cellHeight = bounds.getHeight() / rows;
    }

    public void translateRowLeft() {
        int row = App.R.nextInt(rows) + 1;
        ParallelTransition pt = new ParallelTransition();
        for (int i = 0; i < columnsPadded; i++) {
            VideoImagePane pane = getChild(row, i);
            TranslateTransition tt = new TranslateTransition(TRANSITION_DURATION, pane);
            tt.setToX(-cellWidth);
            pt.getChildren().add(tt);
        }
        pt.setOnFinished(event -> {
            VideoImagePane[] newRow = new VideoImagePane[columnsPadded];
            for (int j = 0; j < columnsPadded; j++) {
                int x = j - 1;
                if (x < 0) x = columnsPadded-1;
                VideoImagePane child = getChild(row, j);
                child.setTranslateX(0);
                newRow[x] = child;
            }
            for (int j = 0; j < columnsPadded; j++) {
                if (j == 0 || j == columnsPadded-1) {
                    newRow[j].setVideo(videos[App.R.nextInt(videos.length)]);
                }
                children[row][j] = newRow[j];
            }
            requestLayout();
        });
        pt.play();
    }

    public void translateRowRight() {
        int row = App.R.nextInt(rows) + 1;
        ParallelTransition pt = new ParallelTransition();
        for (int i = 0; i < columnsPadded; i++) {
            VideoImagePane pane = getChild(row, i);
            TranslateTransition tt = new TranslateTransition(TRANSITION_DURATION, pane);
            tt.setToX(cellWidth);
            pt.getChildren().add(tt);
        }
        pt.setOnFinished(event -> {
            VideoImagePane[] newRow = new VideoImagePane[columnsPadded];
            for (int j = 0; j < columnsPadded; j++) {
                int x = j + 1;
                if (x >= columnsPadded) x = 0;
                VideoImagePane child = getChild(row, j);
                child.setTranslateX(0);
                newRow[x] = child;
            }
            for (int j = 0; j < columnsPadded; j++) {
                if (j == 0 || j == columnsPadded-1) {
                    newRow[j].setVideo(videos[App.R.nextInt(videos.length)]);
                }
                children[row][j] = newRow[j];
            }
            requestLayout();
        });
        pt.play();
    }

    public void translateColDown() {
        int col = App.R.nextInt(columns) + 1;
        ParallelTransition pt = new ParallelTransition();
        for (int i = 0; i < rowsPadded; i++) {
            VideoImagePane pane = getChild(i, col);
            TranslateTransition tt = new TranslateTransition(TRANSITION_DURATION, pane);
            tt.setToY(cellHeight);
            pt.getChildren().add(tt);
        }
        pt.setOnFinished(event -> {
            VideoImagePane[] newCol = new VideoImagePane[rowsPadded];
            for (int j = 0; j < rowsPadded; j++) {
                int y = j + 1;
                if (y >= rowsPadded) y = 0;
                newCol[y] = getChild(j, col);;
            }
            for (int j = 0; j < rowsPadded; j++) {
                if (j == 0 || j == rowsPadded-1) {
                    newCol[j].setVideo(videos[App.R.nextInt(videos.length)]);
                }
                newCol[j].setTranslateY(0.0);
                children[j][col] = newCol[j];
            }
            requestLayout();
        });
        pt.play();
    }

    public void translateColUp() {
        int col = App.R.nextInt(columns) + 1;
        ParallelTransition pt = new ParallelTransition();
        for (int i = 0; i < rowsPadded; i++) {
            VideoImagePane pane = getChild(i, col);
            TranslateTransition tt = new TranslateTransition(TRANSITION_DURATION, pane);
            tt.setToY(-cellHeight);
            pt.getChildren().add(tt);
        }
        pt.setOnFinished(event -> {
            VideoImagePane[] newCol = new VideoImagePane[rowsPadded];
            for (int j = 0; j < rowsPadded; j++) {
                int y = j - 1;
                if (y < 0) y = rowsPadded-1;
                newCol[y] = getChild(j, col);
            }
            for (int j = 0; j < rowsPadded; j++) {
                if (j == 0 || j == rowsPadded-1) {
                    newCol[j].setVideo(videos[App.R.nextInt(videos.length)]);
                }
                newCol[j].setTranslateY(0.0);
                children[j][col] = newCol[j];
            }
            requestLayout();
        });
        pt.play();
    }

    @Override
    protected void layoutChildren() {
        if (children == null) return;
        int y = -1;
        for (int i = 0; i < rowsPadded; i++, y++) {
            int x = -1;
            for (int j = 0; j < columnsPadded; j++, x++) {
                VideoImagePane child = children[i][j];
                child.resizeRelocate(x*cellWidth, y*cellHeight, cellWidth, cellHeight);
            }
        }
    }

    public VideoImagePane[][] getVideoPanes() {
        return children;
    }
}
