package model.toml;

import java.util.Map;

public class TomlString implements TomlParser {
    private String tomlString;
    private Map<String, Object> tomlMap;

    public TomlString() {
    }
    public TomlString(String toml) {
        setTomlString(toml);
    }

    public void setTomlString(String toml) {
        this.tomlString = toml;
        readTomlMap();
    }

    public Map<String, Object> getTomlMap() {
        return tomlMap;
    }

    public void setTomlMap(Map<String, Object> tomlMap) {
        this.tomlMap = tomlMap;
        writeTomlMap();
    }

    public void readTomlMap() {
        tomlMap = TomlUtils.readToml(tomlString);
    }

    public void writeTomlMap() {
        tomlString = TomlUtils.toToml(tomlMap);
    }


    @Override
    public String toString() {
        return tomlString;
    }
}
