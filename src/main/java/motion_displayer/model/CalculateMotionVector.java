package motion_displayer.model;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;


public class CalculateMotionVector {

    private final int max_threads = Runtime.getRuntime().availableProcessors() * 4;
    private final List<Thread> workers = new ArrayList<>();
    private final ReentrantLock lock_modified_frames = new ReentrantLock();
    private final int frame_width;
    private final int frame_height;
    private final int search_size;
    private final int block_size;
    private final Scalar arrow_colour;


    public CalculateMotionVector(int frame_width, int frame_height,  int search_size, int block_size, Scalar arrow_colour) {
        this.frame_width = frame_width;
        this.frame_height = frame_height;
        this.search_size = search_size;
        this.block_size = block_size;
        this.arrow_colour = arrow_colour;
    }

    /**
     * Waits for a thread to finish and clears it for use
     */
    private void clearFinishedThreads() {
        List<Thread> to_remove = new ArrayList<>();
        while (true) {
            for (Thread worker : this.workers) {
                if (!worker.isAlive()) {
                    to_remove.add(worker);
                }
            }
            if (to_remove.toArray().length != 0) {
                this.workers.removeAll(to_remove);
                break;
            }
        }
    }

    /**
     * Processes frame applying motion vectors to each of its search areas
     * @param frame frame to investigate
     * @param previous_frame frame to compare motion against current frame
     * @return modified copy of frame with motion vectors applied
     */
    public Mat processFrame(Mat frame, Mat previous_frame) {
        Mat modified_frame = new Mat();
        frame.copyTo(modified_frame);
        int x = 0;
        int y = 0;
        while (true) {
            if (y >= this.frame_height) {
                while (this.workers.size() != 0) {
                    clearFinishedThreads();
                }
                break;
            } else if (x >= this.frame_width) {
                x = 0;
                y += this.search_size;
            } else {
                if (this.workers.size() == this.max_threads) {
                    clearFinishedThreads();
                }
                Thread worker = new Thread(new CalculateBlockVectorWorker(this.lock_modified_frames,
                                                                          new MeanAbsoluteDifference(),
                                                                          modified_frame,
                                                                          frame,
                                                                          previous_frame,
                                                                          x,
                                                                          y,
                                                                          this.search_size,
                                                                          this.block_size,
                                                                          this.arrow_colour));
                worker.start();
                this.workers.add(worker);
                x += this.search_size;
            }
        }
        return modified_frame;
    }
}
