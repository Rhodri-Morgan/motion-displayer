package motion_displayer.model;

import java.util.concurrent.locks.ReentrantLock;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;


public class CalculateBlockVectorWorker implements Runnable {

    private final ReentrantLock lock_modified_frame;
    private final FrameMatchingStrategy frame_matching_strategy;
    private final Mat modified_frame;
    private final Mat frame;
    private final Mat previous_frame;
    private final int x;
    private final int y;
    private final int search_size;
    private final int block_size;
    private final Scalar arrow_colour;

    public CalculateBlockVectorWorker(ReentrantLock lock_modified_frame,
                                      FrameMatchingStrategy frame_matching_strategy,
                                      Mat modified_frame,
                                      Mat frame,
                                      Mat previous_frame,
                                      int x,
                                      int y,
                                      int search_size,
                                      int block_size,
                                      Scalar arrow_colour) {
        this.lock_modified_frame = lock_modified_frame;
        this.frame_matching_strategy = frame_matching_strategy;
        this.modified_frame = modified_frame;
        this.frame = frame;
        this.previous_frame = previous_frame;
        this.x = x;
        this.y = y;
        this.search_size = search_size;
        this.block_size = block_size;
        this.arrow_colour = arrow_colour;
    }

    @Override
    public void run() {
        Rect search_area_roi = new Rect(this.x, this.y, this.search_size, this.search_size);
        int center_block_modifier = (int) Math.floor((double) (this.search_size - this.block_size) / 2);
        Mat search_area;
        Mat previous_search_area;
        Mat center_block;
        try {
            search_area = new Mat(this.frame, search_area_roi);
            previous_search_area = new Mat(this.previous_frame, search_area_roi);
            center_block = new Mat(search_area, new Rect(center_block_modifier, center_block_modifier, this.block_size, this.block_size));
        } catch (CvException e) {
            return;
        }
        Double matching_metric = null;
        int matching_x = 0;
        int matching_y = 0;
        for (int search_y=0; search_y<this.search_size-this.block_size; search_y++) {
            for (int search_x=0; search_x<this.search_size-this.block_size; search_x++) {
                try {
                    Rect previous_search_roi = new Rect(search_x, search_y, this.block_size, this.block_size);
                    Mat previous_search_block = new Mat(previous_search_area, previous_search_roi);
                    Double metric = this.frame_matching_strategy.match(center_block,
                                                                       this.x+center_block_modifier,
                                                                       this.y+center_block_modifier,
                                                                       previous_search_block,
                                                                       this.x+search_x,
                                                                       this.y+search_y);
                    if (matching_metric == null || metric < matching_metric) {
                        matching_metric = metric;
                        matching_x = this.x+search_x;
                        matching_y = this.y+search_y;
                    }
                } catch (CvException e) {
                    // Pass
                }
            }
        }
        Point start = new Point(matching_x, matching_y);
        Point end = new Point(this.x+center_block_modifier, this.y+center_block_modifier);
        this.lock_modified_frame.lock();
        if (matching_metric != null && matching_metric <= 50) {
            Imgproc.arrowedLine(this.modified_frame,
                                start,
                                end,
                                this.arrow_colour,
                                1,
                                Imgproc.LINE_8,
                                0,
                                0.5);
        }
        this.lock_modified_frame.unlock();
    }
}
