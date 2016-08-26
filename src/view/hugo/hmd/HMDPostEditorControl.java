package view.hugo.hmd;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.hugo.HMDFileProcessor;
import model.utility.CallbackVisitor;
import view.hugo.markdown.MarkdownEditorControl;
import view.toml.TomlEditorControl;
import view.utils.ExceptionAlerter;
import view.utils.TimedUpdaterUtil;

import java.io.IOException;

/**
 * Created by Sohan Chowdhury on 8/15/16.
 * Website: sohanchy.com
 * Email: sifat3d@gmail.com
 */
public class HMDPostEditorControl extends AnchorPane {
    @FXML
    private TomlEditorControl tomlEditorControl;
    @FXML
    private MarkdownEditorControl markdownEditorControl;
    @FXML
    private ToolBar rightToolBar;

    @FXML
    private Button onDashboardButton;
    @FXML
    private Button saveButton;
    @FXML
    private Label statusLabel;


    private HMDFileProcessor hMdFile;

    public HMDPostEditorControl() {
        bindFxml();
    }

    public HMDPostEditorControl(HMDFileProcessor hMdFile) {
        this();
        this.hMdFile = hMdFile;
        setHMdFile(hMdFile);

    }

    private void bindFxml() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HMDPostEditor.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            System.out.println("Failed to load fxml");
            ExceptionAlerter.showException(e);
            throw new RuntimeException(e);
        }
    }

    public void setHMdFile(HMDFileProcessor hMdFile) {
        this.hMdFile = hMdFile;
        setupEditors();
    }

    private void setupEditors() {

        try {
            if (hMdFile.isValidMd()) {
//              System.out.println("isValid: " + hMdFile.isValidMd());

                load();

            } else {
                throw new Exception("Invalid HMd File");
            }
        } catch (Exception e) {
            ExceptionAlerter.showException(e);
        }
    }

    @FXML
    private void onSaveButton(ActionEvent event) {
        this.save();
        showUpdateInLabel("Saved.");
        this.load();
    }

    @FXML
    private void onReloadButton(ActionEvent event) {
        this.load();
        showUpdateInLabel("Reloaded.", 2000, statusLabel);
    }

    @FXML
    private void onSaveAndExit(ActionEvent event) {
        this.save();
        Stage stage = (Stage) this.getScene().getWindow();
        stage.hide();
    }

    //For this control, temporary status update
    private void showUpdateInLabel(String update) {
        showUpdateInLabel(update, 2000, statusLabel);
    }

    //Show temporary status update
    private void showUpdateInLabel(String update, int mseconds, Label status) {

        String oldMsg = status.getText();
        status.setText(update);

        TimedUpdaterUtil.callAfter(mseconds, new CallbackVisitor() {
            @Override
            public void call() {
                status.setText(oldMsg);
            }
        });

    }

    private void save() {
        this.hMdFile.setFrontMatter(tomlEditorControl.getTomlString());
        this.hMdFile.setPostContent(markdownEditorControl.getMdText());
        hMdFile.writeHMdFile();
    }

    private void load() {
//        System.out.println("Read toml");
        tomlEditorControl.setTomlString(this.hMdFile.getFrontMatter());
//        System.out.println("toml done");

//        System.out.println("Read md postcontent");
        markdownEditorControl.setMdText(this.hMdFile.getPostContent());
//        System.out.println("reading postcontent done");
    }

    public String getFileName() {
        return hMdFile.getFileName();
    }

}