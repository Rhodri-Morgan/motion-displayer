package motion_displayer.view;

import motion_displayer.model.VideoFile;
import motion_displayer.model.VideoProcessor;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;


public class ProcessFileState extends  AppState {

    private final VideoFile video;
    private final VideoProcessor video_processor;

    public ProcessFileState(AppStateController context, VideoFile video) {
        super(context);
        this.video = video;
        this.video_processor = new VideoProcessor(video);
    }

    @Override
    void draw() {
        Runnable runnable_video_processor = () -> {
            try {
                this.video_processor.processVideo();
            } catch (Exception e) {
                System.out.println(e);
                // Goto error screen
            }
        };
        Thread thread_video_processor = new Thread(runnable_video_processor);
        thread_video_processor.start();
        Label processing_label = new Label("Processing Video File");
        processing_label.setId("processing_label");
        processing_label.setTranslateY(-40);
        Label processed_count_label = new Label();
        processed_count_label.getStyleClass().add("processing_details");
        processed_count_label.setTranslateY(35);
        Label estimated_time_label = new Label();
        estimated_time_label.getStyleClass().add("processing_details");
        estimated_time_label.setTranslateY(60);
        ProgressBar progress_bar = new ProgressBar(0);
        progress_bar.setMinWidth(400);
        progress_bar.setMaxWidth(400);
        progress_bar.setMinHeight(25);
        progress_bar.setMaxHeight(25);
        super.getContext().getRoot().getChildren().addAll(processing_label, progress_bar, processed_count_label, estimated_time_label);
        Timeline timeLine = new Timeline(new KeyFrame(javafx.util.Duration.seconds(0.5), e -> {
            try {
                if (thread_video_processor.isAlive()) {
                    progress_bar.setProgress(this.video_processor.getProgress());
                    processed_count_label.setText("Completed " + this.video_processor.getProcessedFrames() + " / " + this.video.getFrameCount() + " frames.");
                    java.time.Duration estimated_time = this.video_processor.getEstimatedProcessingTime();
                    String estimated_minutes_seconds = estimated_time.toMinutesPart() + " minutes, " + estimated_time.toSecondsPart() + " seconds.";
                    if (estimated_time.toHours() == 0) {
                        estimated_time_label.setText("Estimated time to completion " + estimated_minutes_seconds);
                    } else {
                        estimated_time_label.setText("Estimated time to completion " + estimated_time.toHours() + " hours, " + estimated_minutes_seconds);
                    }
                } else {
                    // Goto success screen
                }
            } catch (NullPointerException | ArithmeticException n) {
                // Pass
            }
        }));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }
}
