package motion_displayer.model;

import javafx.util.Pair;


public class CalculateMacroBlockSizes {

    private final int baseline_search_size = 40;
    private final double search_size_block_size_ratio = 0.5;
    private final int allowed_margin = 10;
    private final int frame_width;
    private final int frame_height;
    private int search_size;
    private int block_size;

    public CalculateMacroBlockSizes(int frame_width, int frame_height) {
        this.frame_width = frame_width;
        this.frame_height = frame_height;
        this.search_size = this.baseline_search_size;
        this.compute();
    }

    public int getSuggestedSearchSize() {
        return this.search_size;
    }

    public int getSuggestedBlockSize() {
        return this.block_size;
    }

    /**
     * Determines if the given side length yields search areas that exclude no pixels from analysis
     * @param search_size side length of proposed search area
     * @return boolean value according to if fitting perfectly
     */
    private boolean isValidSearchSize(int search_size) {
        boolean valid_frame_width = (((double) this.frame_width / search_size) % 1) == 0;
        boolean valid_frame_height = (((double) this.frame_height / search_size) % 1) == 0;
        return valid_frame_width && valid_frame_height;
    }

    /**
     * Using the given side length calculates the number of pixels excluded from analysis if one were to use this search area size
     * @param search_size side length of proposed search area
     * @return number of pixels excluded
     */
    private int getPixelRemainder(int search_size) {
        double width_num_search_areas = ((double) this.frame_width / search_size);
        double height_num_search_areas = ((double) this.frame_height / search_size);
        String width_num_search_areas_str = String.valueOf(width_num_search_areas);
        String height_num_search_areas_str = String.valueOf(height_num_search_areas);
        double width_remainder = Double.parseDouble("0" + width_num_search_areas_str.substring(width_num_search_areas_str.indexOf(".")));
        double height_remainder = Double.parseDouble("0" + height_num_search_areas_str.substring(height_num_search_areas_str.indexOf(".")));
        return (int) (Math.round(width_remainder*search_size) + Math.round(height_remainder*search_size));

    }

    /**
     * Using the given upper and lower bounds for side length, this determines which excludes the least number of pixels for analysis
     * @param under_search_size lower bound of side length for search area
     * @param over_search_size upper bound of side length for search area
     * @return best/minimising pair composed of side length and number of pixels excluded
     */
    private Pair<Integer, Integer> approximateBestSearchSize(int under_search_size, int over_search_size) {
        Pair<Integer, Integer> under_search_size_pair = new Pair<>(under_search_size, this.getPixelRemainder(under_search_size));
        Pair<Integer, Integer> over_search_size_pair = new Pair<>(over_search_size, this.getPixelRemainder(over_search_size));
        if (under_search_size_pair.getValue() < over_search_size_pair.getValue()) {
            return under_search_size_pair;
        } else {
            return over_search_size_pair;
        }
    }

    /**
     * Computes the best search area size and block area size according to baseline and allowed margin to extend from baseline
     */
    private void compute() {
        int under_suggested_search_size = this.baseline_search_size;
        int over_suggested_search_size = this.baseline_search_size;
        int current_margin = 0;
        while (current_margin != this.allowed_margin) {
            current_margin += 1;
            if (this.isValidSearchSize(under_suggested_search_size)) {
                this.search_size = under_suggested_search_size;
                break;
            }
            else if (this.isValidSearchSize(over_suggested_search_size)) {
                this.search_size = over_suggested_search_size;
                break;
            }
            else {
                Pair<Integer, Integer> approx_search_size_pair = this.approximateBestSearchSize(under_suggested_search_size, over_suggested_search_size);
                if (this.getPixelRemainder(this.search_size) > approx_search_size_pair.getValue()) {
                    this.search_size = approx_search_size_pair.getKey();
                }
                under_suggested_search_size -= 1;
                over_suggested_search_size += 1;
            }
        }
        this.block_size = (int) Math.floor((double) this.search_size * this.search_size_block_size_ratio);
    }
}
