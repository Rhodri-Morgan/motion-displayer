package motion_displayer.view;

import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class AppStateContext {

    private final HostServices host_services;
    private Stage stage;
    private AppState state;

    public AppStateContext(HostServices host_services, Stage stage, StackPane root) {
        this.host_services = host_services;
        this.stage = stage;
        this.setState(new OpenFileState(root));
    }

    public Stage getStage() {
        return this.stage;
    }

    public void setState(AppState state) {
        this.state = state;
        this.draw();
    }

    private void draw() {
        this.state.getRoot().getChildren().clear();
        AnchorPane header = new AnchorPane();
        Image github_image = new Image(String.valueOf(this.getClass().getClassLoader().getResource("github_logo_white.png")));
        ImageView github_image_view = new ImageView(github_image);
        github_image_view.setPreserveRatio(true);
        github_image_view.setFitWidth(32);
        github_image_view.setFitHeight(32);
        AnchorPane.setTopAnchor(github_image_view, 10.0);
        AnchorPane.setRightAnchor(github_image_view, 10.0);
        String link = "https://github.com/Rhodri-Morgan/Motion-Displayer";
        Hyperlink github_link = new Hyperlink(link);
        github_link.setId("github_link");
        github_link.setText("GitHub.com/Rhodri-Morgan/Motion-Displayer");
        github_link.setBorder(Border.EMPTY);
        github_link.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                host_services.showDocument(link);
            }
        });
        AnchorPane.setTopAnchor(github_link, 18.0);
        AnchorPane.setRightAnchor(github_link, 45.0);
        header.getChildren().addAll(github_image_view, github_link);
        this.state.getRoot().getChildren().add(header);
        this.state.draw(this, header);
    }
}
