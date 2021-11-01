package motion_displayer.view;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;


public interface AppState {

    StackPane getRoot();

    void draw(AppStateContext context);
}
