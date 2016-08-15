package view.errorhandling;

import javafx.scene.control.Alert;

/**
 * Created by Sohan Chowdhury on 8/15/16.
 * Website: sohanchy.com
 * Email: sifat3d@gmail.com
 */
public class ExceptionAlerter {
    public static void showException(Exception e) {

        if (e != null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "IOException: \n" + e.getMessage() +
                    "\n (Stacktrace printed to stdout)");

            System.out.println("From Alert:" + e.getMessage());
            e.printStackTrace();
            alert.showAndWait();
        }
    }
}
