package view.utils;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import model.utility.CallbackVisitor;

/**
 * Created by Sohan Chowdhury on 8/26/16.
 * Website: sohanchy.com
 * Email: sifat3d@gmail.com
 */
public class TimedUpdaterUtil {
    static final int defaultWait = 2000;

    public static void callAfter(CallbackVisitor callback) {
        callAfter(defaultWait, callback);
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

}
