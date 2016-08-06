package hugo;

import utility.Ipc;
import java.io.File;
import java.util.*;
import java.lang.Thread;

public class Hugo{
    private Ipc hugo;
    private List<String> params;
    private final static String[] paramServer = {"server"};
    private final static String[] paramNewSite = {"new","site"};
    private String srcDir,pubDir;
    private Thread serverThread;
    
    
    public Hugo(String srcDir){
        this.srcDir = srcDir;
        pubDir = srcDir + File.separator + "public";
//         System.out.println(pubDir);
        hugo = new Ipc("exec" + File.separator + "hugo");
    }
    
    public void hugoBuild(){
        params = new ArrayList<String>();
        params.add("--source");
        params.add(srcDir);
        params.add("--destination");
        params.add(pubDir);
        
        hugo.setProgArgs(params);
        hugo.runProc();
        hugo.showOutput();
    }
    
    //do not call this without a seperate thread
    public void runHugoServer(){
    
        System.out.println("Server src" + srcDir);
    
        params = new ArrayList<String>();
        params.add("server");

        params.add("--source");
        params.add(srcDir);
        
        hugo.setProgArgs(params);
        hugo.runProc();
        hugo.waitFor();
        hugo.showOutput();
    }
    
    public boolean startHugoServer(){
        if(serverThread == null){
            serverThread = new hugoServerThread(this);
            serverThread.start();
            return true;
        }
        return false;
    }
    
    public boolean stopHugoServer(){
        if(serverThread != null){
            hugo.destroy();
            if(!serverThread.isInterrupted()){
                serverThread.interrupt();
            }
            serverThread = null;
        return true;
        }
        
    return false;
    }
    
    public String getHugoOut(){
            return hugo.stdOut;
    }
    
    private class hugoServerThread extends Thread{
        private Hugo hugo;
        
        private hugoServerThread(Hugo hugo){
                this.hugo = hugo;
        }
                    
        public void run(){
                hugo.runHugoServer();
        }
    }
    
}
