package motion_displayer;

import java.util.LinkedList;
import java.nio.file.Path;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;


public class VideoFile {

    private VideoCapture video;
    private LinkedList<Mat> frames;

    public VideoFile(Path path) {
        this.video = new VideoCapture(path.toString());
        this.frames = new LinkedList<Mat>();
    }

    /**
     * Using path given to constructor extracts frames
     * @return linked list representation where each link is a Mat frame
     */
    public LinkedList<Mat> getFrames() {
        if (this.frames.toArray().length == 0) {
            Mat frame = new Mat();
            while (true) {
                if (video.read(frame)) {
                    this.frames.addLast(frame);
                } else {
                    break;
                }
            }
        }
        return this.frames;
    }

    /**
     * Gets the width of each frame in the video
     * @return frame width of video
     */
    public int getFrameWidth() {
        return (int) video.get(Videoio.CAP_PROP_FRAME_WIDTH);
    }

    /**
     * Gets the height of each frame in the video
     * @return frame height of video
     */
    public int getFrameHeight() {
        return (int) video.get(Videoio.CAP_PROP_FRAME_HEIGHT);
    }
}
