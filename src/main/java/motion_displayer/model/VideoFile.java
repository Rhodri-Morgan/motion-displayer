package motion_displayer.model;

import java.util.LinkedList;
import java.nio.file.Paths;
import java.nio.file.Path;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import javafx.scene.image.Image;


public class VideoFile {

    private final VideoCapture video_capture;
    private final Mat thumbnail_clean;
    private final DecorateThumbnail decorate_thumbnail;
    private final String out_path;
    private int search_size;
    private int block_size;
    private LinkedList<Mat> unmodified_frames;
    private LinkedList<Mat> modified_frames;
    private Scalar arrow_colour;

    public VideoFile(Path in_path, Scalar arrow_colour) {
        this.video_capture = new VideoCapture(in_path.toString());
        this.search_size = this.getSuggestedSearchSize();
        this.block_size = this.getSuggestedBlockSize();
        this.arrow_colour = arrow_colour;
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

    public void setArrowColour(Scalar arrow_colour) {
        this.arrow_colour = arrow_colour;
    }

    public VideoCapture getVideoCapture() {
        return this.video_capture;
    }

    public Image getThumbnail(boolean print_macro_blocks, boolean print_arrows) {
       return this.decorate_thumbnail.process(this.thumbnail_clean.clone(),
                                              print_macro_blocks,
                                              print_arrows,
                                              this.arrow_colour,
                                              this.search_size,
                                              this.block_size);
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
        return 16;
    }

    public int getMinSearchSize() {
        return 10;
    }

    public int getMaxSearchSize() {
        return 60;
    }

    public int getBlockSize() {
        return this.block_size;
    }

    public int getSuggestedBlockSize() {
        return 7;
    }

    public int getMinBlockSize() {
        int min_block_size = (int) Math.round(this.search_size*0.25);
        if (min_block_size % 2 != 0) {
            min_block_size -= 1;
        }
        return min_block_size;
    }

    public int getMaxBlockSize() {
        int max_block_size = (int) Math.round(this.search_size*0.75);
        if (max_block_size % 2 != 0) {
            max_block_size += 1;
        }
        return max_block_size;
    }

    public Scalar getArrowColour() {
        return this.arrow_colour;
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
