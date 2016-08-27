package view.hugo.pane;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.hugo.Hugo;
import model.toml.TomlConfig;
import model.utility.CallbackVisitor;
import model.utility.FileHandler;
import view.hugo.hmd.TabbedHMDPostEditor;
import view.toml.TomlConfigEditor;
import view.utils.ExceptionAlerter;
import view.utils.TimedUpdaterUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Sohan Chowdhury on 8/21/16.
 * Website: sohanchy.com
 * Email: sifat3d@gmail.com
 */
public class HugoPane extends Accordion {

    //Config Editor
    @FXML
    private TextField confBlogTitleField, confBlogAuthorField, confBlogDisqusField;
    @FXML
    private ChoiceBox<DirChoiceWrapper> confBlogThemeVBox;
    @FXML
    private Button confBlogPrefSaveButton;

    //BLOG
    @FXML
    private Button liveBlogServerToggleButton, buildBlogButton;
    @FXML
    private Hyperlink openBlogInBrowserButton;
    @FXML
    private Label buildStatusLabel;

    //Content
    @FXML
    private ChoiceBox<DirChoiceWrapper> writeContentTypeChoiceBox;
    @FXML
    private Button createContentTypeButton;
    @FXML
    private TextField contentTypeTextField;

    //Main LOGIC
    private String hugoBlogRootDirPath, hugoBlogOutputDirPath, hugoBlogContentDirPath, hugoBlogThemesDirPath;
    private String hugoSiteConfigFilePath;

    private boolean liveServerRunning = false;
    private Hugo hugoServer;

    private TabbedHMDPostEditor tabbedHMDPostEditor;

    @FXML
    private TitledPane blogTitledPane, prefTitledPane, contentTitledPane;

    public HugoPane() {
        super();
        bindFxml();

        setupGui();
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
        setHugoSitePaths(hugoSiteDirPath);

        this.tabbedHMDPostEditor = tabbedHMDPostEditor;
    }

