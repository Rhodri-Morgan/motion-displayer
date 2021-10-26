package motion_displayer;

import org.opencv.core.Mat;


public class MeanSquaredError implements FrameMatchingStrategy {

    private int sumColour(Mat area, int x, int y) {
        int rgb_summed_image = 0;
        for (double rgb : area.get(x, y)) {
            rgb_summed_image += (int) rgb;
        }
        return rgb_summed_image;
    }

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
