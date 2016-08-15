package view.markdown;

import javafx.scene.web.HTMLEditor;
import model.utility.Pandoc;

/**
 * Created by Sohan Chowdhury on 8/14/16.
 * Website: sohanchy.com
 * Email: sifat3d@gmail.com
 */

public class MarkdownEditorControl extends HTMLEditor {

    private Pandoc pandoc;

    public MarkdownEditorControl() {
        super();

        pandoc = new Pandoc();

        customize();
    }

    private void customize() {
        //TODO Customize HTMLEDITOR later.

        setHtmlText("Hello, this is where you write :)");
    }


    public String getMdText() {
        return (pandoc.htmlToMd(getHtmlText()));
    }

    public void setMdText(String md) {
        setHtmlText(pandoc.mdToHtml(md));
    }

}
