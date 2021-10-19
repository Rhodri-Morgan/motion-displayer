package motion_displayer;

import org.opencv.core.Mat;


public interface FrameMatchingStrategy {

    double match(Mat block, int block_x, int block_y, Mat search_block, int search_x, int search_y);
}
