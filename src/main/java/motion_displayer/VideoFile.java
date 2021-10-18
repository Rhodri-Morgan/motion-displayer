package motion_displayer;

import java.util.LinkedList;
import java.nio.file.Path;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;


public class VideoFile {

    private VideoCapture video_capture;
    private int block_width;
    private int block_height;
    private LinkedList<Frame> unmodified_frames;
    private LinkedList<Frame> modified_frames;

    public VideoFile(Path path, int block_width, int block_height) {
        this.video_capture = new VideoCapture(path.toString());
        this.block_width = block_width;
        this.block_height = block_height;
    }

    public void setUnmodifiedFrames(LinkedList<Frame> unmodified_frames) {
        this.unmodified_frames = unmodified_frames;
    }

    public void setModifiedFrames(LinkedList<Frame> modified_frames) {
        this.modified_frames = modified_frames;
    }

    public VideoCapture getVideoCapture() {
        return this.video_capture;
    }

    public LinkedList<Frame> getUnmodifiedFrames() {
        return this.unmodified_frames;
    }

    public LinkedList<Frame> getModifiedFrames() {
        return this.modified_frames;
    }

    public int getBlockWidth() {
        return this.block_width;
    }

    public int getBlockHeight() {
        return this.block_height;
    }

    public int getFrameWidth() {
        return (int) this.video_capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
    }

    public int getFrameHeight() {
        return (int) this.video_capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
    }
}
