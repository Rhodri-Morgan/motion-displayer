package motion_displayer.view;

import javafx.scene.layout.Pane;


public interface AppState {

    Pane getRoot();

    void draw(AppStateContext context);
}
