package motion_displayer.model;

import java.util.LinkedList;
import java.nio.file.Paths;
import java.nio.file.Path;
import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;


public class VideoFile {

    private final int  min_search_size = 20;
    private final int max_search_size = 80;
    private final VideoCapture video_capture;
    private final Mat thumbnail_clean;
    private final DecorateThumbnail decorate_thumbnail;
    private final String out_path;
    private final CalculateMacroBlockSizes suggested_sizes;
    private int search_size;
    private int block_size;
    private LinkedList<Mat> unmodified_frames;
    private LinkedList<Mat> modified_frames;

    public VideoFile(Path in_path) {
        this.video_capture = new VideoCapture(in_path.toString());
        this.suggested_sizes = new CalculateMacroBlockSizes(this.getFrameWidth(), this.getFrameHeight());
        this.search_size = this.suggested_sizes.getSuggestedSearchSize();
        this.block_size = this.suggested_sizes.getSuggestedBlockSize();
        this.thumbnail_clean = new Mat();
        VideoCapture temp_capture = new VideoCapture(in_path.toString());
        temp_capture.read(this.thumbnail_clean);
        this.decorate_thumbnail = new DecorateThumbnail(this.getFrameWidth(), this.getFrameHeight());
        String file_name  = in_path.getFileName().toString();
        String file_extension = file_name.substring(file_name.length()-4);
        String out_file_name = file_name.substring(0, file_name.length()-4)+"_out"+file_extension;
        this.out_path = Paths.get(in_path.getParent().toString(), out_file_name).toString();
    }

    public void setUnmodifiedFrames(LinkedList<Mat> unmodified_frames) {
        this.unmodified_frames = unmodified_frames;
    }

    public void setModifiedFrames(LinkedList<Mat> modified_frames) {
        this.modified_frames = modified_frames;
    }

    public void setSearchSize(int search_size) {
        this.search_size = search_size;
    }

    public void setBlockSize(int block_size) {
        this.block_size = block_size;
    }

    public VideoCapture getVideoCapture() {
        return this.video_capture;
    }

    public Image getThumbnail() {
       return this.decorate_thumbnail.process(this.thumbnail_clean.clone(), new Scalar(255, 255, 255), this.search_size, this.block_size);
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

    public int getSuggestedSearchSize() {
        return this.suggested_sizes.getSuggestedSearchSize();
    }

    public int getMinSearchSize() {
        return this.min_search_size;
    }

    public int getMaxSearchSize() {
        return this.max_search_size;
    }

    public int getBlockSize() {
        return this.block_size;
    }

    public int getSuggestedBlockSize() {
        return this.suggested_sizes.getSuggestedBlockSize();
    }

    public int getMinBlockSize() {
        return (int) Math.round(this.search_size*0.25);
    }

    public int getMaxBlockSize() {
        return (int) Math.round(this.search_size*0.75);
    }

    public int getFrameWidth() {
        return (int) this.video_capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
    }

    public int getFrameHeight() {
        return (int) this.video_capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
    }

    public int getFps() {
        return (int) this.video_capture.get(Videoio.CAP_PROP_FPS);
    }
}
