package view.filetree;

/**
 * Created by Sohan Chowdhury on 8/21/16.
 * Website: sohanchy.com
 * Email: sifat3d@gmail.com
 */

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import model.hugo.HMDFileProcessor;
import model.toml.TomlString;
import view.hugo.hmd.TabbedHMDPostEditor;
import view.utils.ExceptionAlerter;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.Date;


public class FileTreeTable extends AnchorPane {

    private final static NumberFormat NumberFormater = NumberFormat.getIntegerInstance();
    private final static DateFormat DateFormater = DateFormat.getDateTimeInstance();
    String dirPath;
    private TreeTableView<File> treeTableView;
    private TabbedHMDPostEditor tabbedHMDPostEditor;

    //FXML
    @FXML
    private TreeTableColumn nameCol, titleCol, lastModCol;

    public FileTreeTable() {
        super();
        bindFxml();
    }


    public FileTreeTable(String dirPath, TabbedHMDPostEditor tabbedHMDPostEditor) {
        this();
        setup(dirPath, tabbedHMDPostEditor);
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
    }

    public void setup(String dirPath, TabbedHMDPostEditor tabbedHMDPostEditor) {
        this.dirPath = dirPath;
        this.tabbedHMDPostEditor = tabbedHMDPostEditor;
        prepare();
    }

    private void prepare() {
        treeTableView = new TreeTableView<>();
        buildFileBrowserTreeTableView(treeTableView);

        final PseudoClass firstRowClass = PseudoClass.getPseudoClass("first-row");

        treeTableView.setRowFactory(treeTable -> {
            TreeTableRow<File> row = new TreeTableRow<File>();
            row.treeItemProperty().addListener((ov, oldTreeItem, newTreeItem) ->
                    row.pseudoClassStateChanged(firstRowClass, newTreeItem == treeTable.getRoot()));
            return row;
        });

        treeTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    TreeItem<File> file = treeTableView.getSelectionModel().getSelectedItem();

                    if (!file.getValue().isDirectory()) {
                        try {
                            openHMdPostEditor(file.getValue().getCanonicalPath());
                        } catch (IOException e) {
                            ExceptionAlerter.showException(e);
                        }
                    }
                }
            }
        });


        this.getChildren().add(treeTableView);
    }


    private void buildFileBrowserTreeTableView(TreeTableView<File> treeTableView) {
        TreeItem<File> root = createNode(new File(dirPath));
        root.setExpanded(true);

        treeTableView.setRoot(root);

        // --- name column
        nameCol.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<File, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<File, String> p) {
                        File f = p.getValue().getValue();
                        String text = f.getParentFile() == null ? "/" : f.getName();
                        return new ReadOnlyObjectWrapper<String>(text);
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

                    private String getFileExtension(File file) {
                        String name = file.getName();
                        try {
                            return name.substring(name.lastIndexOf(".") + 1);
                        } catch (Exception e) {
                            return "";
                        }
                    }

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
                            setText(DateFormater.format(item));
                        }
                    }
                };
            }
        });

        treeTableView.getColumns().setAll(nameCol, titleCol, lastModCol);
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
            File[] files = f.listFiles();
            if (files != null) {
                ObservableList<TreeItem<File>> children = FXCollections.observableArrayList();

                for (File childFile : files) {
                    children.add(createNode(childFile));
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


}
