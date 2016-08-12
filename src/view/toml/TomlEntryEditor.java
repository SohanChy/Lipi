package view.toml;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.FlowPane;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sohan on 8/12/16.
 */
class TomlEntryEditor extends FlowPane {

    private final Label keyLabel;
    private TextInputControl valueTextControl;

    private TomlEntryEditor(String key) {
        super();
        this.setStyle("-fx-border-width: 0 0 2 0; -fx-border-color: white;");
        this.getStylesheets().add(getClass().getResource("toml_entry_editor.css").toExternalForm());

        keyLabel = new Label(key);
        keyLabel.setPrefWidth(150.0);
        keyLabel.setLineSpacing(3);
    }

    TomlEntryEditor(String key, String value) {
        this(key);

        initValueTextControl(value);
        this.getChildren().addAll(keyLabel, valueTextControl);
    }


    public Map<String, String> getValue() {
        Map<String, String> tmpMap = new HashMap<>();
        tmpMap.put(keyLabel.getText(), valueTextControl.getText());
        return tmpMap;
    }

    private void initValueTextControl(String value) {

        if (value.length() > 30) {
            TextArea valueTextArea = new TextArea(value);
            valueTextArea.setPrefColumnCount(14);
            valueTextArea.setPrefRowCount(1);
            valueTextArea.prefHeight(20);
            valueTextArea.setWrapText(true);

            valueTextControl = valueTextArea;
        } else {
            TextField valueTextField = new TextField(value);
            valueTextField.setPrefColumnCount(15);
            valueTextControl = valueTextField;
        }

    }

}
