package motion_displayer.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;


public class BackButtonHandler implements EventHandler<ActionEvent> {

    private final AppStateContext context;
    private final AppState next_state;

    public BackButtonHandler(AppStateContext context, AppState next_state) {
        this.context = context;
        this.next_state = next_state;
    }

    @Override
    public void handle(ActionEvent event) {
        context.setState(next_state);
    }
}
