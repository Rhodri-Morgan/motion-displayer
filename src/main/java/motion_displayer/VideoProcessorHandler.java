package motion_displayer;


public class VideoProcessorHandler {

    private VideoFile video;

    public VideoProcessorHandler(VideoFile video) {
        this.video = video;
    }

    public void process() {
        VideoProcessorLink extract_frames = new FramesExtractorLink();
        extract_frames.handle(this.video);
    }
}
