package motion_displayer.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.nio.file.Path;


public class SuccessProcessedState extends AppState {

    private final Path output_file;

    public SuccessProcessedState(AppStateController context, Path output_file) {
        super(context);
        this.output_file = output_file;
    }

    /**
     * Draw success title and details of output file
     */
    private void drawSuccessTitle() {
        Image complete_image = new Image(String.valueOf(this.getClass().getClassLoader().getResource("tick.png")));
        ImageView complete_image_view = new ImageView(complete_image);
        complete_image_view.setPreserveRatio(true);
        complete_image_view.setFitWidth(125);
        complete_image_view.setFitHeight(125);
        complete_image_view.setTranslateY(-125.0);
        Label complete_notification_label = new Label("Processing Complete");
        complete_notification_label.setTranslateY(-5.0);
        complete_notification_label.getStyleClass().add("large_sized_text");
        String file_location_str = "The file as been saved as '" + output_file.getFileName().toString() + "' in the same directory as the original.";
        Label file_location_label = new Label(file_location_str);
        file_location_label.getStyleClass().add("medium_sized_text");
        file_location_label.setTranslateY(45.0);
        super.getContext().getRoot().getChildren().addAll(complete_image_view, complete_notification_label, file_location_label);
    }

    /**
     * Draw button and functionality to process another file (go back to open file state)
     */
    private void drawRestartButton() {
        Button restart_button = new Button("Process Another File");
        restart_button.setTranslateY(90.0);
        restart_button.setId("restart_button");
        restart_button.setOnAction(new ChangeStateButtonHandler(super.getContext(), new OpenFileState(super.getContext())));
        super.getContext().getRoot().getChildren().add(restart_button);
    }

    @Override
    public void draw() {
        try {
            this.drawSuccessTitle();
            this.drawRestartButton();
        } catch (Exception e) {
            e.printStackTrace();
            super.getContext().setState(new ErrorOccurredState(super.getContext()));
        }
    }
}
