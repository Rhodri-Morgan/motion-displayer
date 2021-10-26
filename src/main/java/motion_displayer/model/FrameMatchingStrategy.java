package motion_displayer.model;

import org.opencv.core.Mat;


public interface FrameMatchingStrategy {

    Double match(Mat block, int block_x, int block_y, Mat search_block, int search_x, int search_y);
}
