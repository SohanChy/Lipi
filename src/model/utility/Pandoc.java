package model.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pandoc {

    private final static String[] paramHTMLtoMD = {"-f", "html", "-t", "markdown_github-raw_html-native_divs-native_spans"};
    private final static String[] paramMDtoHTML = {"-f", "markdown_github", "-t", "html"};
    private final Ipc pandoc;
    private List<String> params;

    public Pandoc() {
        pandoc = new Ipc("exec" + File.separator + "pandoc");

    }

    public String htmlToMd(String html) {

        String md = null;
        try {
            String outFile = FileHandler.getExpiringTempFile().getCanonicalPath();

            htmlToMdFile(html, outFile);

            md = FileHandler.readFile(outFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return md;
    }

    public String mdToHtml(String md) {

        String html = null;
        try {
            String outFile = FileHandler.getExpiringTempFile().getCanonicalPath();

            mdToHtmlFile(md, outFile);

            html = FileHandler.readFile(outFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return html;
    }

    private boolean htmlToMdFile(String html, String outFile) {

        try {
            File tmpHtml = FileHandler.stringToTempFile(html);

            params = new ArrayList<String>();
            params.addAll(Arrays.asList(paramHTMLtoMD));

            //System.out.println(tmpHtml.getCanonicalPath());

            params.add(tmpHtml.getCanonicalPath());
            params.add("-o");
            params.add(outFile);

            pandoc.setProgArgs(params);

            pandoc.runProc();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean mdToHtmlFile(String md, String outFile) {

        try {
            File tmpMd = FileHandler.stringToTempFile(md);

            params = new ArrayList<String>();
            params.addAll(Arrays.asList(paramMDtoHTML));

            //System.out.println(tmpHtml.getCanonicalPath());

            params.add(tmpMd.getCanonicalPath());
            params.add("-o");
            params.add(outFile);

            pandoc.setProgArgs(params);

            pandoc.runProc();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }

}
