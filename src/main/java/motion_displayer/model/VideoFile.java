package motion_displayer.model;

import java.util.LinkedList;
import java.nio.file.Path;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;


public class VideoFile {

    private final VideoCapture video_capture;
    private final String out_path;
    private final int search_size;
    private final int block_size;
    private final int threshold;
    private final boolean debug;
    private LinkedList<Mat> unmodified_frames;
    private LinkedList<Mat> modified_frames;

    public VideoFile(Path in_path, Path out_path, int search_size, int block_size, int threshold, boolean debug) {
        this.video_capture = new VideoCapture(in_path.toString());
        this.out_path = out_path.toString();
        this.search_size = search_size;
        this.block_size = block_size;
        this.threshold = threshold;
        this.debug = debug;
    }

    public void setUnmodifiedFrames(LinkedList<Mat> unmodified_frames) {
        this.unmodified_frames = unmodified_frames;
    }

    public void setModifiedFrames(LinkedList<Mat> modified_frames) {
        this.modified_frames = modified_frames;
    }

    public VideoCapture getVideoCapture() {
        return this.video_capture;
    }

    public String getOutPath() {
        return this.out_path;
    }

    public LinkedList<Mat> getUnmodifiedFrames() {
        return this.unmodified_frames;
    }

    public LinkedList<Mat> getModifiedFrames() {
        return this.modified_frames;
    }

    public int getSearchSize() {
        return this.search_size;
    }

    public int getBlockSize() {
        return this.block_size;
    }

    public int getThreshold() {
        return this.threshold;
    }

    public int getFrameWidth() {
        return (int) this.video_capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
    }

    public int getFrameHeight() {
        return (int) this.video_capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
    }

    public int getFrameCount() {
        return (int) this.video_capture.get(Videoio.CAP_PROP_FRAME_COUNT);
    }

    public int getFps() {
        return (int) this.video_capture.get(Videoio.CAP_PROP_FPS);
    }

    public boolean isDebug() {
        return this.debug;
    }
}
