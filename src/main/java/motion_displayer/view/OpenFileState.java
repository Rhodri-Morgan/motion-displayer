package motion_displayer.view;

import java.io.File;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;


public class OpenFileState extends AppState {

    public OpenFileState(AppStateController context) {
        super(context);
    }

    @Override
    public void draw() {
        Button find_file_button = new Button("Open Video File...");
        find_file_button.setId("find_file_button");
        Label file_type_label = new Label("Supported Video Formats -  *.mp4 / *.avi / *.mov");
        file_type_label.setId("file_type_label");
        find_file_button.setOnAction(e -> {
            FileChooser file_chooser = new FileChooser();
            file_chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP4 File", "*.mp4"),
                new FileChooser.ExtensionFilter("AVI File", "*.avi"),
                new FileChooser.ExtensionFilter("MOV File", "*.mov")
            );
            file_chooser.setTitle("Open Video File");
            try {
                File file = file_chooser.showOpenDialog(super.getContext().getStage());
                super.getContext().setState(new ConfigureOptionsState(super.getContext(), file.toPath()));
            } catch (NullPointerException n) {
                //Pass
            }
        });
        file_type_label.setTranslateY(50);
        super.getContext().getRoot().getChildren().addAll(find_file_button, file_type_label);
    }
}
