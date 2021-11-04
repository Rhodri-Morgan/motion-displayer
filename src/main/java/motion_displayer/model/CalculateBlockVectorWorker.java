package motion_displayer.model;

import java.util.concurrent.locks.ReentrantLock;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;


public class CalculateBlockVectorWorker implements Runnable {

    private final VideoFile video;
    private final ReentrantLock lock_modified_frame;
    private final FrameMatchingStrategy frame_matching_strategy;
    private final Mat modified_frame;
    private final Mat frame;
    private final Mat previous_frame;
    private final int x;
    private final int y;

    public CalculateBlockVectorWorker(VideoFile video,
                                      ReentrantLock lock_modified_frame,
                                      FrameMatchingStrategy frame_matching_strategy,
                                      Mat modified_frame,
                                      Mat frame,
                                      Mat previous_frame,
                                      int x,
                                      int y) {
        this.video = video;
        this.lock_modified_frame = lock_modified_frame;
        this.frame_matching_strategy = frame_matching_strategy;
        this.modified_frame = modified_frame;
        this.frame = frame;
        this.previous_frame = previous_frame;
        this.x = x;
        this.y = y;
    }

    @Override
    public void run() {
        Rect search_area_roi = new Rect(this.x, this.y, this.video.getSearchSize(), this.video.getSearchSize());
        Mat search_area = new Mat(this.frame, search_area_roi);
        Mat previous_search_area = new Mat(this.previous_frame, search_area_roi);
        int center_block_modifier = (int) Math.floor((double) (this.video.getSearchSize() - this.video.getBlockSize()) / 2);
        Mat center_block = new Mat(search_area, new Rect(center_block_modifier, center_block_modifier, this.video.getBlockSize(), this.video.getBlockSize()));
        Double matching_metric = null;
        int matching_x = 0;
        int matching_y = 0;
        for (int search_y=0; search_y<this.video.getSearchSize()-this.video.getBlockSize(); search_y++) {
            for (int search_x=0; search_x<this.video.getSearchSize()-this.video.getBlockSize(); search_x++) {
                try {
                    Rect previous_search_roi = new Rect(search_x, search_y, this.video.getBlockSize(), this.video.getBlockSize());
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
        if (matching_metric != null) {
            Imgproc.arrowedLine(this.modified_frame,
                                start,
                                end,
                                this.video.getArrowColour(),
                                1,
                                Imgproc.LINE_8,
                                0,
                                0.5);
        }
        this.lock_modified_frame.unlock();
    }
}
