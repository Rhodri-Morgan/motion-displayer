package motion_displayer.view;

import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Rectangle2D;


public class ImageZoomHandler {

    private final double min_zoom_size = 50;
    private final AppStateController context;
    private final ImageView thumbnail_view;
    private final Button reset_zoom;
    private boolean is_cropped;
    private double mouse_click_x_thumbnail;
    private double mouse_click_y_thumbnail;
    private double mouse_click_x_scene;
    private double mouse_click_y_scene;


    public ImageZoomHandler(AppStateController context, ImageView thumbnail_view, Button reset_zoom, boolean is_cropped) {
        this.context = context;
        this.thumbnail_view = thumbnail_view;
        this.reset_zoom = reset_zoom;
        this.is_cropped = is_cropped;
    }

    /**
     * Sets image in a cropped stage - restricting further zooming and making resetting zoom possible
     */
    public void setCropped() {
        this.is_cropped = true;
        this.thumbnail_view.setCursor(Cursor.DEFAULT);
        this.reset_zoom.setVisible(true);
        this.reset_zoom.toFront();
    }

    /**
     * Stores mouse click begin location and draws rectangle to show user highlighted section of image
     * @param e mouse event of user click
     */
    private void setOnMousePressedThumbnail(MouseEvent e) {
        if (!this.is_cropped) {
            this.mouse_click_x_thumbnail = e.getX();
            this.mouse_click_y_thumbnail = e.getY();
            this.mouse_click_x_scene = e.getSceneX() - ((double) this.context.getScreenWidth() / 2);
            this.mouse_click_y_scene = e.getSceneY() - ((double) this.context.getScreenHeight() / 2);
            Rectangle zoom_rectangle = new Rectangle(this.mouse_click_x_scene, this.mouse_click_y_scene, 0, 0);
            zoom_rectangle.setId("zoom_rectangle");
            this.context.getRoot().getChildren().add(zoom_rectangle);
        }
    }

    /**
     * Bounds a value between min and max - e.g returning max if value greater than max
     * @param value value to be bounded between min/max
     * @param min min allowed for value
     * @param max max allowed for value
     * @return bounded value between min/max if exceeding those set boundaries
     */
    private double boundValue(double value, double min, double max) {
        if (value < min) {
            return min;
        }
        else if (value > max) {
            return max;
        }
        else {
            return value;
        }
    }

    /**
     * Using release location calculates selected area (also using stored start) and crops image to show that area
     * @param e mouse event of user releasing click
     */
    private void setOnMouseReleasedThumbnail(MouseEvent e) {
        if (!this.is_cropped) {
            double mouse_release_x_thumbnail = this.boundValue(e.getX(), 0, thumbnail_view.getBoundsInLocal().getWidth());
            double mouse_release_y_thumbnail = this.boundValue(e.getY(), 0, thumbnail_view.getBoundsInLocal().getHeight());
            double width_ratio = thumbnail_view.getImage().getWidth() / thumbnail_view.getBoundsInLocal().getWidth();
            double height_ratio = thumbnail_view.getImage().getHeight() / thumbnail_view.getBoundsInLocal().getHeight();
            double zoom_area_x_scene = Math.abs(this.mouse_click_x_thumbnail - mouse_release_x_thumbnail) * width_ratio;
            double zoom_area_y_scene = Math.abs(this.mouse_click_y_thumbnail - mouse_release_y_thumbnail) * height_ratio;
            if (zoom_area_x_scene >= min_zoom_size && zoom_area_y_scene >= min_zoom_size) {
                Rectangle2D crop = new Rectangle2D(Math.min(this.mouse_click_x_thumbnail, mouse_release_x_thumbnail) * width_ratio,
                                                   Math.min(this.mouse_click_y_thumbnail, mouse_release_y_thumbnail) * height_ratio,
                                                   zoom_area_x_scene,
                                                   zoom_area_y_scene);
                thumbnail_view.setViewport(crop);
                this.setCropped();
            }
            this.context.getRoot().getChildren().remove(this.context.getScene().lookup("#zoom_rectangle"));
        }
    }

