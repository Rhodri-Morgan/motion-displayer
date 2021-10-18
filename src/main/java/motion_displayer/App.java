package motion_displayer;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.opencv.core.Core;


public class App {

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME );
        Path path = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "test.mp4");
        VideoFile video = new VideoFile(path, 40, 40, 20, 20);      // These are dummy value however these will be given by user
        VideoProcessorHandler video_processor = new VideoProcessorHandler(video);
        video_processor.process();
    }
}
