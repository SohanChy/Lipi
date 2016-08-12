package tests;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import model.toml.TomlConfig;
import model.toml.TomlParser;
import view.toml.TomlEditorControl;

public class RunTestsJavaFX extends Application {
    private static final String testsFolder = "tests-res";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {


        String inp;
        //inp = testsFolder + "/tomlConfigTest.toml";
        inp = testsFolder + "/tomlConfigTest.toml";

        TomlParser tomlConfig = new TomlConfig(inp);

        TomlEditorControl tomlEditor = new TomlEditorControl(tomlConfig);

        Button print = new Button("print");
        print.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println(tomlConfig);
            }
        });

        tomlEditor.getChildren().add(print);

        ScrollPane sp = new ScrollPane(tomlEditor);
        sp.setFitToHeight(true);
        sp.getStylesheets().add(getClass().getResource("material.css").toExternalForm());

        primaryStage.setTitle("Toml Editor");
        primaryStage.setScene(new Scene(sp));

        primaryStage.show();


    }
}
