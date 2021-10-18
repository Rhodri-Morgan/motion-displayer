package motion_displayer;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.util.LinkedList;


public class CalculateMotionVectorLink implements VideoProcessorLink {

    private FrameMatchingStrategy frame_matching_strategy;
    private VideoProcessorLink next;

    public CalculateMotionVectorLink(FrameMatchingStrategy frame_matching_strategy) {
        this.frame_matching_strategy = frame_matching_strategy;
    }

    @Override
    public void handle(VideoFile video) {
        for (Mat frame : video.getUnmodifiedFrames()) {
            LinkedList<Mat> modified_frames = new LinkedList<>();
            int current_x = 0;
            int current_y = 0;
            while (true) {
                if (current_x >= video.getFrameWidth() || current_y >= video.getFrameHeight()) {
                    video.setModifiedFrames(modified_frames);
                    break;
                } else {
                    Rect block_roi = new Rect(current_x, current_y, video.getBlockWidth()-1, video.getBlockHeight()-1);
                    Mat block = new Mat(frame, block_roi);
                    Mat temp_block = new Mat();
                    block.copyTo(temp_block);
                    Rect search_area = new Rect(current_x, current_y, video.getBlockWidth()-1, video.getBlockHeight()-1);
                    /* TODO */
                    current_x += video.getBlockWidth();
                    current_y += video.getBlockHeight();
                }
            }
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
