package motion_displayer;

import org.opencv.core.Mat;


public class SumSquareDifferenceStrategy implements FrameMatchingStrategy {

    private int sumCount(int start, int count) {
        int result = 0;
        for (int i=start; i<=(start+count)-1; i++) {
            result += i;
        }
        return result;
    }

    private int sumColour(Mat area) {
        int rgb_summed_image = 0;
        for (int x=0; x<area.width(); x++) {
            for (int y=0; y<area.height(); y++) {
                for (double rgb : area.get(x, y)) {
                    rgb_summed_image += (int) rgb;
                }
            }
        }
        return rgb_summed_image;
    }

    @Override
    public double match(Mat block, int block_x, int block_y, Mat search_block, int search_x, int search_y) {
        int block_sum_x = sumCount(block_x, block.width());
        int block_sum_y = sumCount(block_y, block.height());
        int block_sum_c = sumColour(block);
        int search_sum_x = sumCount(search_x, search_block.width());
        int search_sum_y = sumCount(search_y, search_block.height());
        int search_sum_c = sumColour(search_block);
        return Math.sqrt(Math.pow((block_sum_x+block_sum_y+block_sum_c) - (search_sum_x+search_sum_y+search_sum_c),2));
    }
}
