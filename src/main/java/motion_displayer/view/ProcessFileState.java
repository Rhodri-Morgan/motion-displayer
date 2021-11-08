package motion_displayer.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;
import motion_displayer.model.VideoFile;
import motion_displayer.model.VideoProcessor;

public class ProcessFileState extends  AppState {

    private VideoFile video;
    private VideoProcessor video_processor;

    public ProcessFileState(AppStateController context, VideoFile video) {
        super(context);
        this.video = video;
        this.video_processor = new VideoProcessor(video);
    }

    @Override
    void draw() {
        Runnable runnable_video_processor = () -> this.video_processor.process();
        Thread thread_video_processor = new Thread(runnable_video_processor);
        thread_video_processor.start();
        Label progress_label = new Label();
        ProgressBar progress_bar = new ProgressBar();
        super.getContext().getRoot().getChildren().addAll(progress_label, progress_bar);
        Timeline timeLine = new Timeline(new KeyFrame(Duration.seconds(0.1), e -> {
            try {
                //progress_label.setText(this.video_processor.getStatus());
                //progress_bar.setProgress(this.video_processor.getProgress());
            } catch (NullPointerException n) {
                // Pass
            }
        }));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
        progress_label.setTranslateY(-10);
        progress_bar.setTranslateY(10);
    }
}
