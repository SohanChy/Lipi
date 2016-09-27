package view.filetree;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import model.hugo.HMDFileProcessor;
import model.toml.TomlString;
import model.utility.MarkdownFileUtils;
import view.hugo.hmd.TabbedHMDPostEditor;
import view.utils.ExceptionAlerter;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;


public class FileTreeTable extends AnchorPane {

    private final static NumberFormat NumberFormater = NumberFormat.getIntegerInstance();
    private final static DateFormat DateFormatter = DateFormat.getDateTimeInstance();
    private final ContextMenu rightClickContextMenu;
    private String hugoBlogRootDirPath, hugoBlogContentDirPath;
    private TabbedHMDPostEditor tabbedHMDPostEditor;
    //FXML
    @FXML
    private TreeTableView<File> treeTableView;
    @FXML
    private TreeTableColumn nameCol, titleCol, lastModCol;

    public FileTreeTable() {
        super();
        bindFxml();
        rightClickContextMenu = new ContextMenu();
        buildContextMenu();
    }


    public FileTreeTable(String hugoBlogRootDirPath, TabbedHMDPostEditor tabbedHMDPostEditor) {
        this();
        setup(hugoBlogRootDirPath, tabbedHMDPostEditor);
    }

    private void bindFxml() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("file_tree_table.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            System.out.println("Failed to load fxml" + e.getMessage());
            ExceptionAlerter.showException(e);
            throw new RuntimeException(e);
        }

        this.getStyleClass().add("file-tree-table");
    }

    public void setup(String hugoBlogRootDirPath, TabbedHMDPostEditor tabbedHMDPostEditor) {

//        buildRightClickContextMenu();

        this.hugoBlogRootDirPath = hugoBlogRootDirPath;
        this.hugoBlogContentDirPath = this.hugoBlogRootDirPath + File.separator + "content";
        this.tabbedHMDPostEditor = tabbedHMDPostEditor;
        prepare();
    }

    private void prepare() {
        buildFileBrowserTreeTableView(treeTableView);

        final PseudoClass firstRowClass = PseudoClass.getPseudoClass("first-row");

        treeTableView.setRowFactory(treeTable -> {
            TreeTableRow<File> row = new TreeTableRow<File>();
            row.treeItemProperty().addListener((ov, oldTreeItem, newTreeItem) ->
                    row.pseudoClassStateChanged(firstRowClass, newTreeItem == treeTable.getRoot()));
            row.setContextMenu(rightClickContextMenu);
            return row;
        });

        treeTableView.setOnMouseClicked(
                new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    openSelectedTreeitemInHmdEditor();
                }

            }
        });

//        this.getChildren().add(treeTableView);
    }

    private void openSelectedTreeitemInHmdEditor() {
        TreeItem<File> file = treeTableView.getSelectionModel().getSelectedItem();

        if (file != null) {
            if (!file.getValue().isDirectory() && file.getValue().getName().endsWith(".md")) {
                try {
                    System.out.println(file.getValue());
                    openHMdPostEditor(file.getValue().getCanonicalPath());
                } catch (IOException e) {
                    ExceptionAlerter.showException(e);
                }
            }
        }
    }


    private void buildFileBrowserTreeTableView(TreeTableView<File> treeTableView) {
        TreeItem<File> root = createNode(new File(hugoBlogContentDirPath));
        root.setExpanded(true);

        treeTableView.setRoot(root);

        nameCol.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<File, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<File, String> p) {
                        File f = p.getValue().getValue();
                        String text = (f.getParentFile() == null) ? "/" : f.getName();
                        return new ReadOnlyObjectWrapper<String>(text);
                    }
                });

        nameCol.setCellFactory(new Callback<TreeTableColumn<File, String>, TreeTableCell<File, String>>() {
            @Override
            public TreeTableCell<File, String> call(TreeTableColumn<File, String> param) {
                return new TreeTableCell<File, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

             /*           if(getFileExtension(item).equals("md")){
                            setContextMenu(rightClickContextMenu);
                        }*/

                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item);
                        }
                    }
                };
            }

        });

        titleCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<File, File>, ObservableValue<File>>() {
            @Override
            public ObservableValue<File> call(TreeTableColumn.CellDataFeatures<File, File> p) {
                return new ReadOnlyObjectWrapper<File>(p.getValue().getValue());
            }
        });
        titleCol.setCellFactory(new Callback<TreeTableColumn<File, File>, TreeTableCell<File, File>>() {
            @Override
            public TreeTableCell<File, File> call(final TreeTableColumn<File, File> p) {
                return new TreeTableCell<File, File>() {

                    @Override
                    protected void updateItem(File item, boolean empty) {
                        super.updateItem(item, empty);

                        TreeTableView<File> treeTable = p.getTreeTableView();

                        TreeItem<File> treeItem = treeTable.getTreeItem(getIndex());
                        if (item == null || empty || treeItem == null ||
                                treeItem.getValue() == null || treeItem.getValue().isDirectory()) {
                            setText(null);
                        } else if (getFileExtension(item).equals("md")) {
                            try {
                                HMDFileProcessor hmd = new HMDFileProcessor(item.getCanonicalPath());
                                hmd.readHMdFile();

//                                setContextMenu(rightClickContextMenu);

                                if (hmd.isValidMd()) {
                                    TomlString ts = new TomlString(hmd.getFrontMatter());
                                    String postTitle = ts.getTomlMap().get("title").toString();

                                    if (postTitle != null) {
                                        setText(postTitle);
                                    } else {
                                        throw new Exception("TOML without any TITLE attribute.");
                                    }
                                }
                            } catch (Exception e) {
                                //ExceptionAlerter.showException(e);
                            }
                        } else {
                            setText(getFileExtension(item));
                        }
                    }
                };
            }
        });
        titleCol.setComparator(new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                long s1 = f1.isDirectory() ? 0 : f1.length();
                long s2 = f2.isDirectory() ? 0 : f2.length();
                long result = s1 - s2;
                if (result < 0) {
                    return -1;
                } else if (result == 0) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });

        // --- modified column
        lastModCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<File, Date>, ObservableValue<Date>>() {
            @Override
            public ObservableValue<Date> call(TreeTableColumn.CellDataFeatures<File, Date> p) {
                if (!p.getValue().getValue().isDirectory()) {
                    return new ReadOnlyObjectWrapper<Date>(new Date(p.getValue().getValue().lastModified()));
                } else return null;
            }
        });
        lastModCol.setCellFactory(new Callback<TreeTableColumn<File, Date>, TreeTableCell<File, Date>>() {
            @Override
            public TreeTableCell<File, Date> call(TreeTableColumn<File, Date> p) {
                return new TreeTableCell<File, Date>() {
                    @Override
                    protected void updateItem(Date item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(DateFormatter.format(item));
//                            setContextMenu(rightClickContextMenu);
                        }
                    }
                };
            }
        });

