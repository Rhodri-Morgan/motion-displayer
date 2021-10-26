package motion_displayer;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.opencv.core.Core;


public class App {

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME );

        Path in_path = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "f1.mp4");
        Path out_path = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "f1_out.mp4");
        System.out.printf("PROCESSING VIDEO PATH - %s%n", in_path.toString());
        VideoFile video = new VideoFile(in_path, out_path, 40, 20, 0, false);
        VideoProcessorHandler video_processor = new VideoProcessorHandler(video);
        video_processor.process();
        System.out.printf("COMPLETED VIDEO PATH - %s%n", out_path.toString());
    }
}
