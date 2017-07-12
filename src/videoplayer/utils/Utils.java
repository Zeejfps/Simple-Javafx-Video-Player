package videoplayer.utils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by zeejfps on 7/12/2017.
 */
public class Utils {

    private Utils() {}

    public static void genThumb(String videoFilePath, String imageFilePath) throws IOException, InterruptedException, URISyntaxException {

        File videoFile = new File(new URI(videoFilePath));
        File imageFile = new File(new URI(imageFilePath));

        if (imageFile.exists()) return;

        System.out.println("Generating thumbnail...");

        // Build our ffmpeg command
        StringBuilder cmdBuilder = new StringBuilder();
        cmdBuilder.append("cmd /c ffmpeg.exe");

        // Set yes to always override
        cmdBuilder.append(" -y");

        // Our video
        cmdBuilder.append(" -i \"").append(videoFile.getPath()).append("\"");

        // The time in the video
        cmdBuilder.append(" -ss 00:00:10");

        // Command to get the thumbnail and destination
        cmdBuilder.append(" -vframes 1 \"").append(imageFile.getPath()).append("\"");

        String cmd = cmdBuilder.toString();
        Process p = Runtime.getRuntime().exec(cmd);
        p.waitFor();
    }

}