    /**
     * Get adjusted x coordinate of mouse
     * @param mouse_current_x_thumbnail x coordinate of mouse in thumbnail
     * @param mouse_current_x_scene x coordinate of mouse in scene
     * @return adjusted x coordinate
     */
    private double getBoundedSceneX(double mouse_current_x_thumbnail, double mouse_current_x_scene) {
        double x_margin = (this.context.getScreenWidth() - thumbnail_view.getBoundsInLocal().getWidth()) / 2;
        if (mouse_current_x_thumbnail < 0) {
            return x_margin + thumbnail_view.getTranslateX();
        }
        else if (mouse_current_x_thumbnail > thumbnail_view.getBoundsInLocal().getWidth()) {
            return this.context.getScreenWidth() - x_margin + thumbnail_view.getTranslateX();
        }
        else {
            return mouse_current_x_scene;
        }
    }

    /**
     * Get adjusted y coordinate of mouse
     * @param mouse_current_y_thumbnail y coordinate of mouse in thumbnail
     * @param mouse_current_y_scene y coordinate of mouse in scene
     * @return adjusted y coordinate
     */
    private double getBoundedSceneY(double mouse_current_y_thumbnail, double mouse_current_y_scene) {
        double y_margin = (this.context.getScreenHeight() - thumbnail_view.getBoundsInLocal().getHeight()) / 2;
        if (mouse_current_y_thumbnail < 0) {
            return y_margin + thumbnail_view.getTranslateY();
        }
        else if (mouse_current_y_thumbnail > thumbnail_view.getBoundsInLocal().getHeight()) {
            return this.context.getScreenHeight() - y_margin + thumbnail_view.getTranslateY();
        }
        else {
            return mouse_current_y_scene;
        }
    }

    /**
     * Adjust selected area to adapt to size and location of mouse while dragging (properly represent selected crop)
     * @param e mouse event of user dragging while clicked
     */
    private void setOnMouseDraggedThumbnail(MouseEvent e) {
        if (!this.is_cropped) {
            double mouse_current_bounded_x_scene = this.getBoundedSceneX(e.getX(), e.getSceneX()) - ((double) this.context.getScreenWidth() / 2);
            double mouse_current_bounded_y_scene = this.getBoundedSceneY(e.getY(), e.getSceneY()) - ((double) this.context.getScreenHeight() / 2);
            double selected_area_width = Math.abs(this.mouse_click_x_scene - mouse_current_bounded_x_scene);
            double selected_area_height = Math.abs(this.mouse_click_y_scene - mouse_current_bounded_y_scene);
            Rectangle zoom_rectangle = (Rectangle) this.context.getScene().lookup("#zoom_rectangle");
            if (mouse_current_bounded_x_scene < this.mouse_click_x_scene) {
                zoom_rectangle.setTranslateX(mouse_current_bounded_x_scene + (selected_area_width / 2));
            } else {
                zoom_rectangle.setTranslateX(this.mouse_click_x_scene + (selected_area_width / 2));
            }
            if (mouse_current_bounded_y_scene < this.mouse_click_y_scene) {
                zoom_rectangle.setTranslateY(mouse_current_bounded_y_scene + (selected_area_height / 2));
            } else {
                zoom_rectangle.setTranslateY(this.mouse_click_y_scene + (selected_area_height / 2));
            }
            zoom_rectangle.setWidth(selected_area_width);
            zoom_rectangle.setHeight(selected_area_height);
        }
    }

    /**
     * Resets zoom on image (removing crop) and allows the user to zoom again
     * @param e action event of clicking button
     */
    private void setOnActionResetZoom(ActionEvent e) {
        this.is_cropped = false;
        this.reset_zoom.setVisible(false);
        this.thumbnail_view.setViewport(null);
    }

    /**
     * Sets up functionality to enable zooming
     */
    public void enableZoomFunctionality() {
        this.thumbnail_view.setOnMousePressed(e -> {this.setOnMousePressedThumbnail(e);});
        this.thumbnail_view.setOnMouseReleased(e -> {this.setOnMouseReleasedThumbnail(e);});
        this.thumbnail_view.setOnMouseDragged(e -> {this.setOnMouseDraggedThumbnail(e);});
        this.reset_zoom.setOnAction(e -> {this.setOnActionResetZoom(e);});
    }
}

