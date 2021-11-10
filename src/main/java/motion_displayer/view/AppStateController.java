package motion_displayer.view;

import javafx.application.HostServices;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

import java.nio.file.Paths;


public class AppStateController {

    private final HostServices host_services;
    private final Stage stage;
    private final StackPane root;
    private final Scene scene;
    private final int width;
    private final int height;
    private AppState state;

    public AppStateController(HostServices host_services, Stage stage, int width, int height) {
        this.root = new StackPane();
        this.host_services = host_services;
        this.stage = stage;
        this.width = width;
        this.height = height;
        this.scene = new Scene(root, width, height);
        this.scene.getStylesheets().add(String.valueOf(this.getClass().getResource("/styles.css")));
        this.setState(new OpenFileState(this));
    }

    public Stage getStage() {
        return this.stage;
    }

    public StackPane getRoot() {
        return this.root;
    }

    public Scene getScene() {
        return this.scene;
    }

    public HostServices getHostServices() {
        return this.host_services;
    }

    public int getScreenWidth() {
        return this.width;
    }

    public int getScreenHeight() {
        return this.height;
    }

    public void setState(AppState state) {
        this.state = state;
        this.root.getChildren().clear();
        this.state.drawGithubLink();
        this.state.draw();
    }
}
