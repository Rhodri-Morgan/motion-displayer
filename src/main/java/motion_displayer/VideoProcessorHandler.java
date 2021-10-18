package motion_displayer;


public class VideoProcessorHandler {

    private VideoFile video;

    public VideoProcessorHandler(VideoFile video) {
        this.video = video;
    }

    public void process() {
        VideoProcessorLink chain = new FramesExtractorLink();
        chain.addLink(new BlockSegmenterLink());
        chain.addLink(new CalculateMotionVectorLink(new SumSquareDifferenceStrategy()));
        chain.handle(this.video);
    }
}
