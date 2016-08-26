package view.hugo.pane;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import model.hugo.HMDFileCreator;
import model.hugo.HMDFileProcessor;
import model.hugo.Hugo;
import model.utility.CallbackVisitor;
import model.utility.FileHandler;
import view.hugo.hmd.TabbedHMDPostEditor;
import view.utils.ExceptionAlerter;
import view.utils.TakeTwoInputsDialog;
import view.utils.TimedUpdaterUtil;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Sohan Chowdhury on 8/21/16.
 * Website: sohanchy.com
 * Email: sifat3d@gmail.com
 */
public class HugoPane extends Accordion {

    private String hugoSiteDirPath, hugoBuildOutputDirPath, hugoSiteContentPath;

    private boolean liveServerRunning = false;
    private Hugo hugoServer;
    private TabbedHMDPostEditor tabbedHMDPostEditor;

    @FXML
    private TitledPane blogTitledPane, prefTitledPane, contentTitledPane;
    @FXML
    private Button liveBlogServerToggleButton, buildBlogButton;
    @FXML
    private Hyperlink openBlogInBrowserButton;
    @FXML
    private Label buildStatusLabel;

    @FXML
    private ChoiceBox<DirChoiceWrapper> writeContentTypeChoiceBox;
    @FXML
    private Button createContentTypeButton;
    @FXML
    private TextField contentTypeTextField;


    public HugoPane() {
        super();
        bindFxml();

        setupGui();
    }

    public static String toPrettyURL(String string) {
        return Normalizer.normalize(string.toLowerCase().trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("[^\\p{Alnum}]+", "-");
    }

    private void setupGui() {
        this.setExpandedPane(contentTitledPane);

        //Disable Content folder create button if empty
        contentTypeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            createContentTypeButton.setDisable(newValue.trim().isEmpty());
        });
    }

    private void bindFxml() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hugo_pane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            System.out.println("Failed to load fxml" + e.getMessage());
            ExceptionAlerter.showException(e);
            throw new RuntimeException(e);
        }

        this.getStyleClass().add("hugo-pane");
    }

    public void setup(String hugoSiteDirPath, TabbedHMDPostEditor tabbedHMDPostEditor) {
        setHugoSiteDirPath(hugoSiteDirPath);

        this.tabbedHMDPostEditor = tabbedHMDPostEditor;
    }

    public void setHugoSiteDirPath(String hugoSiteDirPath) {
        this.hugoSiteDirPath = hugoSiteDirPath;
        this.hugoSiteContentPath = hugoSiteDirPath + File.separator + "content";
        buildContentTypesList();
    }

    @FXML
    private void onLiveBlogServerToggle() {
        toggleLiveBlogServer();
        updateBlogServerToggleButton();

        TimedUpdaterUtil.callAfter(new CallbackVisitor() {
            @Override
            public void call() {
                if (!openBlogInBrowserButton.isDisabled() && !liveServerRunning) {
                    updateBlogServerToggleButton();
                    ExceptionAlerter.showException(new Exception("Hugo faced a catastrophic error \n" +
                            hugoServer.getHugoOut()));
                }
            }
        });
    }

    private void updateBlogServerToggleButton() {
        if (!liveServerRunning) {
            liveBlogServerToggleButton.setText("Start Live Blog Server");
            liveBlogServerToggleButton.setStyle("-fx-Background-Color: -fx-lightest-color;");
            openBlogInBrowserButton.setDisable(true);
        } else {
            liveBlogServerToggleButton.setText("Stop Live Blog Server");
            liveBlogServerToggleButton.setStyle("-fx-Background-Color: #1E88E5;");

            openBlogInBrowserButton.setDisable(false);
        }
    }

    @FXML
    private void onOpenBlogInBrowserButton() {
        HostServicesProviderUtil.INSTANCE.getHostServices().showDocument("http://localhost:1313");
    }

    private void toggleLiveBlogServer() {
        if (!liveServerRunning) {
            hugoServer = new Hugo(hugoSiteDirPath);

            hugoServer.startHugoServer(new CallbackVisitor() {
                @Override
                public void call() {
                    stopLiveBlogServer();
                }
            });

            liveServerRunning = hugoServer.isServerAlive();

        } else {
            stopLiveBlogServer();
        }


    }

    public void stopLiveBlogServer() {
        if (liveServerRunning) {
            hugoServer.stopHugoServer();
            liveServerRunning = !liveServerRunning;
        }
    }

    @FXML
    private void onBuildBlog() {
        buildBlogButton.setDisable(true);
        buildStatusLabel.setText("Build Status: Building...");
        Hugo hugoBuilder = new Hugo(hugoSiteDirPath);

        hugoBuilder.runHugoBuild(new CallbackVisitor() {
            @Override
            public void call() {
                buildStatusLabel.setText("Build Status: Done");
                buildBlogButton.setDisable(false);
            }
        });
    }

    private void buildContentTypesList() {
        File[] dirList = new File(hugoSiteContentPath).listFiles(File::isDirectory);

        List<DirChoiceWrapper> wrappedDirList = new ArrayList<DirChoiceWrapper>();

        assert dirList != null;

        Arrays.sort(dirList, new Comparator<File>() {
            public int compare(File f1, File f2) {
                return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
            }
        });

        for (File f : dirList) {
            wrappedDirList.add(new DirChoiceWrapper(f));
        }
        writeContentTypeChoiceBox.getItems().clear();
        writeContentTypeChoiceBox.getItems().addAll(wrappedDirList);
        writeContentTypeChoiceBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void onWriteButton() {
        String selectedDir = writeContentTypeChoiceBox.getValue().getDirPath();

        TakeTwoInputsDialog createPostDialog = new TakeTwoInputsDialog("Create New Post", "Just enter a title to proceed");

        createPostDialog.setupDialog("Title", "Enter a title here", "Description", "Enter a Description here");

        if (createPostDialog.showDialog()) {
            String genFilename = toPrettyURL(createPostDialog.getFirstInput()) + ".md";
            String genFilePath = selectedDir + File.separator + genFilename;

            HMDFileCreator newPost = new HMDFileCreator(genFilePath);
            newPost.setupAndMakeFile(createPostDialog.getFirstInput(), createPostDialog.getSecondInput(), "");

            tabbedHMDPostEditor.addTab(new HMDFileProcessor(genFilePath));
        }
    }

    @FXML
    private void onCreateType() {
        String dirPath = hugoSiteContentPath + File.separator + toPrettyURL(contentTypeTextField.getText());

        String oldText = createContentTypeButton.getText();
        if (FileHandler.makeDir(dirPath)) {
            contentHasChanged();

            createContentTypeButton.setText("Done!");
        } else {
            createContentTypeButton.setText("Failed!");
        }

        TimedUpdaterUtil.callAfter(new CallbackVisitor() {
            @Override
            public void call() {
                createContentTypeButton.setText(oldText);
            }
        });
    }

    public void contentHasChanged() {
        //Update UI here
        buildContentTypesList();
    }

    public void onPreferenceEditor() {

    }

    private class DirChoiceWrapper {
        private File dir;

        DirChoiceWrapper(File dir) {
            this.dir = dir;
        }

        @Override
        public String toString() {
            return dir.getName();
        }

        public File getDir() {
            return this.dir;
        }

        public String getDirPath() {
            return this.dir.getPath();
        }
    }

}
