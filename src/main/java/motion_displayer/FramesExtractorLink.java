package motion_displayer;

import java.util.LinkedList;
import org.opencv.core.Mat;


public class FramesExtractorLink implements VideoProcessorLink {

    private VideoProcessorLink next;

    @Override
    public void handle(VideoFile video) {
        System.out.println("--- Extracting Frames ---");
        LinkedList<Mat> frames = new LinkedList<>();
        Mat frame = new Mat();
        while (true) {
            if (video.getVideoCapture().read(frame)) {
                Mat temp_frame = new Mat();
                frame.copyTo(temp_frame);
                frames.addLast(temp_frame);
                System.out.printf("\rExtracted Frame Count %d/%d", frames.toArray().length, video.getFrameCount());
            } else {
                break;
            }
        }
        System.out.print("\n");
        video.setUnmodifiedFrames(frames);
        if (this.next != null) {
            this.next.handle(video);
        }
    }

    @Override
    public void addLink(VideoProcessorLink link) {
        if (this.next != null) {
            this.next.addLink(link);
        } else {
            this.next = link;
        }
    }
}
