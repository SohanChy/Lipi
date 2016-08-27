package view.wizard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.toml.TomlConfig;
import view.utils.ExceptionAlerter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by Sohan Chowdhury on 8/27/16.
 * Website: sohanchy.com
 * Email: sifat3d@gmail.com
 */
public class BasicConfig extends GridPane {

    public static final String hugoBlogThemesDirPath = Paths.get("res/hugo-res/themes/").toAbsolutePath().toUri().toString();
    public static final String hugoDefaultConfigFilePath = Paths.get("res/hugo-res/default-config.toml").toAbsolutePath().toUri().toString();


    @FXML
    private TextField confBlogTitleField, confBlogAuthorField;
    @FXML
    private ChoiceBox<DirChoiceWrapper> confThemeChoice;

    public BasicConfig() {
        bindFxml();
        setupGui();
    }

    private void bindFxml() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("basic_config.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            System.out.println("Failed to load fxml" + e.getMessage());
            ExceptionAlerter.showException(e);
            throw new RuntimeException(e);
        }

        this.getStyleClass().add("basic-config");
    }

    private void setupGui() {
        setupConfBlogThemeVBox();
    }

    public List<DirChoiceWrapper> getDirChoiceWrapperList(String dirContainerPath) {

        File[] dirList = (new File(dirContainerPath)).listFiles();

        List<DirChoiceWrapper> wrappedDirList = new ArrayList<DirChoiceWrapper>();

        System.out.println(new File(dirContainerPath).getName());
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

    private void setupConfBlogThemeVBox() {
        confThemeChoice.getItems().clear();


        List<DirChoiceWrapper> dirChoiceWrapperList = getDirChoiceWrapperList(hugoBlogThemesDirPath);

        confThemeChoice.getItems().addAll(dirChoiceWrapperList);

        confThemeChoice.getSelectionModel().selectFirst();
    }

    private void afterDirIsChosen() {

    }

    private void configuration(String writePath) {
        String userBlogTitle = confBlogTitleField.getText();
        String userBlogTheme = confThemeChoice.getValue().getName();

        String userBlogParamAuthor = confBlogAuthorField.getText();

        TomlConfig tomlConfig = new TomlConfig(hugoDefaultConfigFilePath);

        Map<String, Object> mainTomlConfigMap = tomlConfig.getTomlMap();

        mainTomlConfigMap.put("Title", userBlogTitle);
        mainTomlConfigMap.put("theme", userBlogTheme);

        Map<String, Object> paramsMap = (Map<String, Object>) tomlConfig.getTomlMap().get("Params");

        paramsMap.put("Author", userBlogParamAuthor);

        mainTomlConfigMap.put("Params", paramsMap);

        tomlConfig.setTomlMap(mainTomlConfigMap);

        TomlConfig newToml = new TomlConfig(writePath);
        newToml.setTomlMap(tomlConfig.getTomlMap());
        newToml.writeTomlMap();

    }

    @FXML
    private void onNextButton() {
        String confBlogNameText = confBlogTitleField.getText();
        String confBlogThemeText = confThemeChoice.getValue().getName();
        String confAuthorNameText = confBlogAuthorField.getText();

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
