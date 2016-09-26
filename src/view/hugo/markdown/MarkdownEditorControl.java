package view.hugo.markdown;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.web.HTMLEditor;
import model.utility.Pandoc;

/**
 * Created by Sohan Chowdhury on 8/14/16.
 * Website: sohanchy.com
 * Email: sifat3d@gmail.com
 */

public class MarkdownEditorControl extends HTMLEditor {

    private final String banglaFontStyle = "<style>" +
            "p { \n" +
            "    font-family: 'Siyam Rupali';\n" +
            "-webkit-font-smoothing: antialiased;   \n" +
            "}" +
            "</style>";
    private final String normalFontStyle = "<style>" +
            "p { \n" +
            "sans-serif,Helvetica,Arial,Sans-serif; \n" +
            "-webkit-font-smoothing: antialiased;   \n" +
            "}" +
            "</style>";
    private final String editorBgColorStyle = "<style> \n" +
            "body { \n" +
            "background-color: #f0f0f0;" +
            "}" +
            "</style>";
    private boolean banglaMode = false;
    private Button banglaModeToggle, imageInsertButton;

    private Pandoc pandoc;

    public MarkdownEditorControl() {
        super();

        pandoc = new Pandoc();

        customize();
    }

    private void setupImageInsertButton() {

        imageInsertButton = new Button("Insert Image");

//        imageInsertButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent e) {
//                FileChooser fileChooser = new FileChooser();
//                fileChooser.setTitle("Open Resource File");
//                fileChooser.getExtensionFilters().addAll(
//                        new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
//                );
//                File selectedFile = fileChooser.showOpenDialog(new Stage());
//                if (selectedFile != null) {
//                    System.out.println(selectedFile.getName());
//                }
//            }
//        });
        imageInsertButton.setTooltip(new Tooltip("Coming soon!"));

    }

    private void setupBanglaModeToggle() {
        banglaModeToggle = new Button("English Mode");

        banglaModeToggle.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                banglaMode = !banglaMode;

                if (banglaMode) {
                    banglaModeToggle.setText("Bangla Mode");
                } else {
                    banglaModeToggle.setText("English Mode");
                }

                setMdText(getMdText());

            }
        });
    }

    private void customize() {
        //TODO Customize HTMLEDITOR later.

        ToolBar topBar = (ToolBar) this.lookup(".top-toolbar");
        ToolBar bottomBar = (ToolBar) this.lookup(".bottom-toolbar");

        setupBanglaModeToggle();
        setupImageInsertButton();

        topBar.getItems().add(banglaModeToggle);
        bottomBar.getItems().add(imageInsertButton);

        setMdText("Hello, this is where you write :)");
    }


    public String getMdText() {
        return (pandoc.htmlToMd(getHtmlText()));
    }

    public void setMdText(String md) {
        setHtmlText("Loading...");

        setHtmlText((editorBgColorStyle + getFontStyle() + pandoc.mdToHtml(md)));
    }

    private void setBanglaMode(boolean forceBangla) {
        banglaMode = forceBangla;
    }

    private String getFontStyle() {
        if (banglaMode) {
            return banglaFontStyle;
        } else {
            return normalFontStyle;
        }
    }

}
