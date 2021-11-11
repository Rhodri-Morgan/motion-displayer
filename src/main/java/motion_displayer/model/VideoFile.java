package motion_displayer.model;

import java.nio.file.Paths;
import java.nio.file.Path;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;
import javafx.scene.image.Image;


public class VideoFile {

    private final VideoCapture video_capture;
    private final AnnotateThumbnail annotate_thumbnail;
    private final Path in_path;
    private final Path out_path;
    private VideoWriter video_writer;
    private int search_size;
    private int block_size;
    private Scalar arrow_colour;

    /**
     * VideoFile Constructor extracts thumbnail, gets out path and applied suggested search/block sizes
     * @param in_path path of video for processing
     * @param arrow_colour colour of motion vectors to be drawn
     */
    public VideoFile(Path in_path, Scalar arrow_colour) {
        this.video_capture = new VideoCapture(in_path.toString());
        this.in_path = in_path;
        String file_name  = in_path.getFileName().toString();
        String file_extension = file_name.substring(file_name.length()-4);
        String out_file_name = file_name.substring(0, file_name.length()-4)+"_out"+file_extension;
        this.out_path = Paths.get(in_path.getParent().toString(), out_file_name);
        this.search_size = this.getSuggestedSearchSize();
        this.block_size = this.getSuggestedBlockSize();
        this.arrow_colour = arrow_colour;
        Mat thumbnail= new Mat();
        VideoCapture temp_capture = new VideoCapture(in_path.toString());
        temp_capture.read(thumbnail);
        this.annotate_thumbnail = new AnnotateThumbnail(thumbnail, this.getFrameWidth(), this.getFrameHeight());
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

    /**
     * If video writer has not been instantiated it creates output file and passes method to write to it
     * @return video writer object for writing to out path
     */
    public VideoWriter getVideoWriter() {
        if (this.video_writer == null) {
            this.video_writer = new VideoWriter(this.getOutPath().toString(),
                                                VideoWriter.fourcc('D','I','V','X'),
                                                this.getFps(),
                                                new Size(this.getFrameWidth(), this.getFrameHeight()));
        }
        return this.video_writer;
    }

    /**
     * Gets and annotates thumbnail with given parameters
     * @param annotate_macro_block flag whether to draw search and block area boundaries
     * @param annotate_arrows flag whether to draw sample motion vector arrows
     * @return annotated thumbnail with parameters in this class and passed to method
     */
    public Image getThumbnail(boolean annotate_macro_block, boolean annotate_arrows) {
       return this.annotate_thumbnail.process(annotate_macro_block, annotate_arrows, this.arrow_colour, this.search_size, this.block_size);
    }

    public Path getInPath() {
        return this.in_path;
    }

    public Path getOutPath() {
        return this.out_path;
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
        return 50;
    }

    public int getBlockSize() {
        return this.block_size;
    }

    public int getSuggestedBlockSize() {
        return 7;
    }

    /**
     * Gets the min size of a block contained within the search area and ensures it is an even number
     * @return min block size allowed for this search area size in this video
     */
    public int getMinBlockSize() {
        int min_block_size = (int) Math.round(this.search_size*0.25);
        if (min_block_size % 2 != 0) {
            min_block_size -= 1;
        }
        return min_block_size;
    }

    /**
     * Gets the max size of a block contained within the search area and ensures it is an even number
     * @return max block size allowed for this search area size in this video
     */
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

    // WARNING THIS IS NOT 100% ACCURATE
    public int getFrameCount() {
        return (int) this.video_capture.get(Videoio.CAP_PROP_FRAME_COUNT);
    }
}
