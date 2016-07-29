import javafx.fxml.FXML; 
import javafx.scene.web.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;

public class HugoFxController extends BorderPane{
    @FXML private TreeView leftPaneTreeView;
    @FXML private WebView markDownEditorWebView;
    
    public HugoFxController throws IOException(){
        FXMLLoader fxmlLoader = new FXMLLoader(
            getClass().getResource("workspace.fxml"));
        
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        fxmlLoader.load();
        
        WebEngine webEngine = markDownEditorWebView.getEngine();
        markDownEditorWebView = 
        }

}
