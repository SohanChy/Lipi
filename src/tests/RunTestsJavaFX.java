package tests;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.hugo.HMDFileProcessor;
import model.utility.FileHandler;
import view.dashboard.DashboardMain;
import view.filetree.FileTreeTable;
import view.hugo.hmd.TabbedHMDPostEditor;
import view.hugo.markdown.MarkdownEditorControl;
import view.hugo.pane.HostServicesProviderUtil;
import view.hugo.pane.HugoPane;
import view.toml.TomlConfigEditor;
import view.toml.TomlEditorControl;
import view.utils.ExceptionAlerter;
import view.wizard.WelcomeWizard;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class RunTestsJavaFX extends Application {
    private static final String testsFolder = "tests-res";
    private static final String testsBlog = testsFolder + File.separator + "blog";

    String inp;
    Stage primaryStage, editorStage;
    Pane pane;
    TabbedHMDPostEditor tabbedHMDPostEditor;

    public static void main(String[] args) {
        launch(args);
    }

    private void readyGui(Stage primaryStage) {
        this.primaryStage = primaryStage;
        editorStage = new Stage();

        tabbedHMDPostEditor = new TabbedHMDPostEditor(editorStage);
        editorStage.setScene(new Scene(tabbedHMDPostEditor));

        primaryStage.setTitle("Main Application");
        editorStage.setTitle("Hugo Markdown Editor");
        //Must do this for opening browser;
        HostServicesProviderUtil.INSTANCE.init(getHostServices());

        pane = new VBox();

        File f = new File("kalpurush.ttf");
        try {
            Font.loadFont(f.getCanonicalPath(), 10);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        pane.getStylesheets().add(Paths.get("res/material.css").toAbsolutePath().toUri().toString());
        pane.getStylesheets().add(Paths.get("res/custom.css").toAbsolutePath().toUri().toString());
//

        primaryStage.setScene(new Scene(pane));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        readyGui(primaryStage);


//      testTomlEditor();
//      testMdEditor();
//      testHMdPostEditor();
//      testFileTreeControl();
//      testHugoPane();
//      testTabbedHMdPostEditor();
//      testTomlConfigEditor();
        testDashboardMain();
//
//        wizard();

        primaryStage.show();
    }

    public void testTomlEditor() {
        //inp = testsFolder + "/tomlConfigTest.toml";
        inp = testsFolder + "/tomlConfigTest.toml";

        HMDFileProcessor mdObject = new HMDFileProcessor(testsFolder + "/test.md");
        mdObject.readHMdFile();


//        TomlParser tomlParser = new TomlString(mdObject.getFrontMatter());

        String tomlString;
        try {
            tomlString = FileHandler.readFile(inp);


            TomlEditorControl tomlEditor = new TomlEditorControl(tomlString);

            Button print = new Button("print");
            print.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    mdObject.setFrontMatter(tomlEditor.getTomlString());
                    System.out.println(mdObject.getFrontMatter());
                }
            });

            tomlEditor.getChildren().add(print);

            ScrollPane sp = new ScrollPane(tomlEditor);
            sp.setFitToHeight(true);

            pane.getChildren().add(sp);

            primaryStage.setTitle("Toml Editor");

        } catch (IOException e) {
            ExceptionAlerter.showException(e);
        }
    }

    public void testMdEditor() {

        MarkdownEditorControl mdEditor = new MarkdownEditorControl();

        pane.getChildren().add(new Pane(mdEditor));

        primaryStage.setTitle("Markdown Editor");
    }

//    public void testHMdPostEditor() {
//
//        HMDFileProcessor mdObject = new HMDFileProcessor(testsFolder + "/test.md");
//        mdObject.readHMdFile();
//        HMDPostEditorControl hMDEditor = new HMDPostEditorControl(mdObject,);
//        TabPane p = new TabPane();
//        p.getTabs().add(0, new Tab(hMDEditor.getFileName(), hMDEditor));
//
//        mdObject = new HMDFileProcessor(testsFolder + "/tomlParserTest.md");
//        mdObject.readHMdFile();
//        hMDEditor = new HMDPostEditorControl(mdObject,testsFolder);
//        p.getTabs().add(1, new Tab(hMDEditor.getFileName(), hMDEditor));
//
//        pane.getChildren().add(p);
//
//        primaryStage.setTitle("Post Editor");
//    }

    public void testFileTreeControl() {

        primaryStage.setTitle("FileTreeControl");

        FileTreeTable ft = new FileTreeTable(testsBlog, tabbedHMDPostEditor);

        Text txt = new Text("TEst teST স্ট্য্যাকওভারফ্লোর প্রোগ্রামিং");

        pane.getChildren().addAll(ft, txt);

//        ft.setFitToHeight(true);
    }

    public void testHugoPane() {

        primaryStage.setTitle("HugoPane");

        HugoPane hp = new HugoPane();
        hp.setup(testsBlog, tabbedHMDPostEditor);

        pane.getChildren().addAll(hp);

//        ft.setFitToHeight(true);
    }

    public void testTabbedHMdPostEditor() {
        HMDFileProcessor mdObject = new HMDFileProcessor(testsFolder + "/test.md");
        tabbedHMDPostEditor.addTab(mdObject);

    }


    public void testTomlConfigEditor() {
        inp = testsFolder + "/tomlConfigTest.toml";

        TomlConfigEditor tomlEditor = new TomlConfigEditor(inp);
        pane.getChildren().add(tomlEditor);


    }

    public void testDashboardMain() {

        primaryStage.setTitle("Blog Dashboard - Lipi");

        String blogFolder = "/media/sohan/Mediabox/Downloads/blog/";

        DashboardMain mainDashboard = new DashboardMain(blogFolder, tabbedHMDPostEditor);

        pane.getChildren().add(mainDashboard);

    }


    public void wizard() {

        primaryStage.setTitle("Lipi");

        WelcomeWizard welcome = new WelcomeWizard(primaryStage);

        pane.getChildren().add(welcome);

    }

}