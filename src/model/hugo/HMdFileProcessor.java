package model.hugo;

import model.toml.TomlUtils;
import model.utility.FileHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class HMdFileProcessor {
    private String frontMatter, postContent;
    private String filePath;
    private File mdFile;
    private boolean validMd;

    public HMdFileProcessor(String filePath) {
        this.filePath = filePath;
        setupFile();
    }

    public String getFileName() {
        return mdFile.getName();
    }

    public boolean isValidMd() {
        return validMd;
    }

    private void setupFile() {
        this.mdFile = new File(filePath);
    }

    public String getFrontMatter() {
        return frontMatter;
    }

    public void setFrontMatter(String frontMatter) {
        this.frontMatter = frontMatter.trim() + "\n";
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent.trim() + "\n";
    }

    public boolean readHMdFile() {

        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(mdFile), StandardCharsets.UTF_8));

            String line = br.readLine();

            for (int i = 0; i < 3; i++) {

                //try first 3 lines
                if (i == 2) {
                    throw new Exception("No toml frontmatter found, Invalid or Empty Hugo Markdown file.");
                } else if (line.contains(TomlUtils.TOML_IDENTIFIER)) {
                    break;
                }

                line = br.readLine();
            }

            frontMatter = postContent = "";
            for (line = br.readLine();
                 line != null && !line.contains(TomlUtils.TOML_IDENTIFIER);
                 line = br.readLine()) {

                frontMatter += (line + "\n");
            }

            frontMatter = frontMatter.trim() + "\n";

            if (frontMatter.isEmpty()) {
                throw new Exception("toml is empty");
            }

            for (line = br.readLine();
                 line != null;
                 line = br.readLine()) {

                postContent += (line + "\n");
            }

            postContent = postContent.trim() + "\n";

            validMd = true;
            return true;

        } catch (IOException e) {
            validMd = false;
            System.out.println("IOException: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            validMd = false;
            System.out.println("Some Exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }


    }

    public boolean writeHMdFile(boolean force) {
        String combinedMdText = TomlUtils.TOML_IDENTIFIER + "\n"
                + frontMatter + TomlUtils.TOML_IDENTIFIER + "\n \n"
                + postContent;

        try {
            FileHandler.writeFile(combinedMdText, mdFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean writeHMdFile() {

        try {
            if (validMd) {
                return writeHMdFile(true);
            } else {
                throw new Exception("Not valid HMd File.");
            }
        } catch (Exception e) {
            e.getMessage();
        }

        return false;
    }


}
