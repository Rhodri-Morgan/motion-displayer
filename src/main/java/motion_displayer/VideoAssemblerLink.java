package motion_displayer;

import java.util.LinkedList;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoWriter;


public class VideoAssemblerLink implements VideoProcessorLink {

    private VideoProcessorLink next;

    @Override
    public void handle(VideoFile video) {
        System.out.println("--- Building output video ---");
        LinkedList<Mat> frames = video.getModifiedFrames();
        VideoWriter writer = new VideoWriter(video.getOutPath(),
                                             VideoWriter.fourcc('D','I','V','X'),
                                             video.getFps(),
                                             frames.get(0).size());
        int processed_frames = 0;
        for (Mat frame : video.getModifiedFrames()) {
            writer.write(frame);
            processed_frames += 1;
            System.out.printf("\rWritten modified frame %d/%d", processed_frames, video.getModifiedFrames().size());
        }
        writer.release();
        System.out.print("\n");
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
