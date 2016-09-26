package model.hugo;

import model.utility.CallbackVisitor;
import model.utility.Ipc;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Hugo {
    private final static String[] paramServer = {"server"};
    private final static String[] paramNewSite = {"new", "site"};
    private final Ipc hugoIpc;
    private final String srcDir;
    private String pubDir;
    private List<String> params;
    private Thread serverThread;

    public Hugo(String srcDir) {
        this.srcDir = srcDir;
        pubDir = srcDir + File.separator + "public";
        //         System.out.println(pubDir);
        hugoIpc = new Ipc("exec" + File.separator + "hugo");
    }

    public String getSrcDir() {
        return srcDir;
    }

    public String getPubDir() {
        return pubDir;
    }

    public void setPubDir(String pubDir) {
        this.pubDir = pubDir;
    }

    public void hugoMakeSite(String blogName) {
        params = new ArrayList<String>();
        params.addAll(Arrays.asList(paramNewSite));
        params.add(blogName);

        hugoIpc.setProgArgs(params);
        hugoIpc.runProc();
        hugoIpc.showOutput();

    }

    protected void hugoBuild() {
        params = new ArrayList<String>();
        params.add("--source");
        params.add(srcDir);
        params.add("--destination");
        params.add(pubDir);

        try {
            FileUtils.deleteDirectory(new File(pubDir));
        } catch (Exception e) {
            System.out.println(e.getMessage() + "Maybe no previous public folder exists.");
        }

        hugoIpc.setProgArgs(params);
        hugoIpc.runProc();
        hugoIpc.showOutput();
    }

    //do not call this without a separate thread
    private void runHugoServer(CallbackVisitor callback) {

        System.out.println("Server src: " + srcDir);

        params = new ArrayList<String>();
        params.add("server");

        params.add("--source");
        params.add(srcDir);

        hugoIpc.setProgArgs(params);
        hugoIpc.runProc();
        hugoIpc.showOutput();
        callback.call();
    }

    public void runHugoBuild(CallbackVisitor callBack) {
        HugoThread hugoBuilderThread = new HugoThread(this) {
            public void run() {
                hugo.hugoBuild();
            }
        };
        hugoBuilderThread.start();
        callBack.call();
    }

    public boolean startHugoServer(CallbackVisitor callback) {
        if (serverThread == null) {
            serverThread = new HugoThread(this) {
                public void run() {
                    hugo.runHugoServer(callback);
                }
            };

            serverThread.start();
            return true;
        }
        return false;
    }

    public boolean isServerAlive() {
        return (serverThread.getState() != Thread.State.TERMINATED);
    }

    public boolean stopHugoServer() {
        if (serverThread != null) {
            hugoIpc.destroy();
            serverThread = null;
            return true;
        }

        return false;
    }

    public String getHugoOut() {
        return hugoIpc.stdOut;
    }

    private abstract class HugoThread extends Thread {
        public final Hugo hugo;

        private HugoThread(Hugo hugo) {
            this.hugo = hugo;
//            this.setDaemon(true);
        }

        public abstract void run();
    }

}
