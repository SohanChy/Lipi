package model.hugo;

import view.utils.ExceptionAlerter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Sohan Chowdhury on 8/26/16.
 * Website: sohanchy.com
 * Email: sifat3d@gmail.com
 */
public class HMDFileCreator {

    private HMDFileProcessor HMDFileProcessor;
    private File file;

    public HMDFileCreator(String filePath) {

        file = new File(filePath);
        try {
            file.createNewFile();
            HMDFileProcessor = new HMDFileProcessor(filePath);
        } catch (IOException e) {
            ExceptionAlerter.showException(e);
            System.exit(0);
        }

    }

    public void setupFile() {
        setupFile("Hello Internet!", "This is a simple empty blog post.", "");
    }

    public void setupFile(String title, String description, String postContent) {
        buildBasicFrontMatter(title, description);
        this.HMDFileProcessor.setPostContent(postContent);
    }

    private void buildBasicFrontMatter(String title, String description) {
        StringBuilder basicFrontMatter = new StringBuilder();

        basicFrontMatter.append("title = \"").append(title).append("\"\n");
        basicFrontMatter.append("description = \"").append(description).append("\"\n");

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        basicFrontMatter.append("lastmod = \"").append(timeStamp).append("\"\n");
        basicFrontMatter.append("date = \"").append(timeStamp).append("\"\n");

        basicFrontMatter.append("tags = [ \"Untagged\" ] \n");

        this.HMDFileProcessor.setFrontMatter(basicFrontMatter.toString());
    }

    public void setupAndMakeFile(String title, String description, String postContent) {
        setupFile(title, description, postContent);
        makeFile();
    }

    public void makeFile() {
        HMDFileProcessor.writeHMdFile(true);
    }
}
