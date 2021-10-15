package motion_displayer;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.highgui.HighGui;

import java.nio.file.Path;
import java.nio.file.Paths;


public class App {

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME );
        Path path = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "test.mp4");
        VideoFile video = new VideoFile(path, 30, 27);      // These are dummy value however these will be given by user
    }
}
