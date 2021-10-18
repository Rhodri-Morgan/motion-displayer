package motion_displayer;

import java.util.LinkedList;
import org.opencv.core.Mat;


public class Frame {

    private Mat frame;
    private LinkedList<Mat> blocks;

    public Frame(Mat frame) {
        this.frame = frame;
        this.blocks = new LinkedList<>();
    }

    public void setBlocks(LinkedList<Mat> blocks) {
        this.blocks = blocks;
    }

    public Mat getFrame() {
        return this.frame;
    }

    public LinkedList<Mat> getBlocks() {
        return this.blocks;
    }
}
