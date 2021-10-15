package motion_displayer;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.LinkedList;

public class VideoFileProcessor {

    private FrameMatchingStrategy frameMatchingStrategy;

    public VideoFileProcessor(FrameMatchingStrategy frameMatchingStrategy) {
        this.frameMatchingStrategy = frameMatchingStrategy;
    }

    public LinkedList<Mat> processVideo(LinkedList<Mat> frames) {
        LinkedList<Mat> motion_frames = new LinkedList<Mat>();
        Mat previous_frame = null;
        for (Mat frame : frames) {
            if (previous_frame == null) {
                previous_frame = frame;
            } else {
                System.out.println("Test");
                System.exit(0);
                // Calculate and draw arrow
            }
            motion_frames.addFirst(frame);
        }
        return motion_frames;
    }


}
