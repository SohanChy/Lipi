package view.toml;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.toml.TomlParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TomlEditorControl extends VBox {
    private final List<Pane> tomlEntry;
    private final String groupKey;
    private Map<String, Object> tomlMap;
    private TomlParser tomlParser;

    private TomlEditorControl(Map<String, Object> tomlMap, String groupKey) {
        this.tomlMap = tomlMap;
        this.groupKey = groupKey;

        this.tomlEntry = new ArrayList<Pane>();
        buildTomlEditor();

        this.getChildren().addAll(tomlEntry);
    }

    private TomlEditorControl(Map<String, Object> tomlMap) {
        this(tomlMap, null);
    }

    public TomlEditorControl(TomlParser tomlParser) {
        this(tomlParser.getTomlMap());
        this.tomlParser = tomlParser;

        if (groupKey == null) {
            addSaveButton();
        }
    }

    private String getGroupKey() {
        return groupKey;
    }

    private void addSaveButton() {

        VBox vb = new VBox();

        Button saveButton = new Button("Save");
        saveButton.setPrefWidth(150);
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            TomlParser tomlParser;

            @Override
            public void handle(ActionEvent event) {
                tomlParser.setTomlMap(readBackTomlMap());
            }

            public EventHandler<ActionEvent> setToml(TomlParser tomlParser) {
                this.tomlParser = tomlParser;
                return this;
            }
        }.setToml(tomlParser));

        vb.getChildren().add(saveButton);
        vb.setAlignment(Pos.CENTER);

        this.getChildren().add(vb);
    }

    private void buildTomlEditor() {
        for (Map.Entry<String, Object> entry : tomlMap.entrySet()) {

            if (entry.getValue() instanceof Map) {
                tomlEntry.add(new LabelVBox(entry.getKey()));

                //noinspection unchecked
                tomlEntry.add(new TomlEditorControl((Map<String, Object>) entry.getValue(), entry.getKey()));
            } else {
                //add to 0 index aka push front.
                tomlEntry.add(0, new TomlEntryEditor(entry.getKey(), entry.getValue().toString()));
            }

        }

    }

    private Map<String, Object> readBackTomlMap() {

        Map<String, Object> editedTomlMap = new HashMap<String, Object>();

        for (Pane iterPane : tomlEntry) {
            if (iterPane instanceof TomlEntryEditor) {
                TomlEntryEditor iterPaneCast = (TomlEntryEditor) iterPane;
                editedTomlMap.putAll(iterPaneCast.getValue());
            } else if (iterPane instanceof TomlEditorControl) {
                TomlEditorControl iterPaneCast = (TomlEditorControl) iterPane;
                editedTomlMap.put(iterPaneCast.getGroupKey(), iterPaneCast.readBackTomlMap());
            }
        }
        return editedTomlMap;
    }

    public TomlParser getTomlParser() {
        tomlParser.setTomlMap(readBackTomlMap());
        return this.tomlParser;
    }

    private class LabelVBox extends VBox {
        public LabelVBox(String text) {
            super();
            this.getStylesheets().add(getClass().getResource("toml_entry_editor.css").toExternalForm());
            this.setSpacing(5);

            Label label = new Label(text);
            label.setStyle("-fx-padding-bottom: 100;\n" +
                    "-fx-font-size:14px;" +
                    "-fx-text-fill: Blue;" +
                    "-fx-border-width: 0 0 2 0; -fx-border-color: green;");

            this.getChildren().add(label);
        }
    }
}
