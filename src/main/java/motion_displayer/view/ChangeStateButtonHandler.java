package motion_displayer.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;


public class ChangeStateButtonHandler implements EventHandler<ActionEvent> {

    private final AppStateController context;
    private final AppState next_state;

    /**
     * ChangeStateButtonHandler Constructor
     * @param context app state context
     * @param next_state the next state to be switched to
     */
    public ChangeStateButtonHandler(AppStateController context, AppState next_state) {
        this.context = context;
        this.next_state = next_state;
    }

    /**
     * Set state to change to that passed as part of constructor in class
     * @param event JavaFX action event of user click/drag/etc
     */
    @Override
    public void handle(ActionEvent event) {
        context.setState(next_state);
    }
}
