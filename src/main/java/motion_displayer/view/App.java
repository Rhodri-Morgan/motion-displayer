package motion_displayer.view;

import javafx.application.HostServices;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;


public class App {

    private final Scene scene;
    private final AppStateContext context;

    public App(HostServices host_services, int width, int height) {
        Pane root = new AnchorPane();
        this.scene = new Scene(root, width, height);
        this.scene.getStylesheets().add(String.valueOf(this.getClass().getResource("/styles.css")));
        this.context = new AppStateContext(host_services, root);
    }

    public Scene getScene() {
        return this.scene;
    }

    public AppStateContext getAppStateContext() {
        return this.context;
    }
}
