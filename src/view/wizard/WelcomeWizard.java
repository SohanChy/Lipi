package view.wizard;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import model.toml.TomlConfig;
import view.dashboard.DashboardMain;
import view.hugo.hmd.TabbedHMDPostEditor;
import view.utils.ExceptionAlerter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.prefs.Preferences;

/**
 * Created by Sohan Chowdhury on 8/27/16.
 * Website: sohanchy.com
 * Email: sifat3d@gmail.com
 */
public class WelcomeWizard extends GridPane {

    Stage primaryStage;
    @FXML
    CheckBox autoOpenCheckBox;
    @FXML
    private ListView recentBlogsList;

    public WelcomeWizard(Stage primaryStage) {
        this.primaryStage = primaryStage;
        bindFxml();

        loadHistory();
    }

    public static void openDirBlog(File blogDir, Stage primaryStage) {
        if (blogDir != null) {
            try {
                primaryStage.close();
                String selectedDirPath = blogDir.getCanonicalPath();

                //history is saved
                WelcomeWizard.storeBlogHistory(selectedDirPath);

                TomlConfig tomlConfig = new TomlConfig(selectedDirPath + File.separator + "config.toml");

                primaryStage.setTitle("Blog Dashboard: " + tomlConfig.getTomlMap().get("Title").toString() + " - Lipi");

                Stage editorStage = new Stage();
                editorStage.setTitle("Lipi Post Editor");
                TabbedHMDPostEditor t = new TabbedHMDPostEditor(editorStage);
                t.getStylesheets().add(Paths.get("res/material.css").toAbsolutePath().toUri().toString());
                t.getStylesheets().add(Paths.get("res/custom.css").toAbsolutePath().toUri().toString());
                editorStage.setScene(new Scene(t));

                editorStage.getIcons().add(
                        new Image(Paths.get("res/lipi-hmdeditor-icon.png").toAbsolutePath().toUri().toString())
                );

                DashboardMain mainDashboard = new DashboardMain(selectedDirPath, t);

                VBox holder = new VBox();
//                holder.setMinHeight(680);
                holder.setMinWidth(1000);
                holder.getChildren().add(mainDashboard);

                holder.getStylesheets().add(Paths.get("res/material.css").toAbsolutePath().toUri().toString());
                holder.getStylesheets().add(Paths.get("res/custom.css").toAbsolutePath().toUri().toString());

                Scene scene = new Scene(holder);

                primaryStage.setScene(scene);

//                primaryStage.setMinHeight(680);
                primaryStage.setMinWidth(1000);

                primaryStage.show();
                primaryStage.centerOnScreen();


            } catch (IOException e) {
                ExceptionAlerter.showException(e);
                e.getMessage();
            }
        }
    }

    public static void storeBlogHistory(String blogDir) {
        // Retrieve the user preference node for the package com.mycompany
        Preferences prefs = Preferences.userNodeForPackage(view.wizard.WelcomeWizard.class);

        // Set the value of the preference
        prefs.put("blog_history_1", blogDir);
    }

    private void loadHistory() {

        String blogHistoryDir = getBlogHistory();

        if (blogHistoryDir != null) {
            Path p = Paths.get(blogHistoryDir);
            String file = p.getFileName().toString();

            recentBlogsList.getItems().add(file);
            recentBlogsList.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    openDirBlog(new File(blogHistoryDir), primaryStage);
                }
            });

            String autoOpen = Preferences.userNodeForPackage(view.wizard.WelcomeWizard.class).get("auto_open", "false");
            if (autoOpen.equals("true")) {
                Platform.runLater(() -> {
                    openDirBlog(new File(blogHistoryDir), primaryStage);
                });
            }

        }
    }

    @FXML
    private void autoOpenClicked() {

        if (autoOpenCheckBox.isSelected()) {
            Preferences.userNodeForPackage(view.wizard.WelcomeWizard.class).put("auto_open", "true");
        } else {
            Preferences.userNodeForPackage(view.wizard.WelcomeWizard.class).put("auto_open", "false");
        }
    }

    private void bindFxml() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("welcome_wizard.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            System.out.println("Failed to load fxml" + e.getMessage());
            ExceptionAlerter.showException(e);
            throw new RuntimeException(e);
        }

        this.getStyleClass().add("welcome-wizard");
    }

    @FXML
    private void onCreateNewBlog() {
        primaryStage.setScene(new Scene(new BasicConfig(primaryStage)));
    }

    @FXML
    private void onOpenOldBlog() {
        DirectoryChooser blogDirChooser = new DirectoryChooser();
        blogDirChooser.setTitle("Select your blog folder");

        File selectedDirectory = blogDirChooser.showDialog(this.getScene().getWindow());

        WelcomeWizard.openDirBlog(selectedDirectory, primaryStage);
    }

    private String getBlogHistory() {
        // Retrieve the user preference node for the package com.mycompany
        Preferences prefs = Preferences.userNodeForPackage(view.wizard.WelcomeWizard.class);

        // Get the value of the preference;
        // default value is returned if the preference does not exist
        String defaultValue = null;

        String propertyValue = prefs.get("blog_history_1", defaultValue);

        return propertyValue;

    }
}
