package motion_displayer.model;

import org.opencv.core.Mat;


public class MeanSquaredError extends FrameMatchingStrategy {

    @Override
    public Double match(Mat block, int block_x, int block_y, Mat search_block, int search_x, int search_y) {
        double total = 0;
        for (int y=0; y<block.height(); y++) {
            for (int x=0; x<block.width(); x++) {
                total += Math.pow(sumColour(search_block, x, y) - sumColour(block, x, y), 2);
            }
        }
        return (total / Math.pow(block.width(), 2));
    }
}
