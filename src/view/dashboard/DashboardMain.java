package view.dashboard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import view.filetree.FileTreeTable;
import view.hugo.hmd.TabbedHMDPostEditor;
import view.hugo.pane.HugoPane;
import view.utils.ExceptionAlerter;

import java.io.IOException;

/**
 * Created by Sohan Chowdhury on 8/21/16.
 * Website: sohanchy.com
 * Email: sifat3d@gmail.com
 */
public class DashboardMain extends BorderPane {
    private final String hugoBlogRootDirPath;
    TabbedHMDPostEditor tabbedHMDPostEditor;
    @FXML
    private HugoPane hugoPaneComponent;
    @FXML
    private FileTreeTable fileTreeTableComponent;

    public DashboardMain(String hugoBlogRootDirPath, TabbedHMDPostEditor tabbedHMDPostEditor) {
        super();
        bindFxml();

        this.tabbedHMDPostEditor = tabbedHMDPostEditor;
        this.hugoBlogRootDirPath = hugoBlogRootDirPath;

        initComponents();
    }

    public void initComponents() {
        fileTreeTableComponent.setup(hugoBlogRootDirPath, tabbedHMDPostEditor);
        hugoPaneComponent.setup(hugoBlogRootDirPath, tabbedHMDPostEditor, fileTreeTableComponent);
        this.tabbedHMDPostEditor.blogDir = hugoBlogRootDirPath;

    }

    private void bindFxml() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dashboard_main.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            System.out.println("Failed to load fxml" + e.getMessage());
            ExceptionAlerter.showException(e);
            throw new RuntimeException(e);
        }
    }
}
