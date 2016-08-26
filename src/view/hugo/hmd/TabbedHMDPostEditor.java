package view.hugo.hmd;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import model.hugo.HMDFileProcessor;
import view.utils.ExceptionAlerter;

import java.io.IOException;

/**
 * Created by Sohan Chowdhury on 8/26/16.
 * Website: sohanchy.com
 * Email: sifat3d@gmail.com
 */
public class TabbedHMDPostEditor extends TabPane {

    private Stage editorStage;

    public TabbedHMDPostEditor(Stage editorStage) {
        bindFxml();
        this.editorStage = editorStage;
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
        HMDPostEditorControl hMDEditor = new HMDPostEditorControl(hMdFile);

        this.getTabs().add(0, new Tab(hMDEditor.getFileName(), hMDEditor));

        this.getSelectionModel().selectFirst();

        editorStage.show();
        editorStage.toFront();
    }

}
