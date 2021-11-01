package motion_displayer.model;

import javafx.scene.image.Image;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


import java.io.ByteArrayInputStream;

public class DecorateThumbnail {

    private final int frame_width;
    private final int frame_height;

    public DecorateThumbnail(int frame_width, int frame_height) {
        this.frame_width = frame_width;
        this.frame_height = frame_height;
    }

    public Image process(Mat thumbnail, Scalar colour, int search_size, int block_size) {
        int x = 0;
        int y = 0;
        while (true) {
            if (y+search_size > this.frame_height) {
                MatOfByte byte_thumbnail = new MatOfByte();
                Imgcodecs.imencode(".bmp", thumbnail, byte_thumbnail);
                return new Image(new ByteArrayInputStream(byte_thumbnail.toArray()));
            } else if (x+search_size > this.frame_width) {
                x = 0;
                y += search_size;
            } else {
                int thickness = (int) Math.round((double) search_size/20);
                int center_block_modifier = (int) Math.floor((double) (search_size - block_size) / 2);
                Rect search_area_roi = new Rect(x, y, search_size, search_size);
                Rect block_area_roi = new Rect(x+center_block_modifier, y+center_block_modifier, block_size, block_size);
                Imgproc.rectangle(thumbnail, search_area_roi, colour, thickness);
                Imgproc.rectangle(thumbnail, block_area_roi, colour, thickness);
                x += search_size;
            }
        }
    }
}
