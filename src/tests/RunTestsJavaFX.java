package tests;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.hugo.HMdFileProcessor;
import view.hugoMd.HMdPostEditorControl;
import view.markdown.MarkdownEditorControl;
import view.toml.TomlEditorControl;

import java.util.ArrayList;

public class RunTestsJavaFX extends Application {
    private static final String testsFolder = "tests-res";

    String inp;
    Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    public static ArrayList<Node> getAllNodes(Parent root) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        addAllDescendents(root, nodes);
        return nodes;
    }

    private static void addAllDescendents(Parent parent, ArrayList<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent)
                addAllDescendents((Parent) node, nodes);
        }
    }

    private void readyGui(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        readyGui(primaryStage);


//       testTomlEditor();
//        testMdEditor();
        testHMdPostEditor();


        primaryStage.show();
    }

    public void testTomlEditor() {
        //inp = testsFolder + "/tomlConfigTest.toml";
        inp = testsFolder + "/tomlConfigTest.toml";


        HMdFileProcessor mdObject = new HMdFileProcessor(testsFolder + "/test.md");
        mdObject.readHMdFile();


//        TomlParser tomlParser = new TomlString(mdObject.getFrontMatter());

        TomlEditorControl tomlEditor = new TomlEditorControl(mdObject.getFrontMatter());

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
        sp.getStylesheets().add(getClass().getResource("material.css").toExternalForm());

        primaryStage.setTitle("Toml Editor");
        primaryStage.setScene(new Scene(sp));
    }

    public void testMdEditor() {

        MarkdownEditorControl mdEditor = new MarkdownEditorControl();

        Pane p = new Pane(mdEditor);

        p.getStylesheets().add(getClass().getResource("material.css").toExternalForm());

        primaryStage.setTitle("Markdown Editor");
        primaryStage.setScene(new Scene(p));
    }

    public void testHMdPostEditor() {

        HMdFileProcessor mdObject = new HMdFileProcessor(testsFolder + "/test.md");
        mdObject.readHMdFile();
        HMdPostEditorControl hMDEditor = new HMdPostEditorControl(mdObject);
        TabPane p = new TabPane();
        p.getTabs().add(0, new Tab(hMDEditor.getFileName(), hMDEditor));

        mdObject = new HMdFileProcessor(testsFolder + "/tomlParserTest.md");
        mdObject.readHMdFile();
        hMDEditor = new HMdPostEditorControl(mdObject);
        p.getTabs().add(1, new Tab(hMDEditor.getFileName(), hMDEditor));

        p.getStylesheets().add(getClass().getResource("material.css").toExternalForm());
        p.getStylesheets().add(getClass().getResource("custom.css").toExternalForm());

        primaryStage.setTitle("Post Editor");
        primaryStage.setScene(new Scene(p));
    }

}