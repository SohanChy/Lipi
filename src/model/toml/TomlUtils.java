package model.toml;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;

import java.util.Map;

public abstract class TomlUtils {

    public static final String TOML_IDENTIFIER = "+++";

    public static Map<String, Object> readToml(String toml) {
        return new Toml().read(toml).toMap();
    }

    public static String toToml(Map<String, Object> tomlMap) {
        TomlWriter tomlWriter = new TomlWriter();
        return tomlWriter.write(tomlMap);
    }

    public static String toToml(Object from) {
        TomlWriter tomlWriter = new TomlWriter();
        return tomlWriter.write(from);
    }

}
