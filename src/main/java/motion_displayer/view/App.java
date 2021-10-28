package motion_displayer.view;

import javafx.application.HostServices;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class App {

    private final Scene scene;
    private final AppStateContext context;

    public App(HostServices host_services, Stage stage, int width, int height) {
        StackPane root = new StackPane();
        this.scene = new Scene(root, width, height);
        this.scene.getStylesheets().add(String.valueOf(this.getClass().getResource("/styles.css")));
        this.context = new AppStateContext(host_services, stage, root);
    }

    public Scene getScene() {
        return this.scene;
    }
}
