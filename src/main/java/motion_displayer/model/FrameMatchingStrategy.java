package motion_displayer.model;

import org.opencv.core.Mat;


public abstract class FrameMatchingStrategy {

    /**
     * Sums the colour R,G,B values at coordinates in image
     * @param area subset/crop of image
     * @param x x coordinate/pixel to get summed colour
     * @param y y coordinate/pixel to get summed colour
     * @return summed R,G,B values are coordinate/pixel in image
     */
     double sumColour(Mat area, int x, int y) {
        double rgb_summed_image = 0;
        for (double rgb : area.get(x, y)) {
            rgb_summed_image += rgb;
        }
        return rgb_summed_image;
    }

    /**
     * Applies matching metric
     * @param block subset of original image that is centered block in search area
     * @param block_x block x (top-left) coordinate in original image
     * @param block_y block y (top-left) coordinate in original image
     * @param search_block subset of original image that is a valid iterating block in search area
     * @param search_x search block x (top-left) coordinate in original image
     * @param search_y search block y (top-left) coordinate in original image
     * @return value of applying given data to matching metric
     */
    abstract Double match(Mat block, int block_x, int block_y, Mat search_block, int search_x, int search_y);
}
