package motion_displayer;

import org.opencv.core.Core;
import java.nio.file.Path;
import java.nio.file.Paths;


public class App {

    private static int calculateBlockSize(int frame_width, int frame_height) {
        int min_width = 25;
        int min_height = 25;

        while (true) {
            double width_check = ((double) frame_width) / min_width;
            double height_check = ((double) frame_width) / min_height;
            if (width_check % 1 == 0 && height_check % 1 == 0) {
                return min_width;
            }
            else if (min > width_check || min > height_check) {
                return 0;
            }
            else {
                min++;
            }
        }
    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME );
        Path path = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "test.mp4");
        VideoFile video = new VideoFile(path);
        System.out.println(calculateBlockSize(video.getFrameWidth(), video.getFrameHeight()));
        VideoFileProcessor videoProcessor = new VideoFileProcessor(new SumSquareDifferenceStrategy());
        videoProcessor.processVideo(video.getFrames());
    }
}
