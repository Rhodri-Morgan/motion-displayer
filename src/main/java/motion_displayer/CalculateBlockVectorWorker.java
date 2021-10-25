package motion_displayer;

import java.util.concurrent.locks.ReentrantLock;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;


public class CalculateBlockVectorWorker implements Runnable {

    private final double text_size_ratio = 0.02;
    private VideoFile video;
    private ReentrantLock lock_modified_frame;
    private FrameMatchingStrategy frame_matching_strategy;
    private Mat modified_frame;
    private Mat frame;
    private Mat previous_frame;
    private int x;
    private int y;

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
        int center_block_modifier = (int) ((double) (this.video.getSearchSize() - this.video.getBlockSize()) / 2);
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
        Point start = new Point(this.x+center_block_modifier, this.y+center_block_modifier);
        Point end = new Point(matching_x, matching_y);
        this.lock_modified_frame.lock();
        if (video.isDebug()) {
            Imgproc.rectangle(this.modified_frame,
                              new Rect(this.x, this.y, this.video.getSearchSize(), this.video.getSearchSize()),
                              new Scalar(255, 255, 255),
                              1);
            Imgproc.putText(this.modified_frame,
                            Math.round(matching_metric) + "",
                            new Point(this.x, this.y+center_block_modifier),
                            Imgproc.FONT_HERSHEY_PLAIN,
                            text_size_ratio*video.getSearchSize(),
                            new Scalar(255, 255, 255));
        }
        else if (matching_metric > video.getThreshold()) {
            Imgproc.arrowedLine(this.modified_frame,
                                start,
                                end,
                                new Scalar(255, 255, 255),
                                1,
                                Imgproc.LINE_8,
                                0,
                                0.5);
        }
        this.lock_modified_frame.unlock();
    }
}
