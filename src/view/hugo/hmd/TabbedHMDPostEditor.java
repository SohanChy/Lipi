package view.hugo.hmd;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import model.hugo.HMDFileCreator;
import model.hugo.HMDFileProcessor;
import view.utils.ExceptionAlerter;
import view.utils.TakeTwoInputsDialog;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;

/**
 * Created by Sohan Chowdhury on 8/26/16.
 * Website: sohanchy.com
 * Email: sifat3d@gmail.com
 */
public class TabbedHMDPostEditor extends TabPane {

    public String blogDir;
    private Stage editorStage;

    public TabbedHMDPostEditor(Stage editorStage) {
        bindFxml();
        this.editorStage = editorStage;
    }

    //Utils
    public static void createNewPostAndOpen(TabbedHMDPostEditor tabbedHMDPostEditor, String targetDir) {

        TakeTwoInputsDialog createPostDialog = new TakeTwoInputsDialog("Create New Post", "Just enter a title to proceed");

        createPostDialog.setupDialog("Title", "Enter a title here", "Description", "Enter a Description here");

        if (createPostDialog.showDialog()) {
            String genFilename = toPrettyURL(createPostDialog.getFirstInput()) + ".md";
            String genFilePath = targetDir + File.separator + genFilename;

            HMDFileCreator newPost = new HMDFileCreator(genFilePath);
            newPost.setupAndMakeFile(createPostDialog.getFirstInput(), createPostDialog.getSecondInput().replaceAll("(\\r|\\n|\\r\\n)+", "\\\\n "), "");

            tabbedHMDPostEditor.addTab(new HMDFileProcessor(genFilePath));
        }
    }

    public static void createNewPostAndOpen(TabbedHMDPostEditor tabbedHMDPostEditor, File target) {
        try {
            String targetDir = target.getCanonicalPath();

            createNewPostAndOpen(tabbedHMDPostEditor, targetDir);
        } catch (IOException e) {
            ExceptionAlerter.showException(e);
        }
    }

    public static String toPrettyURL(String string) {
        return Normalizer.normalize(string.toLowerCase().trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("[^\\p{Alnum}]+", "-");
    }

    private void bindFxml() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TabbedHMDPostEditor.fxml"));
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

    public void addTab(HMDFileProcessor hMdFile) {

        hMdFile.readHMdFile();
        HMDPostEditorControl hMDEditor = new HMDPostEditorControl(hMdFile, this);

//        hMDEditor.setTabbedHMDPostEditor(this);

        this.getTabs().add(0, new Tab(hMDEditor.getFileName(), hMDEditor));

        this.getSelectionModel().selectFirst();

        editorStage.show();
        editorStage.toFront();
    }

    public void removeCurrentTab() {
        int selectedIndex = this.getSelectionModel().getSelectedIndex();
        this.getTabs().remove(selectedIndex);

        editorStage.hide();
    }

}
