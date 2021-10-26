package motion_displayer.model;


public class VideoProcessorHandler {

    private VideoFile video;

    public VideoProcessorHandler(VideoFile video) {
        this.video = video;
    }

    public void process() {
        VideoProcessorLink chain = new FramesExtractorLink();
        chain.addLink(new CalculateMotionVectorLink());
        chain.addLink(new VideoAssemblerLink());
        chain.handle(this.video);
    }
}
