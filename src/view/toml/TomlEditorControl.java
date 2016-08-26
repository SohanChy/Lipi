package view.toml;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.toml.TomlParser;
import model.toml.TomlString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TomlEditorControl extends VBox {
    private List<Pane> tomlEntry;
    private String groupKey;
    private Map<String, Object> tomlMap;
    private TomlParser tomlParser;

    private TomlEditorControl(Map<String, Object> tomlMap, String groupKey) {
        setTomlMap(tomlMap, groupKey);
    }

    public TomlEditorControl() {
        //Empty Constructor
    }

    public TomlEditorControl(TomlParser tomlParser) {
        setTomlParser(tomlParser);
    }

    public TomlEditorControl(String tomlString) {
        setTomlString(tomlString);
    }

    private void setTomlMap(Map<String, Object> tomlMap, String groupKey) {
        this.getChildren().clear();

        this.tomlMap = tomlMap;
        this.groupKey = groupKey;

        this.tomlEntry = new ArrayList<Pane>();
        buildTomlEditor();

        this.getChildren().addAll(tomlEntry);
    }

    private String getGroupKey() {
        return groupKey;
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

    public void setTomlParser(TomlParser tomlParser) {
        this.tomlParser = tomlParser;
        setTomlMap(tomlParser.getTomlMap(), null);

        /*if (groupKey == null) {
            addSaveButton();
        }*/
    }

    public String getTomlString() {
        tomlParser.setTomlMap(readBackTomlMap());
        return this.tomlParser.toString();
    }

    public void setTomlString(String tomlS) {
        this.tomlParser = new TomlString(tomlS);
        setTomlParser(this.tomlParser);
    }

    private class LabelVBox extends VBox {
        public LabelVBox(String text) {
            super();
            this.setSpacing(5);

            Label label = new Label(text);
            this.getChildren().add(label);
        }
    }
}
