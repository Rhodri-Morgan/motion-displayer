package motion_displayer.view;

import java.nio.file.Path;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;


public class ConfigureOptionsState implements AppState {

    private final StackPane root;
    private final Path file_path;

    public ConfigureOptionsState(StackPane root, Path file_path){
        this.root = root;
        this.file_path = file_path;
    }

    @Override
    public StackPane getRoot() {
        return this.root;
    }

    @Override
    public void draw(AppStateContext context, AnchorPane header) {
        Button back_button = new Button("\uD83E\uDC60 Back");
        back_button.setOnAction(new BackButtonHandler(context, new OpenFileState(this.root)));
        AnchorPane.setTopAnchor(back_button, 10.0);
        AnchorPane.setLeftAnchor(back_button, 10.0);
        header.getChildren().addAll(back_button);
    }
}
