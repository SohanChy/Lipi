package view.wizard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import view.utils.ExceptionAlerter;

import java.io.IOException;

/**
 * Created by Sohan Chowdhury on 8/27/16.
 * Website: sohanchy.com
 * Email: sifat3d@gmail.com
 */
public class WelcomeWizard extends GridPane {

    Stage primaryStage;

    public WelcomeWizard(Stage primaryStage) {
        this.primaryStage = primaryStage;
        bindFxml();
    }

    private void bindFxml() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("welcome_wizard.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            System.out.println("Failed to load fxml" + e.getMessage());
            ExceptionAlerter.showException(e);
            throw new RuntimeException(e);
        }

        this.getStyleClass().add("welcome-wizard");
    }

    @FXML
    private void onCreateNewBlog() {
        primaryStage.setScene(new Scene(new BasicConfig(primaryStage)));
    }
}
