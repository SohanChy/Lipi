//package HugoFx;
 
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.Parent;
import javafx.stage.Stage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
 
public class HugoFx extends Application {
    
    @Override
    public void start(Stage mainStage) throws Exception{
	    
	Parent root = FXMLLoader.load(getClass().getResource("design/main_workspace.fxml"));
    
        Scene scene = new Scene(root);
	    
	mainStage.setTitle("HugoFx");
	mainStage.setScene(scene);
	mainStage.show();    
	    
        }
    
 public static void main(String[] args) {
        launch(args);
    }
}
 
