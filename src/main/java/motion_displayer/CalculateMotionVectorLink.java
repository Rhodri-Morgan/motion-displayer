package motion_displayer;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import org.opencv.core.Mat;
import org.opencv.core.Rect;


public class CalculateMotionVectorLink implements VideoProcessorLink {

    private final int max_threads = Runtime.getRuntime().availableProcessors() * 4;
    private List<Thread> workers = new ArrayList<Thread>();
    private VideoProcessorLink next;

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

    @Override
    public void handle(VideoFile video) {
        System.out.println("--- Calculating Motion Vectors ---");
        LinkedList<Mat> unmodified_frames = video.getUnmodifiedFrames();
        LinkedList<Mat> modified_frames = new LinkedList<>();
        int processed_frame_count = 0;
        Mat frame = null;
        Mat next_frame;
        while (unmodified_frames.size() != 0) {
            if (frame == null) {
                frame = unmodified_frames.pop();
            }
            next_frame = unmodified_frames.pop();
            Map<Integer, Mat> modified_blocks = new HashMap<Integer, Mat>();
            int current_x = 0;
            int current_y = 0;
            int block_id = 0;
            while (true) {
                if (current_x >= video.getFrameWidth() && current_y == video.getFrameHeight() - video.getBlockSize()) {
                    while (this.workers.size() != 0) {
                        clearFinishedThreads();
                    }
                    // TODO Merge blocks to now modified frame
                    break;
                }
                else if (current_x >= video.getFrameWidth()) {
                    current_x = 0;
                    current_y += video.getBlockSize();
                } else {
                    if (this.workers.size() == this.max_threads) {
                        clearFinishedThreads();
                    } else {
                        Thread worker = new Thread(new CalculateBlockVectorWorker(block_id,
                                                                                  modified_blocks,
                                                                                  new SumSquareDifferenceStrategy(),
                                                                                  frame,
                                                                                  next_frame,
                                                                                  current_x,
                                                                                  current_y,
                                                                                  video.getSearchSize(),
                                                                                  video.getBlockSize()));
                        worker.start();
                        this.workers.add(worker);
                    }
                    current_x += video.getBlockSize();
                }
            }
            frame = next_frame;
            processed_frame_count += 1;
            System.out.printf("\rProcessed Frame Count %d/%d", processed_frame_count, video.getFrameCount());
        }
        System.out.print("\n");
        video.setModifiedFrames(modified_frames);
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
