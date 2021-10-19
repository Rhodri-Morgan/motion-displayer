package motion_displayer;

import java.util.LinkedList;
import java.nio.file.Path;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;


public class VideoFile {

    private VideoCapture video_capture;
    private int block_size;
    private int search_size;
    private LinkedList<Mat> unmodified_frames;
    private LinkedList<Mat> modified_frames;

    public VideoFile(Path path, int block_size, int search_size) {
        this.video_capture = new VideoCapture(path.toString());
        this.block_size = block_size;
        this.search_size = search_size;
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

    public LinkedList<Mat> getUnmodifiedFrames() {
        return this.unmodified_frames;
    }

    public LinkedList<Mat> getModifiedFrames() {
        return this.modified_frames;
    }

    public int getBlockSize() {
        return this.block_size;
    }

    public int getSearchSize() {
        return this.search_size;
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

}
