package view.utils;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

/**
 * Created by Sohan Chowdhury on 8/26/16.
 * Website: sohanchy.com
 * Email: sifat3d@gmail.com
 */

//  Modified and adapted from
//  http://code.makery.ch/blog/javafx-dialogs-official/

public final class TakeTwoInputsDialog {
    Dialog<ButtonType> dialog;
    private TextField firstInputField;
    private TextArea secondInputField;

    public TakeTwoInputsDialog(String title, String description) {
        dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(description);
    }

    public void setupDialog() {
        setupDialog("First Input", "", "Second Input", "");
    }

    public void setupDialog(String firstInputLabel, String firstInputPrompt,
                            String secondInputLabel, String secondInputPrompt) {

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));

        grid.add(new Label(firstInputLabel), 0, 0);
        firstInputField = new TextField();
        firstInputField.setPromptText(firstInputPrompt);
        firstInputField.setPrefColumnCount(35);
        grid.add(firstInputField, 1, 0);

        grid.add(new Label(secondInputLabel), 0, 1);
        secondInputField = new TextArea();
        secondInputField.setPrefColumnCount(35);
        secondInputField.setPrefRowCount(1);
        secondInputField.setPromptText(secondInputPrompt);
        grid.add(secondInputField, 1, 1);

        // Enable/Disable login button depending on whether a username was entered.
        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        firstInputField.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);
    }

    public boolean showDialog() {
        Optional<ButtonType> result = dialog.showAndWait();
        return (result.isPresent() && result.get() == ButtonType.OK);

    }

    public String getFirstInput() {
        return firstInputField.getText();
    }

    public String getSecondInput() {
        return secondInputField.getText();
    }

}