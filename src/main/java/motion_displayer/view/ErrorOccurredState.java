package motion_displayer.view;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class ErrorOccurredState extends AppState {

    public ErrorOccurredState(AppStateController context) {
        super(context);
    }

    private void drawErrorTitle() {
        Image error_image = new Image(String.valueOf(this.getClass().getClassLoader().getResource("cross.png")));
        ImageView error_image_view = new ImageView(error_image);
        error_image_view.setPreserveRatio(true);
        error_image_view.setFitWidth(125);
        error_image_view.setFitHeight(125);
        error_image_view.setTranslateY(-125.0);
        Label error_notification_label = new Label("An error has unexpectedly occurred!");
        error_notification_label.setTranslateY(-35.0);
        error_notification_label.setId("error_notification");
        error_notification_label.getStyleClass().add("large_sized_text");
        super.getContext().getRoot().getChildren().addAll(error_image_view, error_notification_label);
    }

    private void drawErrorFixes() {
        Label instructions_item_1_label = new Label("Please ensure:     \u2022 During processing the input file is not deleted/corrupted.");
        instructions_item_1_label.getStyleClass().add("medium_sized_text");
        instructions_item_1_label.setTranslateY(35.0);
        Label instructions_item_2_label = new Label("\u2022 During processing the output file is not deleted/corrupted.");
        instructions_item_2_label.getStyleClass().add("medium_sized_text");
        instructions_item_2_label.setTranslateX(65.0);
        instructions_item_2_label.setTranslateY(60.0);
        Label instructions_item_3_label = new Label("\u2022 There is enough space to store the outputted file.");
        instructions_item_3_label.getStyleClass().add("medium_sized_text");
        instructions_item_3_label.setTranslateX(38.0);
        instructions_item_3_label.setTranslateY(85.0);
        Label next_step_label = new Label("To retry close the application and attempt to process the file again.");
        next_step_label.getStyleClass().add("medium_sized_text");
        next_step_label.setTranslateY(145.0);
        super.getContext().getRoot().getChildren().addAll(instructions_item_1_label, instructions_item_2_label, instructions_item_3_label, next_step_label);
    }

    @Override
    public void draw() {
        this.drawErrorTitle();
        this.drawErrorFixes();
    }
}
