package motion_displayer;


import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.util.LinkedList;

public class BlockSegmenterLink implements VideoProcessorLink {

    private VideoProcessorLink next;

    @Override
    public void handle(VideoFile video) {
        for (Frame frame : video.getUnmodifiedFrames()) {
            LinkedList<Mat> blocks = new LinkedList<>();
            int current_x = 0;
            int current_y = 0;
            while (true) {
                if (current_x >= video.getFrameWidth() || current_y >= video.getFrameHeight()) {
                    frame.setBlocks(blocks);
                    break;
                } else {
                    Rect roi = new Rect(current_x, current_y, video.getBlockWidth()-1, video.getBlockHeight()-1);
                    Mat block = new Mat(frame.getFrame(), roi);
                    Mat temp_block = new Mat();
                    block.copyTo(temp_block);
                    blocks.addLast(temp_block);
                    current_x += video.getBlockWidth();
                    current_y += video.getBlockHeight();
                }
            }
        }
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
