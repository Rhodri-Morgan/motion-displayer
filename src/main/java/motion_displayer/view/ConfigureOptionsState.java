package motion_displayer.view;

import motion_displayer.model.VideoFile;
import java.nio.file.Path;
import org.opencv.core.Scalar;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.geometry.Rectangle2D;
import javafx.beans.binding.Bindings;


public class ConfigureOptionsState implements AppState {

    private final VideoFile video;
    private final Color default_arrows_colour = Color.BLACK;
    private AppStateController context;

    public ConfigureOptionsState(Path file_path){
        this.video = new VideoFile(file_path, new Scalar(this.default_arrows_colour.getBlue(),
                                                         this.default_arrows_colour.getGreen(),
                                                         this.default_arrows_colour.getRed()));
    }

    private void drawBackButton() {
        AnchorPane header = (AnchorPane) this.context.getScene().lookup("#header");
        Button back_button = new Button("\uD83E\uDC60 Back");
        back_button.setOnAction(new BackButtonHandler(this.context, new OpenFileState()));
        AnchorPane.setTopAnchor(back_button, 10.0);
        AnchorPane.setLeftAnchor(back_button, 10.0);
        header.getChildren().add(back_button);
    }

    private void drawThumbnail() {
        boolean print_macro_blocks = false;
        boolean print_arrows = true;
        Rectangle2D crop = null;
        try {
            print_macro_blocks = ((CheckBox) this.context.getScene().lookup("#macro_blocks_toggle")).isSelected();
            print_arrows = ((CheckBox) this.context.getScene().lookup("#vectors_toggle")).isSelected();
            ImageView thumbnail_view = (ImageView) this.context.getScene().lookup("#thumbnail_view");
            Button reset_zoom = (Button) this.context.getScene().lookup("#reset_zoom");
            crop = thumbnail_view.getViewport();
            this.context.getRoot().getChildren().remove(thumbnail_view);
            this.context.getRoot().getChildren().remove(reset_zoom);
        }
        catch (NullPointerException e) {
            // Pass
        }
        Button reset_zoom = new Button("Reset Zoom");
        reset_zoom.setVisible(crop != null);
        reset_zoom.setTranslateX(400.0);
        reset_zoom.setTranslateY(155.0);
        reset_zoom.setId("reset_zoom");
        ImageView thumbnail_view = new ImageView();
        thumbnail_view.setId("thumbnail_view");
        Image thumbnail = this.video.getThumbnail(print_macro_blocks, print_arrows);
        thumbnail_view.setImage(thumbnail);
        thumbnail_view.setPreserveRatio(true);
        if (crop != null) {
            thumbnail_view.setViewport(crop);
        }
        ImageZoomHandler image_zoom_handler = new ImageZoomHandler(this.context, thumbnail_view, reset_zoom, crop != null);
        image_zoom_handler.enableZoomFunctionality();
        double thumbnail_width = this.context.getRoot().getWidth() - 60.0;
        double thumbnail_height = thumbnail_width / (16.0 / 9.0);
        thumbnail_view.setFitWidth(thumbnail_width);
        thumbnail_view.setFitHeight(thumbnail_height);
        thumbnail_view.setTranslateY(-70.0);
        this.context.getRoot().getChildren().addAll(thumbnail_view, reset_zoom);
    }

    private void drawThumbnailToggles() {
        CheckBox macro_blocks_toggle = new CheckBox("Search/Block Outlines");
        macro_blocks_toggle.setId("macro_blocks_toggle");
        macro_blocks_toggle.getStyleClass().add("thumbnail_toggle");
        macro_blocks_toggle.setTranslateY(220.0);
        macro_blocks_toggle.setTranslateX(-100.0);
        macro_blocks_toggle.selectedProperty().addListener(e -> {
            this.drawThumbnail();
        });
        CheckBox vectors_toggle = new CheckBox("Sample Vectors");
        vectors_toggle.setId("vectors_toggle");
        vectors_toggle.getStyleClass().add("thumbnail_toggle");
        vectors_toggle.setSelected(true);
        vectors_toggle.setTranslateY(220.0);
        vectors_toggle.setTranslateX(100.0);
        vectors_toggle.selectedProperty().addListener(e -> {
            this.drawThumbnail();
        });
        this.context.getRoot().getChildren().addAll(macro_blocks_toggle, vectors_toggle);
    }

    private void drawDivider() {
        Line divider = new Line();
        divider.setId("divider");
        divider.setStartX(0);
        divider.setEndX(this.context.getRoot().getWidth()-30.0);
        divider.setTranslateY(245.0);
        this.context.getRoot().getChildren().add(divider);
    }

