package view.toml;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.FlowPane;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sohan Chowdhury on 8/12/16.
 Website: sohanchy.com
 Email: sifat3d@gmail.com
 */
class TomlEntryEditor extends FlowPane {

    private Label keyLabel;
    private TextInputControl valueTextControl;

    private TomlEntryEditor() {
        super();
        this.getStyleClass().add("toml-entry");
    }

    private TomlEntryEditor(String key) {
        this();
        keyLabel = new Label(key);
        keyLabel.setMinWidth(60.0);
        keyLabel.setLineSpacing(2);
    }

    TomlEntryEditor(String key, String value) {
        this(key);
        initValueTextControl(value);
        this.getChildren().addAll(keyLabel, valueTextControl);
    }

    public Map<String, Object> getValue() {
        Map<String, Object> tmpMap = new HashMap<>();

        String value = valueTextControl.getText();

        if (isStringOfArray(value)) {
            tmpMap.put(keyLabel.getText(), stringAsList(value));
        } else {
            tmpMap.put(keyLabel.getText(), value);
        }

        return tmpMap;
    }

    private boolean isStringOfArray(String string) {
        return string.trim().startsWith("[");
    }

    private List<String> stringAsList(String listString) {
        listString = listString.replace(", ", ",").replace(" ,", ",");
        listString = listString.replace(",", ", ");
        return Arrays.asList(listString.substring(1, listString.length() - 1).split(", "));
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
