package model.utility;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * Created by Sohan Chowdhury on 8/27/16.
 * Website: sohanchy.com
 * Email: sifat3d@gmail.com
 */
public class MarkdownFileUtils {
    public static FilenameFilter getMdFileFilter() {
        return new FilenameFilter() {
            public boolean accept(File dir, String name) {

                try {
                    File f = new File(dir.getCanonicalPath() + File.separator + name);

                    if (f.isDirectory()) {
                        return true;
                    } else {
                        return name.toLowerCase().endsWith(".md");
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                    return false;
                }
            }
        };
    }
}