    private void drawSliderControls() {
        Label block_length_label = new Label("Block Area Side Length:");
        block_length_label.setTranslateX(-404.0);
        block_length_label.setTranslateY(350.0);
        Slider block_length_slider = new Slider(this.video.getMinBlockSize(), this.video.getMaxBlockSize(), this.video.getSuggestedBlockSize());
        block_length_slider.setOnMouseReleased(e -> {
            if (block_length_slider.getValue() != video.getBlockSize()) {
                this.video.setBlockSize((int) block_length_slider.getValue());
                this.drawThumbnail();
            }
        });
        block_length_slider.setBlockIncrement(1);
        block_length_slider.setMajorTickUnit(2);
        block_length_slider.setMinorTickCount(1);
        block_length_slider.setShowTickMarks(true);
        block_length_slider.setShowTickLabels(true);
        block_length_slider.setSnapToTicks(true);
        block_length_slider.setId("block_length_slider");
        block_length_slider.setMinWidth(500.0);
        block_length_slider.setMaxWidth(500.0);
        block_length_slider.setTranslateX(-70.0);
        block_length_slider.setTranslateY(360.0);
        Label block_length_value = new Label();
        block_length_value.textProperty().bind(Bindings.format("%.0f", block_length_slider.valueProperty()));
        block_length_value.getStyleClass().add("slider_value");
        block_length_value.setTranslateX(210.0);
        block_length_value.setTranslateY(350.0);
        this.context.getRoot().getChildren().addAll(block_length_label, block_length_slider, block_length_value);
        Label search_length_label = new Label("Search Area Side Length:");
        search_length_label.setTranslateX(-400.0);
        search_length_label.setTranslateY(285.0);
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
        search_length_slider.setBlockIncrement(1);
        search_length_slider.setMajorTickUnit(4);
        search_length_slider.setShowTickMarks(true);
        search_length_slider.setShowTickLabels(true);
        search_length_slider.setSnapToTicks(true);
        search_length_slider.setId("search_length_slider");
        search_length_slider.setMinWidth(500.0);
        search_length_slider.setMaxWidth(500.0);
        search_length_slider.setTranslateX(-70.0);
        search_length_slider.setTranslateY(295.0);
        Label search_length_value = new Label();
        search_length_value.textProperty().bind(Bindings.format("%.0f", search_length_slider.valueProperty()));
        search_length_value.getStyleClass().add("slider_value");
        search_length_value.setTranslateX(210.0);
        search_length_value.setTranslateY(285.0);
        this.context.getRoot().getChildren().addAll(search_length_label, search_length_slider, search_length_value);
    }

    private void drawBeginControls() {
        Button begin_button = new Button("Process File");
        begin_button.setId("begin_button");
        begin_button.setTranslateX(360.0);
        begin_button.setTranslateY(280.0);
        Button suggested_button = new Button("Configure to suggested values");
        suggested_button.setTranslateX(360.0);
        suggested_button.setTranslateY(328.0);
        suggested_button.setOnAction(e -> {
            Slider block_length_slider = (Slider) context.getScene().lookup("#block_length_slider");
            Slider search_length_slider = (Slider) context.getScene().lookup("#search_length_slider");
            this.video.setBlockSize(this.video.getSuggestedBlockSize());
            this.video.setSearchSize(this.video.getSuggestedSearchSize());
            block_length_slider.setMin(this.video.getMinBlockSize());
            block_length_slider.setMax(this.video.getMaxBlockSize());
            block_length_slider.setValue(this.video.getSuggestedBlockSize());
            search_length_slider.setValue(this.video.getSuggestedSearchSize());
            drawThumbnail();
        });
        Label vector_colour_label = new Label("Vector Colour:");
        vector_colour_label.setTranslateX(290.0);
        vector_colour_label.setTranslateY(370.0);
        ColorPicker vector_colour_picker = new ColorPicker();
        vector_colour_picker.setValue(this.default_arrows_colour);
        vector_colour_picker.setTranslateX(405.0);
        vector_colour_picker.setTranslateY(370.0);
        vector_colour_picker.setOnAction(e -> {
            Color vector_colour = vector_colour_picker.getValue();
            this.video.setArrowColour(new Scalar(255*vector_colour.getBlue(), 255*vector_colour.getGreen(), 255*vector_colour.getRed()));
            this.drawThumbnail();
        });
        this.context.getRoot().getChildren().addAll(begin_button, suggested_button, vector_colour_label, vector_colour_picker);
    }

    @Override
    public void draw(AppStateController context) {
        this.context = context;
        this.drawBackButton();
        this.drawThumbnail();
        this.drawThumbnailToggles();
        this.drawDivider();
        this.drawSliderControls();
        this.drawBeginControls();
    }
}
