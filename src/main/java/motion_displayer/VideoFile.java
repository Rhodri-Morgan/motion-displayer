package motion_displayer;

import java.util.LinkedList;
import java.nio.file.Path;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;


public class VideoFile {

    private VideoCapture video_capture;
    private int block_width;
    private int block_height;
    private LinkedList<Mat> frames;

    public VideoFile(Path path, int block_width, int block_height) {
        this.video_capture = new VideoCapture(path.toString());
        this.block_width = block_width;
        this.block_height = block_height;
    }

    public void setFrames(LinkedList<Mat> frames) {
        this.frames = frames;
    }

    public VideoCapture getVideoCapture() {
        return this.video_capture;
    }

    /**
     * Gets the width of each frame in the video
     * @return frame width of video
     */
    public int getFrameWidth() {
        return (int) this.video_capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
    }

    /**
     * Gets the height of each frame in the video
     * @return frame height of video
     */
    public int getFrameHeight() {
        return (int) this.video_capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
    }
}