    public void setHugoSitePaths(String hugoSiteDirPath) {
        this.hugoBlogRootDirPath = hugoSiteDirPath;
        this.hugoBlogContentDirPath = hugoSiteDirPath + File.separator + "content";
        this.hugoBlogThemesDirPath = hugoSiteDirPath + File.separator + "themes";

        this.hugoSiteConfigFilePath = hugoSiteDirPath + File.separator + "config.toml";

        setupConfBlog();
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

        this.getScene().getWindow().setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                stopLiveBlogServer();
            }
        });
    }

    private void restartLiveBlogServerIfRunning() {
        if (liveServerRunning) {
            onLiveBlogServerToggle();
            TimedUpdaterUtil.callAfter(new CallbackVisitor() {
                @Override
                public void call() {
                    onLiveBlogServerToggle();
                }
            });
        }
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
            hugoServer = new Hugo(hugoBlogRootDirPath);

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
        Hugo hugoBuilder = new Hugo(hugoBlogRootDirPath);

        hugoBuilder.runHugoBuild(new CallbackVisitor() {
            @Override
            public void call() {
                buildStatusLabel.setText("Build Status: Done");
                buildBlogButton.setDisable(false);
            }
        });
    }

    private void buildContentTypesList() {

        writeContentTypeChoiceBox.getItems().clear();
        writeContentTypeChoiceBox.getItems().addAll(getDirChoiceWrapperList(hugoBlogContentDirPath));
        writeContentTypeChoiceBox.getSelectionModel().selectFirst();
    }

    public List<DirChoiceWrapper> getDirChoiceWrapperList(String dirContainerPath) {

        File[] dirList = new File(dirContainerPath).listFiles(File::isDirectory);

        List<DirChoiceWrapper> wrappedDirList = new ArrayList<DirChoiceWrapper>();

        if (dirList == null) throw new AssertionError();

        Arrays.sort(dirList, new Comparator<File>() {
            public int compare(File f1, File f2) {
                return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
            }
        });

        for (File f : dirList) {
            wrappedDirList.add(new DirChoiceWrapper(f));
        }

        return wrappedDirList;
    }

    @FXML
    private void onWriteButton() {
        String selectedDir = writeContentTypeChoiceBox.getValue().getDirPath();

        TabbedHMDPostEditor.createNewPostAndOpen(tabbedHMDPostEditor, selectedDir);
    }

    @FXML
    private void onCreateType() {
        String dirPath = hugoBlogContentDirPath + File.separator + TabbedHMDPostEditor.toPrettyURL(contentTypeTextField.getText());

        String oldText = createContentTypeButton.getText();
        if (FileHandler.makeDir(dirPath)) {
            contentHasChanged();

            TimedUpdaterUtil.temporaryLabeledUpdate(createContentTypeButton, "Done!");
        } else {
            TimedUpdaterUtil.temporaryLabeledUpdate(createContentTypeButton, "Failed!");
        }
    }

    @FXML
    private void onOpenAdvancedConfig() {
        Stage tomlConfigEditorStage = new Stage();

        tomlConfigEditorStage.setTitle("Site Preference Editor (TOML)");
        tomlConfigEditorStage.setScene(new Scene(new TomlConfigEditor(hugoSiteConfigFilePath)));

        tomlConfigEditorStage.showAndWait();
    }

    public void contentHasChanged() {
        //Update UI here
        buildContentTypesList();
    }

    private void setupConfBlog() {
        TomlConfig tomlConfig = new TomlConfig(hugoSiteConfigFilePath);

        confBlogTitleField.setText(tomlConfig.getTomlMap().get("Title").toString());

        setupConfBlogThemeVBox(tomlConfig);


        Map<String, Object> paramsMap = (Map<String, Object>) tomlConfig.getTomlMap().get("Params");

        String paramAuthor = (String) paramsMap.get("Author");
        String paramDisqus = (String) paramsMap.get("Disqus");

        confBlogAuthorField.setText(paramAuthor);
        confBlogDisqusField.setText(paramDisqus);

    }

    private void setupConfBlogThemeVBox(TomlConfig tomlConfig) {
        confBlogThemeVBox.getItems().clear();

        List<DirChoiceWrapper> dirChoiceWrapperList = getDirChoiceWrapperList(hugoBlogThemesDirPath);

        confBlogThemeVBox.getItems().addAll(dirChoiceWrapperList);

        //Select Current Theme
        String currentlySelectedTheme = tomlConfig.getTomlMap().get("theme").toString();

        int currentThemesIndex = dirChoiceWrapperList.indexOf(
                new DirChoiceWrapper(
                        new File(hugoBlogThemesDirPath + File.separator + currentlySelectedTheme
                        )
                )
        );

        if (currentThemesIndex > -1) {
            confBlogThemeVBox.getSelectionModel().select(currentThemesIndex);
        } else {
            ExceptionAlerter.showException(new Exception("Config file has theme that is not in theme directory."));
        }
    }

    @FXML
    private void onConfBlogPrefButton() {

        String userBlogTitle = confBlogTitleField.getText();
        String userBlogTheme = confBlogThemeVBox.getValue().getName();

        String userBlogParamAuthor = confBlogAuthorField.getText();
        String userBlogParamDisqus = confBlogDisqusField.getText();

        TomlConfig tomlConfig = new TomlConfig(hugoSiteConfigFilePath);

        Map<String, Object> mainTomlConfigMap = tomlConfig.getTomlMap();

        mainTomlConfigMap.put("Title", userBlogTitle);
        mainTomlConfigMap.put("theme", userBlogTheme);

        Map<String, Object> paramsMap = (Map<String, Object>) tomlConfig.getTomlMap().get("Params");

        paramsMap.put("Author", userBlogParamAuthor);
        paramsMap.put("Disqus", userBlogParamDisqus);

        mainTomlConfigMap.put("Params", paramsMap);

        tomlConfig.setTomlMap(mainTomlConfigMap);

        restartLiveBlogServerIfRunning();
        tomlConfig.writeTomlMap();

        TimedUpdaterUtil.temporaryLabeledUpdate(confBlogPrefSaveButton, "Saved.");
    }

    //For wrapping directory chosen
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

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof DirChoiceWrapper) {
                return this.getDir().equals(((DirChoiceWrapper) obj).getDir());
            }
            return super.equals(obj);
        }

        public String getName() {
            return this.dir.getName();
        }
    }

}