//        treeTableView.getColumns().setAll(nameCol, titleCol, lastModCol);
    }

    private TreeItem<File> createNode(final File f) {

        final TreeItem<File> node = new TreeItem<File>(f) {
            private boolean isLeaf;
            private boolean isFirstTimeChildren = true;
            private boolean isFirstTimeLeaf = true;

            @Override
            public ObservableList<TreeItem<File>> getChildren() {
                if (isFirstTimeChildren) {
                    isFirstTimeChildren = false;
                    super.getChildren().setAll(buildChildren(this));
                }
                return super.getChildren();
            }

            @Override
            public boolean isLeaf() {
                if (isFirstTimeLeaf) {
                    isFirstTimeLeaf = false;
                    File f = getValue();
                    isLeaf = f.isFile();
                }

                return isLeaf;
            }
        };

        return node;
    }

    private ObservableList<TreeItem<File>> buildChildren(TreeItem<File> TreeItem) {
        File f = TreeItem.getValue();
        if (f != null && f.isDirectory()) {

            File[] files = f.listFiles(MarkdownFileUtils.getMdFileFilter());
            if (files != null) {
                ObservableList<TreeItem<File>> children = FXCollections.observableArrayList();

                for (File childFile : files) {
                    javafx.scene.control.TreeItem<File> node = createNode(childFile);
                    node.setExpanded(true);
                    children.add(node);
                }

                return children;
            }
        }

        return FXCollections.emptyObservableList();
    }


    private void openHMdPostEditor(String file) {

        HMDFileProcessor selectedMdFile = new HMDFileProcessor(file);
        tabbedHMDPostEditor.addTab(selectedMdFile);

    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        return getFileExtension(fileName);
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }

    private void buildContextMenu() {

        MenuItem edit = new MenuItem("Edit");
        edit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                openSelectedTreeitemInHmdEditor();
            }
        });

        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                File file = treeTableView.getSelectionModel().getSelectedItem().getValue();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Delete");
                alert.setHeaderText("Are you sure?");

                if (!file.isDirectory()) {
                    alert.setContentText("Click YES to delete " + file.getName());
                } else {
                    alert.setContentText("This is a folder, IT MUST BE EMPTY TO DELETE!!! :" + file.getName());
                }


                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {

                    if (!file.delete()) {
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Delete FAILED");
                        alert.setHeaderText(null);
                        alert.setContentText("You tried to delete " + file.getName() + ".\n"
                                + "but the Delete operation FAILED!\n" +
                                "Check error/exception for details."
                        );
                        alert.showAndWait();
                    }

                    buildFileBrowserTreeTableView(treeTableView);
                }
            }
        });
        rightClickContextMenu.getItems().addAll(edit, delete);

        rightClickContextMenu.setOnShowing(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent e) {
                File file = treeTableView.getSelectionModel().getSelectedItem().getValue();

                if (file.isDirectory()) {
                    edit.setText("New Post");
                    edit.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent e) {
                            File file = treeTableView.getSelectionModel().getSelectedItem().getValue();

                            TabbedHMDPostEditor.createNewPostAndOpen(tabbedHMDPostEditor, file);
                            reloadTree();
                        }
                    });

                }
            }
        });
    }

    public void reloadTree() {
        buildFileBrowserTreeTableView(treeTableView);
    }

}
