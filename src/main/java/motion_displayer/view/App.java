package motion_displayer.view;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;


public class App {

    private Pane pane;
    private Scene scene;

    public App(int width, int height) {
        this.pane = new Pane();
        this.scene = new Scene(this.pane, width, height);
    }

    public Scene getScene() {
        return this.scene;
    }
}
