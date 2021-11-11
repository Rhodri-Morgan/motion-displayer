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
    private boolean exception_occurred = false;
    private Thread thread_video_processor;
    private Timeline time_line;

    public ProcessFileState(AppStateController context, VideoFile video) {
        super(context);
        this.video = video;
        this.video_processor = new VideoProcessor(video);
    }

    /**
     * Begin thread to perform video processing while JavaFx thread builds UI
     */
    private void beginProcessing() {
        Runnable runnable_video_processor = () -> {
            try {
                this.video_processor.processVideo();
            } catch (Exception e) {
                this.exception_occurred = true;
            }
        };
        this.thread_video_processor = new Thread(runnable_video_processor);
        this.thread_video_processor.start();
    }

    /**
     * Draw progress title, bar, percentage and estimated time until completion
     */
    private void drawProgress() {
        Label processing_label = new Label("Processing Video File");
        processing_label.getStyleClass().add("large_sized_text");
        processing_label.setTranslateY(-60.0);
        ProgressBar progress_bar = new ProgressBar(0);
        progress_bar.setId("progress_bar");
        progress_bar.setMinWidth(400);
        progress_bar.setMaxWidth(400);
        progress_bar.setMinHeight(25);
        progress_bar.setMaxHeight(25);
        Label processed_count_label = new Label("Completed 0 / " + this.video.getFrameCount() + " frames.");
        processed_count_label.getStyleClass().add("medium_sized_text");
        processed_count_label.setId("processed_count_label");
        processed_count_label.setTranslateY(60.0);
        Label estimated_time_label = new Label();
        estimated_time_label.getStyleClass().add("medium_sized_text");
        estimated_time_label.setId("estimated_time_label");
        estimated_time_label.setTranslateY(85.0);
        super.getContext().getRoot().getChildren().addAll(processing_label, progress_bar, processed_count_label, estimated_time_label);
    }

    /**
     * Sets up timeline to update UI elements to properly represent progress
     */
    private void setupTimeline() {
        ProgressBar progress_bar = (ProgressBar) super.getContext().getScene().lookup("#progress_bar");
        Label processed_count_label = (Label) super.getContext().getScene().lookup("#processed_count_label");
        Label estimated_time_label = (Label) super.getContext().getScene().lookup("#estimated_time_label");
        this.time_line = new Timeline(new KeyFrame(javafx.util.Duration.seconds(0.25), e -> {
            try {
                if (this.thread_video_processor.isAlive()) {
                    progress_bar.setProgress(this.video_processor.getProgress());
                    processed_count_label.setText("Completed " + this.video_processor.getProcessedFrames() + " / " + this.video.getFrameCount() + " frames.");
                    java.time.Duration estimated_time = this.video_processor.getEstimatedProcessingTime();
                    String estimated_minutes_seconds = estimated_time.toMinutesPart() + " minutes, " + estimated_time.toSecondsPart() + " seconds.";
                    if (estimated_time.toHours() == 0) {
                        estimated_time_label.setText("Estimated time to completion " + estimated_minutes_seconds);
                    } else {
                        estimated_time_label.setText("Estimated time to completion " + estimated_time.toHours() + " hours, " + estimated_minutes_seconds);
                    }
                }
                else if (this.exception_occurred) {
                    this.time_line.stop();
                    super.getContext().setState(new ErrorOccurredState(super.getContext()));
                }
                else {
                    this.time_line.stop();
                    super.getContext().setState(new SuccessProcessedState(super.getContext(), this.video.getOutPath()));
                }
            } catch (NullPointerException | ArithmeticException n) {
                // Pass
            }
        }));
        this.time_line.setCycleCount(Timeline.INDEFINITE);
        this.time_line.play();
    }

    @Override
    void draw() {
        try {
            this.beginProcessing();
            this.drawProgress();
            this.setupTimeline();
        } catch (Exception e) {
            e.printStackTrace();
            super.getContext().setState(new ErrorOccurredState(super.getContext()));
        }
    }
}
