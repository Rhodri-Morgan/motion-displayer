package motion_displayer.view;

import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;


public abstract class AppState {

    private final AppStateController context;

    public AppState(AppStateController context) {
        this.context = context;
    }

    public AppStateController getContext() {
        return this.context;
    }

    public void drawGithubLink() {
        AnchorPane header = (AnchorPane) this.context.getScene().lookup("#header");
        boolean found = (header != null);
        if (!found) {
            header = new AnchorPane();
            header.setId("header");
        }
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
        github_link.setOnAction(e -> {
            this.context.getHostServices().showDocument(link);
        });
        AnchorPane.setTopAnchor(github_link, 18.0);
        AnchorPane.setRightAnchor(github_link, 45.0);
        header.getChildren().addAll(github_image_view, github_link);
        if (!found) {
            this.context.getRoot().getChildren().add(header);
        }
    }

    public void drawBackButton() {
        AnchorPane header = (AnchorPane) this.context.getScene().lookup("#header");
        boolean found = (header != null);
        if (!found) {
            header = new AnchorPane();
            header.setId("header");
        }
        Button back_button = new Button("\uD83E\uDC60 Back");
        back_button.setOnAction(new BackButtonHandler(this.context, new OpenFileState(this.context)));
        AnchorPane.setTopAnchor(back_button, 10.0);
        AnchorPane.setLeftAnchor(back_button, 10.0);
        header.getChildren().add(back_button);
        if (!found) {
            this.context.getRoot().getChildren().add(header);
        }
    }

    abstract void draw();
}
