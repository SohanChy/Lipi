/**
 * Created by Sohan Chowdhury on 9/26/16.
 * Website: sohanchy.com
 * Email: sifat3d@gmail.com
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import view.hugo.pane.HostServicesProviderUtil;
import view.wizard.WelcomeWizard;

import java.io.File;
import java.nio.file.Paths;

public class Lipi extends Application {

    public Stage primaryStage;
    Pane wizPane;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;

        readyGui();
        readyWizard();

        primaryStage.show();
    }

    private void readyGui() {

        //Must do this for opening browser;
        HostServicesProviderUtil.INSTANCE.init(getHostServices());

        //Load font
        File f = new File("kalpurush.ttf");
        try {
            Font.loadFont(f.getCanonicalPath(), 10);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        wizPane = new VBox();

        wizPane.getStylesheets().add(Paths.get("res/material.css").toAbsolutePath().toUri().toString());
        wizPane.getStylesheets().add(Paths.get("res/custom.css").toAbsolutePath().toUri().toString());

        primaryStage.getIcons().add(
                new Image(Paths.get("res/lipi-icon.png").toAbsolutePath().toUri().toString())
        );
    }

    public void readyWizard() {

        primaryStage.setTitle("Lipi");

        WelcomeWizard welcome = new WelcomeWizard(primaryStage);

        wizPane.getChildren().add(welcome);

        primaryStage.setScene(new Scene(wizPane));
    }
}
