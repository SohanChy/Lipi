package view.toml;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.FlowPane;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sohan Chowdhury on 8/12/16.
 Website: sohanchy.com
 Email: sifat3d@gmail.com
 */
class TomlEntryEditor extends FlowPane {

    private final Label keyLabel;
    private TextInputControl valueTextControl;

    private TomlEntryEditor(String key) {
        super();
        this.getStylesheets().add(getClass().getResource("toml_editor.css").toExternalForm());
        keyLabel = new Label(key);
        keyLabel.setMinWidth(60.0);
        keyLabel.setLineSpacing(2);
        this.setStyle("-fx-border-color: #ECEFF1;\n"
                + "-fx-border-width: 0 0 2 0; \n"
                + "-fx-padding: 5 0 5 0");
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
            valueTextArea.setPrefColumnCount(16);

            int cols = value.length() / 30;
            valueTextArea.setPrefRowCount(cols);
//            valueTextArea.prefHeight(20);
            valueTextArea.setWrapText(true);
            valueTextControl = valueTextArea;
        } else {
            TextField valueTextField = new TextField(value);
            valueTextField.setPrefColumnCount(18);
            valueTextControl = valueTextField;

        }

    }

}
