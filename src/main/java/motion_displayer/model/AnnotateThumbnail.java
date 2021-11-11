package motion_displayer.model;

import java.io.ByteArrayInputStream;
import org.opencv.core.MatOfByte;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Rect;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import javafx.scene.image.Image;


public class AnnotateThumbnail {

    private final Mat thumbnail;
    private final int frame_width;
    private final int frame_height;


    public AnnotateThumbnail(Mat thumbnail, int frame_width, int frame_height) {
        this.thumbnail = thumbnail;
        this.frame_width = frame_width;
        this.frame_height = frame_height;
    }

    /**
     * Applies styling choices to passed thumbnail
     * @param annotate_macro_block flag whether to draw search and block area boundaries
     * @param annotate_arrows flag whether to draw sample motion vector arrows
     * @param arrow_colour colour of sample motion vectors to be drawn
     * @param search_size size of search area
     * @param block_size size of block area
     * @return modified thumbnail with styling choices and parameters applied
     */
    public Image process(boolean annotate_macro_block, boolean annotate_arrows, Scalar arrow_colour, int search_size, int block_size) {
        Mat annotated_thumbnail = new Mat();
        this.thumbnail.copyTo(annotated_thumbnail);
        int x = 0;
        int y = 0;
        while (true) {
            if (y+search_size > this.frame_height) {
                MatOfByte byte_thumbnail = new MatOfByte();
                Imgcodecs.imencode(".bmp", annotated_thumbnail, byte_thumbnail);
                return new Image(new ByteArrayInputStream(byte_thumbnail.toArray()));
            } else if (x+search_size > this.frame_width) {
                x = 0;
                y += search_size;
            } else {
                int center_block_modifier = (int) Math.floor((double) (search_size - block_size) / 2);
                if (annotate_macro_block) {
                    Rect search_area_roi = new Rect(x, y, search_size, search_size);
                    Rect block_area_roi = new Rect(x+center_block_modifier, y+center_block_modifier, block_size, block_size);
                    Imgproc.rectangle(annotated_thumbnail, search_area_roi, new Scalar(255, 255, 255), 1);
                    Imgproc.rectangle(annotated_thumbnail, block_area_roi, new Scalar(255, 255, 255), 1);
                }
                if (annotate_arrows) {
                    Point start = new Point(x+center_block_modifier, y+center_block_modifier);
                    Point end = new Point(x, y);
                    Imgproc.arrowedLine(annotated_thumbnail, start, end, arrow_colour, 1, Imgproc.LINE_8, 0, 0.5);
                }
                x += search_size;
            }
        }
    }
}
