package motion_displayer.model;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;
import org.opencv.core.Mat;


public class CalculateMotionVectorLink implements VideoProcessorLink {

    private final int max_threads = Runtime.getRuntime().availableProcessors() * 4;
    private List<Thread> workers = new ArrayList<>();
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
        int target_frames = unmodified_frames.size();
        ReentrantLock lock_modified_frames = new ReentrantLock();
        Mat frame = null;
        Mat previous_frame = null;
        while (modified_frames.size() != target_frames) {
            Mat modified_frame = new Mat();
            try {
                frame = unmodified_frames.pop();
                frame.copyTo(modified_frame);
            } catch (NullPointerException e) {
                modified_frames.addLast(previous_frame);
            }
            int current_x = 0;
            int current_y = 0;
            while (frame != null && previous_frame != null) {
                if (current_y >= video.getFrameHeight()) {
                    while (this.workers.size() != 0) {
                        clearFinishedThreads();
                    }
                    modified_frames.addLast(modified_frame);
                    break;
                } else if (current_x >= video.getFrameWidth()) {
                    current_x = 0;
                    current_y += video.getSearchSize();
                } else {
                    if (this.workers.size() == this.max_threads) {
                        clearFinishedThreads();
                    }
                    Thread worker = new Thread(new CalculateBlockVectorWorker(video,
                                                                              lock_modified_frames,
                                                                              new MeanAbsoluteDifference(),
                                                                              modified_frame,
                                                                              frame,
                                                                              previous_frame,
                                                                              current_x,
                                                                              current_y));
                    worker.start();
                    this.workers.add(worker);
                    current_x += video.getSearchSize();
                }
            }
            if (previous_frame == null) {
                modified_frames.addLast(frame);
            }
            previous_frame = frame;
            System.out.printf("\rProcessed Frame Count %d/%d", modified_frames.size(), target_frames);
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
