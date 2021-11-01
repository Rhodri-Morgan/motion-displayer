package motion_displayer.view;

import java.nio.file.Path;

import javafx.beans.binding.Bindings;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import motion_displayer.model.VideoFile;


public class ConfigureOptionsState implements AppState {

    private final StackPane root;
    private final VideoFile video;
    private AppStateContext context;

    public ConfigureOptionsState(StackPane root, Path file_path){
        this.root = root;
        this.video = new VideoFile(file_path);
    }

    private void drawBackButton() {
        AnchorPane header = (AnchorPane) this.context.getScene().lookup("#header");
        Button back_button = new Button("\uD83E\uDC60 Back");
        back_button.setOnAction(new BackButtonHandler(this.context, new OpenFileState(this.root)));
        AnchorPane.setTopAnchor(back_button, 10.0);
        AnchorPane.setLeftAnchor(back_button, 10.0);
        header.getChildren().add(back_button);
    }

    private void drawThumbnail() {
        try {
            ImageView thumbnail_view = (ImageView) this.context.getScene().lookup("#thumbnail_view");
            this.root.getChildren().remove(thumbnail_view);
        } catch (NullPointerException e) {
            // Pass
        }
        ImageView thumbnail_view = new ImageView();
        thumbnail_view.setId("thumbnail_view");
        thumbnail_view.setImage(this.video.getThumbnail());
        thumbnail_view.setPreserveRatio(true);
        double thumbnail_width = this.root.getWidth()-60.0;
        double thumbnail_height = thumbnail_width / ((double) 16 / 9);
        thumbnail_view.setFitWidth(thumbnail_width);
        thumbnail_view.setFitHeight(thumbnail_height);
        thumbnail_view.setTranslateY(-70.0);
        this.root.getChildren().add(thumbnail_view);
    }

    private void drawDivider() {
        Line divider = new Line();
        divider.setId("divider");
        divider.setStartX(0);
        divider.setEndX(this.root.getWidth()-30.0);
        divider.setTranslateY(220.0);
        this.root.getChildren().add(divider);
    }

    private void drawControls() {
        Label block_length_label = new Label("Block Area Side Length:");
        block_length_label.setTranslateX(-404.0);
        block_length_label.setTranslateY(320.0);
        Slider block_length_slider = new Slider(this.video.getMinBlockSize(), this.video.getMaxBlockSize(), this.video.getSuggestedBlockSize());
        block_length_slider.setOnMouseReleased(e -> {
            if (block_length_slider.getValue() != video.getBlockSize()) {
                this.video.setBlockSize((int) block_length_slider.getValue());
                this.drawThumbnail();
            }
        });
        block_length_slider.setShowTickMarks(true);
        block_length_slider.setShowTickLabels(true);
        block_length_slider.setMajorTickUnit(4);
        block_length_slider.setSnapToTicks(true);
        block_length_slider.setMinWidth(500);
        block_length_slider.setMaxWidth(500);
        block_length_slider.setTranslateX(-70);
        block_length_slider.setTranslateY(330.0);
        Label block_length_value = new Label();
        block_length_value.textProperty().bind(Bindings.format("%.0f", block_length_slider.valueProperty()));
        block_length_value.setTranslateX(210.0);
        block_length_value.setTranslateY(320.0);
        this.root.getChildren().addAll(block_length_label, block_length_slider, block_length_value);
        Label search_length_label = new Label("Search Area Side Length:");
        search_length_label.setTranslateX(-400.0);
        search_length_label.setTranslateY(260.0);
        Slider search_length_slider = new Slider(this.video.getMinSearchSize(), this.video.getMaxSearchSize(), this.video.getSuggestedSearchSize());
        search_length_slider.setOnMouseReleased(e -> {
            if (search_length_slider.getValue() != video.getSearchSize()) {
                this.video.setSearchSize((int) search_length_slider.getValue());
                block_length_slider.setMin(this.video.getMinBlockSize());
                block_length_slider.setMax(this.video.getMaxBlockSize());
                this.video.setBlockSize((int) block_length_slider.getValue());
                this.drawThumbnail();
            }
        });
        search_length_slider.setShowTickMarks(true);
        search_length_slider.setShowTickLabels(true);
        search_length_slider.setMajorTickUnit(4);
        search_length_slider.setSnapToTicks(true);
        search_length_slider.setMinWidth(500);
        search_length_slider.setMaxWidth(500);
        search_length_slider.setTranslateX(-70);
        search_length_slider.setTranslateY(270.0);
        Label search_length_value = new Label();
        search_length_value.textProperty().bind(Bindings.format("%.0f", search_length_slider.valueProperty()));
        search_length_value.setTranslateX(210.0);
        search_length_value.setTranslateY(260.0);
        this.root.getChildren().addAll(search_length_label, search_length_slider, search_length_value);
    }

    @Override
    public StackPane getRoot() {
        return this.root;
    }

    @Override
    public void draw(AppStateContext context) {
        this.context = context;
        this.drawBackButton();
        this.drawThumbnail();
        this.drawDivider();
        this.drawControls();
    }
}
