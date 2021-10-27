package motion_displayer.view;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;


public class TestState implements AppState {

    private final Pane root;

    public TestState(Pane root){
        this.root = root;
    }

    @Override
    public Pane getRoot() {
        return this.root;
    }

    @Override
    public void draw(AppStateContext context) {
        Pane control_phase = new AnchorPane();
        Button test_button = new Button("TEST TEST TEST");
        AnchorPane.setTopAnchor(test_button, 10.0);
        AnchorPane.setLeftAnchor(test_button, 10.0);
        control_phase.getChildren().addAll(test_button);
        this.root.getChildren().add(control_phase);
    }
}
