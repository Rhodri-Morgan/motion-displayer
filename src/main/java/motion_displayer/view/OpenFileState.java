package motion_displayer.view;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;


public class OpenFileState implements  AppState {

    private final Pane root;

    public OpenFileState(Pane root){
        this.root = root;
    }

    @Override
    public Pane getRoot() {
        return this.root;
    }

    @Override
    public void draw(AppStateContext context) {
        Pane control_phase = new AnchorPane();
        Button back_button = new Button("\uD83E\uDC60 Back");
        back_button.setOnAction(new BackButtonHandler(context, new TestState(this.root)));
        AnchorPane.setTopAnchor(back_button, 10.0);
        AnchorPane.setLeftAnchor(back_button, 10.0);
        control_phase.getChildren().addAll(back_button);
        this.root.getChildren().add(control_phase);
    }
}
