package motion_displayer.model;


public interface VideoProcessorLink {

    void handle(VideoFile video);

    void addLink(VideoProcessorLink link);
}
