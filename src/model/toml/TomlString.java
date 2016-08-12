package model.toml;

import java.util.Map;

public class TomlString implements TomlParser {
    private String tomlString;
    private Map<String, Object> tomlMap;

    public TomlString(String toml) {
        this.tomlString = toml;
    }

    public Map<String, Object> getTomlMap() {
        return tomlMap;
    }

    public void setTomlMap(Map<String, Object> tomlMap) {
        this.tomlMap = tomlMap;
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
