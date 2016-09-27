package view.wizard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import model.hugo.Hugo;
import model.toml.TomlConfig;
import org.apache.commons.io.FileUtils;
import view.hugo.hmd.TabbedHMDPostEditor;
import view.utils.ExceptionAlerter;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by Sohan Chowdhury on 8/27/16.
 * Website: sohanchy.com
 * Email: sifat3d@gmail.com
 */
public class BasicConfig extends GridPane {

    public static final String hugoBlogThemesDirPath = "res/hugo-res/themes/";
    public static final String hugoBlogExampleBlogDirPath = "res/hugo-res/exampleBlog/";
    public static final String hugoDefaultConfigFilePath = "res/hugo-res/default-config.toml";


    @FXML
    private TextField confBlogTitleField, confBlogAuthorField;
    @FXML
    private ChoiceBox<DirChoiceWrapper> confThemeChoice;

    private Stage primaryStage;

    public BasicConfig(Stage primaryStage) {
        this.primaryStage = primaryStage;
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
        this.getStylesheets().add(Paths.get("res/material.css").toAbsolutePath().toUri().toString());
        this.getStylesheets().add(Paths.get("res/custom.css").toAbsolutePath().toUri().toString());
    }

    private void setupGui() {
        setupConfBlogThemeVBox();
    }

    public List<DirChoiceWrapper> getDirChoiceWrapperList(String dirContainerPath) {


        File[] dirList = (new File(dirContainerPath)).listFiles();

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

    private void setupConfBlogThemeVBox() {
        confThemeChoice.getItems().clear();

        List<DirChoiceWrapper> dirChoiceWrapperList = getDirChoiceWrapperList(hugoBlogThemesDirPath);

        confThemeChoice.getItems().addAll(dirChoiceWrapperList);

        confThemeChoice.getSelectionModel().selectLast();
    }

    @FXML
    private void onNextButton() {
        String confBlogNameText = confBlogTitleField.getText();
        String confBlogThemeText = confThemeChoice.getValue().getName();
        String confAuthorNameText = confBlogAuthorField.getText();

        DirectoryChooser blogDirChooser = new DirectoryChooser();
        blogDirChooser.setTitle("Select a folder to create your blog, Or maybe make a new one?");

        File selectedDirectory = blogDirChooser.showDialog(this.getScene().getWindow());

        if (selectedDirectory != null) {
            try {

                String selectedDirPath = selectedDirectory.getCanonicalPath();

                //history is saved
                WelcomeWizard.storeBlogHistory(selectedDirPath);


                //New site is created in program path!! FIX LATER
                String newSite = TabbedHMDPostEditor.toPrettyURL(confBlogNameText);
                new Hugo(selectedDirPath).hugoMakeSite(newSite);


                Path tempLoc = FileSystems.getDefault().getPath(newSite);

                System.out.println(tempLoc.toAbsolutePath());

                String newBlogsPath = selectedDirPath + File.separator + newSite;
                FileUtils.moveDirectory(new File(newSite), new File(newBlogsPath));

                //WRITE CONFIG
                TomlConfig tomlConfig = new TomlConfig(hugoDefaultConfigFilePath);

                Map<String, Object> mainTomlConfigMap = tomlConfig.getTomlMap();

                mainTomlConfigMap.put("Title", confBlogNameText);
                mainTomlConfigMap.put("theme", confBlogThemeText);

                Map<String, Object> paramsMap = (Map<String, Object>) tomlConfig.getTomlMap().get("Params");

                paramsMap.put("Author", confAuthorNameText);

                mainTomlConfigMap.put("Params", paramsMap);

                tomlConfig.setTomlMap(mainTomlConfigMap);

                TomlConfig newToml = new TomlConfig(newBlogsPath + File.separator + "config.toml");
                newToml.setTomlMap(tomlConfig.getTomlMap());
                newToml.writeTomlMap();


                FileUtils.copyDirectory(new File(hugoBlogThemesDirPath), new File(newBlogsPath + File.separator + "themes"));
                FileUtils.copyDirectory(new File(hugoBlogExampleBlogDirPath), new File(newBlogsPath));

                WelcomeWizard.openDirBlog(new File(newBlogsPath), primaryStage);


            } catch (IOException e) {
                ExceptionAlerter.showException(e);
                e.getMessage();
            }
        }


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
