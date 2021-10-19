package motion_displayer;

import java.util.Map;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.Rect;


public class CalculateBlockVectorWorker implements Runnable {

    private Integer block_id;
    private Map<Integer, Mat> modified_blocks;
    private FrameMatchingStrategy frame_matching_strategy;
    private Mat frame;
    private Mat next_frame;
    private int x;
    private int y;
    private int search_size;
    private int block_size;

    public CalculateBlockVectorWorker(Integer block_id,
                                      Map<Integer, Mat> modified_blocks,
                                      FrameMatchingStrategy frame_matching_strategy,
                                      Mat frame,
                                      Mat next_frame,
                                      int x,
                                      int y,
                                      int search_size,
                                      int block_size) {
        this.block_id = block_id;
        this.modified_blocks = modified_blocks;
        this.frame_matching_strategy = frame_matching_strategy;
        this.frame = frame;
        this.next_frame = next_frame;
        this.x = x;
        this.y = y;
        this.search_size = search_size;
        this.block_size = block_size;
    }

    @Override
    public void run() {
        Rect block_roi = new Rect(this.x, this.y, this.block_size, this.block_size);
        Mat block = new Mat(this.frame, block_roi);
        int shift_search = (int) ((double) this.search_size / 2);
        int center_search_x = this.x + shift_search;
        int center_search_y = this.y + shift_search;
        for (int i=0; i<this.search_size; i++) {
            for (int j=0; j<this.search_size; j++) {
                try {
                    int search_x = center_search_x - shift_search;
                    int search_y = center_search_y - shift_search;
                    Rect search_roi = new Rect(search_x, search_y, this.block_size, this.block_size);
                    Mat search_block = new Mat(this.next_frame, search_roi);
                    double metric = this.frame_matching_strategy.match(block, this.x, this.y, search_block, search_x, search_y);
                } catch (CvException e) {
                    // Pass
                }
                center_search_x += 1;
            }
            center_search_y += 1;
        }
        // this.modified_blocks.put(this.block_id, new Mat());      TODO Replace with actual modified block mat
    }
}
