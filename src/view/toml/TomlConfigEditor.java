package view.toml;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.utility.FileHandler;
import view.utils.ExceptionAlerter;
import view.utils.TimedUpdaterUtil;

import java.io.IOException;

/**
 * Created by Sohan Chowdhury on 8/27/16.
 * Website: sohanchy.com
 * Email: sifat3d@gmail.com
 */
public class TomlConfigEditor extends ScrollPane {
    private String filePath;
    private Button saveButton;
    private VBox holderVBox;
    private TomlEditorControl tomlEditorControl;

    public TomlConfigEditor(String filePath) {
        super();

        this.filePath = filePath;

        setupTomlEditorControl();
        holderVBox = new VBox(tomlEditorControl);

        setupSaveButton();

        this.setPrefHeight(650.0);
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.setContent(holderVBox);
    }

    private void setupTomlEditorControl() {
        try {
            tomlEditorControl = new TomlEditorControl(FileHandler.readFile(filePath));
        } catch (IOException e) {
            ExceptionAlerter.showException(e);
        }
    }

    private void setupSaveButton() {

        VBox saveButtonVbox = new VBox();
        saveButtonVbox.setAlignment(Pos.CENTER);
        saveButtonVbox.getStyleClass().add("save-button-vbox");

        saveButton = new Button("Save");
        saveButtonVbox.getChildren().add(saveButton);

        holderVBox.getChildren().add(saveButtonVbox);

        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FileHandler.writeFile(tomlEditorControl.getTomlString(), filePath);
                    TimedUpdaterUtil.temporaryLabeledUpdate(saveButton, "Saved");
                } catch (IOException e) {
                    ExceptionAlerter.showException(e);
                }
            }
        });
    }
}
