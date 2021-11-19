package motion_displayer;

import motion_displayer.view.AppStateController;
import java.util.Objects;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.stage.Stage;
import javafx.scene.image.Image;


public class MainFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    /*
        Solution by Kurt Kaylor - https://stackoverflow.com/questions/4691095/java-loading-dlls-by-a-relative-path-and-hide-them-inside-a-jar
     */
    public static void loadJarDll(String name) throws IOException {
        InputStream in = MainFX.class.getClassLoader().getResourceAsStream(name);
        byte[] buffer = new byte[1024];
        int read = -1;
        File temp = new File(new File(System.getProperty("java.io.tmpdir")), name);
        FileOutputStream fos = new FileOutputStream(temp);

        while((read = in.read(buffer)) != -1) {
            fos.write(buffer, 0, read);
        }
        fos.close();
        in.close();

        System.load(temp.getAbsolutePath());
    }

    @Override
    public void start(Stage stage) throws Exception {
        String protocol = this.getClass().getResource("").getProtocol();
        if (Objects.equals(protocol, "jar")) {
            loadJarDll("opencv_java453.dll");
        }
        else if (Objects.equals(protocol, "file")) {
            Path dll = Paths.get(System.getenv("SystemDrive"), "opencv", "build", "java", "x64", "opencv_java453.dll");     // Local Running
            System.load(dll.toString());
        }

        HostServices host_services = getHostServices();
        Application.setUserAgentStylesheet(STYLESHEET_MODENA);
        AppStateController window = new AppStateController(host_services, stage, 1000, 800);
        stage.setTitle("Motion Displayer");
        stage.getIcons().add(new Image(String.valueOf(this.getClass().getClassLoader().getResource("logo.png"))));
        stage.setResizable(false);
        stage.setOnCloseRequest(e -> {
            System.exit(0);
        });
        stage.setScene(window.getScene());
        stage.show();
    }
}
