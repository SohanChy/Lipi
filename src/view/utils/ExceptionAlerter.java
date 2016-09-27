package view.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Sohan Chowdhury on 8/15/16.
 * Website: sohanchy.com
 * Email: sifat3d@gmail.com
 */
public class ExceptionAlerter {
    public static void showException(Exception e) {

        if (e != null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Exception Alert!");
            alert.setHeaderText("An Exception was thrown.");
            alert.setContentText(e.getMessage());

// Create expandable Exception.
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String exceptionText = sw.toString();

            Label stackTraceHeaderLabel = new Label("The exception stacktrace was:");

            TextArea stackTraceTextArea = new TextArea(exceptionText);
            stackTraceTextArea.setEditable(false);
            stackTraceTextArea.setWrapText(true);

            stackTraceTextArea.setMaxWidth(Double.MAX_VALUE);
            stackTraceTextArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(stackTraceTextArea, Priority.ALWAYS);
            GridPane.setHgrow(stackTraceTextArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(stackTraceHeaderLabel, 0, 0);
            expContent.add(stackTraceTextArea, 0, 1);

// Set expandable Exception into the dialog pane.
            alert.getDialogPane().setExpandableContent(expContent);
            alert.getDialogPane().setExpanded(true);

            alert.showAndWait();
        }
    }
}
