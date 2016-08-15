package model.utility;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileHandler {

    private FileHandler() {
        //FULLY STATIC CLASS
    }

    public static String readFile(String path, Charset encoding) {
        byte[] encoded = null;
        try {
            encoded = Files.readAllBytes(Paths.get(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert encoded != null;
        return new String(encoded, encoding);
    }

    public static String readFile(String path) {

        return readFile(path, StandardCharsets.UTF_8);
    }

    public static void writeFile(String textString, File f, Charset encoding)
            throws IOException {

        try (Writer fWriter = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(f), encoding))) {
            fWriter.write(textString);
        }
    }

    public static void writeFile(String textString, File f)
            throws IOException {
        //System.out.println(f.getCanonicalPath());
        writeFile(textString, f, StandardCharsets.UTF_8);
    }

    public static void writeFile(String textString, String filepath)
            throws IOException {
        File f = new File(filepath);
        writeFile(textString, f, StandardCharsets.UTF_8);
    }

    public static File stringToTempFile(String textString) {
        File f = null;
        try {
            f = getExpiringTempFile();

            writeFile(textString, f);

            //System.out.println(textString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    public static File getExpiringTempFile() {

        File f = null;

        try {
            f = File.createTempFile("JavaFX_pd_", ".tmp");
            f.deleteOnExit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return f;
    }

}
