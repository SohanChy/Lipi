package view.utils;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Labeled;
import model.utility.CallbackVisitor;

/**
 * Created by Sohan Chowdhury on 8/26/16.
 * Website: sohanchy.com
 * Email: sifat3d@gmail.com
 */
public class TimedUpdaterUtil {
    static final int DEFAULT_WAIT = 2000;

    public static void callAfter(CallbackVisitor callback) {
        callAfter(DEFAULT_WAIT, callback);
    }

    public static void callAfter(int mseconds, CallbackVisitor callback) {
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(mseconds);
                } catch (InterruptedException e) {
                    ExceptionAlerter.showException(e);
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                callback.call();
            }
        });
        new Thread(sleeper).start();
    }

    public static void temporaryLabeledUpdate(int mseconds, Labeled labeled, String newText) {
        String oldText = labeled.getText();
        labeled.setText(newText);

        callAfter(mseconds, new CallbackVisitor() {
            @Override
            public void call() {
                labeled.setText(oldText);
            }
        });
    }

    public static void temporaryLabeledUpdate(Labeled labeled, String newText) {
        temporaryLabeledUpdate(DEFAULT_WAIT, labeled, newText);
    }

}
