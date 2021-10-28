package motion_displayer;

import javafx.application.HostServices;
import motion_displayer.view.App;

import javafx.application.Application;
import javafx.stage.Stage;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Main extends Application {

    public static void main(String[] args) {
        Path dll = Paths.get(System.getenv("SystemDrive"), "opencv", "build", "java", "x64", "opencv_java453.dll");
        System.load(dll.toString());
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        HostServices host_services = getHostServices();
        Application.setUserAgentStylesheet(STYLESHEET_MODENA);
        App window = new App(host_services, stage, 1000, 800);
        stage.setTitle("Motion Displayer");
        stage.setResizable(false);
        stage.setScene(window.getScene());
        stage.show();
    }
}
