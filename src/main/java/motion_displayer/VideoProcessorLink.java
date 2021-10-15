package motion_displayer;


public interface VideoProcessorLink {

    void handle(VideoFile video);

    void addLink(VideoProcessorLink link);
}
