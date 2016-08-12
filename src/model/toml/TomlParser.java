package model.toml;

import java.util.Map;

public interface TomlParser {
    Map<String, Object> getTomlMap();

    void setTomlMap(Map<String, Object> tomlMap);

    void readTomlMap();

    void writeTomlMap();

    String toString();
}
