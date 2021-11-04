package motion_displayer.model;

import org.opencv.core.Mat;


public abstract class FrameMatchingStrategy {

     double sumColour(Mat area, int x, int y) {
        double rgb_summed_image = 0;
        for (double rgb : area.get(x, y)) {
            rgb_summed_image += rgb;
        }
        return rgb_summed_image;
    }

    abstract Double match(Mat block, int block_x, int block_y, Mat search_block, int search_x, int search_y);
}
