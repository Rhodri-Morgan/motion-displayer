package motion_displayer.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;


public class App {

    private HostServices host_services;
    private Scene scene;
    private Pane root;

    public App(HostServices host_services, int width, int height) {
        this.host_services = host_services;
        this.root = this.makeRoot();
        this.scene = new Scene(this.root, width, height);
        System.out.println(String.valueOf(this.getClass().getResource("/styles.css")));
        this.scene.getStylesheets().add(String.valueOf(this.getClass().getResource("/styles.css")));
    }

    public Scene getScene() {
        return this.scene;
    }

    private Pane makeRoot() {
        this.root = new AnchorPane();
        Image github_image = new Image(String.valueOf(this.getClass().getClassLoader().getResource("github_logo_white.png")));
        ImageView github_image_view = new ImageView(github_image);
        github_image_view.setPreserveRatio(true);
        github_image_view.setFitWidth(25);
        github_image_view.setFitHeight(25);
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
        AnchorPane.setTopAnchor(github_image_view, 10.0);
        AnchorPane.setRightAnchor(github_image_view, 10.0);
        AnchorPane.setTopAnchor(github_link, 13.0);
        AnchorPane.setRightAnchor(github_link, 40.0);
        this.root.getChildren().addAll(github_image_view, github_link);
        return this.root;
    }
}

/**
 * Root Pane for things applicable to all different stages of app
 * Add pane as children of root for new scenes/stages of app
 *
 * LOOK AT STYLE SHEETS FOR JAVAFX
 */
