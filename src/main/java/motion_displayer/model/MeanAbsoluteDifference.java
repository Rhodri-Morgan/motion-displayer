package motion_displayer.model;

import org.opencv.core.Mat;


public class MeanAbsoluteDifference extends FrameMatchingStrategy {


    @Override
    public Double match(Mat block, Mat search_block) {
        double total = 0;
        for (int y=0; y<block.height(); y++) {
            for (int x=0; x<block.width(); x++) {
                total += Math.abs(sumColour(block, x, y) - sumColour(search_block, x, y));
            }
        }
        return (total / Math.pow(block.width(), 2));
    }
}
