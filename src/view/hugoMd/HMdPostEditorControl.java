package view.hugoMd;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import model.hugo.HMdFileProcessor;
import view.errorhandling.ExceptionAlerter;
import view.markdown.MarkdownEditorControl;
import view.toml.TomlEditorControl;

import java.io.IOException;

/**
 * Created by Sohan Chowdhury on 8/15/16.
 * Website: sohanchy.com
 * Email: sifat3d@gmail.com
 */
public class HMdPostEditorControl extends AnchorPane {
    @FXML
    private TomlEditorControl tomlEditorControl;
    @FXML
    private MarkdownEditorControl markdownEditorControl;
    @FXML
    private ToolBar rightToolBar;

    @FXML
    private Button saveButton;
    @FXML
    private Label statusLabel;


    private HMdFileProcessor hMdFile;

    public HMdPostEditorControl() {
        bindFxml();
    }

    public HMdPostEditorControl(HMdFileProcessor hMdFile) {
        this();
        this.hMdFile = hMdFile;
        setHMdFile(hMdFile);
    }

    private void bindFxml() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HMdPostEditor.fxml"));
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

    public void setHMdFile(HMdFileProcessor hMdFile) {
        this.hMdFile = hMdFile;
        setupEditors();
    }

    private void setupEditors() {

        try {
            if (hMdFile.isValidMd()) {
                System.out.println("isValid: " + hMdFile.isValidMd());

                this.tomlEditorControl.setTomlString(hMdFile.getFrontMatter());
                this.markdownEditorControl.setMdText(hMdFile.getPostContent());
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

    //For this control, temporary status update
    private void showUpdateInLabel(String update) {
        showUpdateInLabel(update, 2000, statusLabel);
    }

    //Show temporary status update
    private void showUpdateInLabel(String update, int mseconds, Label status) {

        String oldMsg = status.getText();
        status.setText(update);

        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(mseconds);
                } catch (InterruptedException e) {
                    ExceptionAlerter.showException(e);
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                status.setText(oldMsg);
            }
        });
        new Thread(sleeper).start();

    }

    private void save() {
        this.hMdFile.setFrontMatter(tomlEditorControl.getTomlString());
        this.hMdFile.setPostContent(markdownEditorControl.getMdText());
        hMdFile.writeHMdFile();
    }

    private void load() {
        tomlEditorControl.setTomlString(this.hMdFile.getFrontMatter());
        markdownEditorControl.setMdText(this.hMdFile.getPostContent());
    }

    public String getFileName() {
        return hMdFile.getFileName();
    }

}
