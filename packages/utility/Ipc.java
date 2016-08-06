package utility;

import java.io.*;
import java.util.*;

//Give executable filename, add optional arguments/commands
//Call runProc()
public class Ipc{
    private String progName,execLoc;
    private List<String> command;
    private Process proc;
    public String stdOut,stdErr;
    
    Ipc(String progName){
    
        setProgName(progName);
    }
    
    public int waitFor() throws InterruptedException{
        return proc.waitFor();
    }
    
    public void setProgName(String progName){
    
        this.progName = progName;
        setProgArgs();
        setExecLoc();
    }
    
    public void setProgArgs(){
    
        command = new ArrayList<String>();
        command.add(execLoc);
    }
     
    public void setProgArgs(List<String> args){
    
        setProgArgs();
        //Converts Space separated args to list then add to end of command
        command.addAll(args);
    }
    
    private void setExecLoc(){
    
        execLoc = System.getProperty("user.dir") + "/" + progName;
    
        //Check for executable file and adjust
            File f = new File(execLoc);

            if(!f.exists() || f.isDirectory()){
                System.out.println("Maybe its Windows?");
                execLoc = execLoc + ".exe";
                
                if(!f.exists() || f.isDirectory()){
                    System.out.println("File not found at all");
                }
                
            }
            else{
                System.out.println("File found,Assuming Linux");
                }
                
    }
    
    public void runProc(){
    
        try{
            proc = new ProcessBuilder().command(command).redirectErrorStream(true).start();
            saveOutput();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            
    }
    
    public void showOutput(){
    saveOutput();
    
    System.out.println("Here is the standard output of the command:\n"
        + stdOut);
        
    System.out.println("Here is the standard error of the command (if any):\n"
        + stdErr);
      
    }
    
    public void saveOutput(){
        stdOut = stdErr = "Empty";
        try{
                BufferedReader stdInput = new BufferedReader(new 
                        InputStreamReader(proc.getInputStream()));

                BufferedReader stdError = new BufferedReader(new 
                        InputStreamReader(proc.getErrorStream()));

                // read the output from the command
                while (stdInput.readLine() != null){
                    stdOut += stdOut;
                }

                // read any errors from the attempted command
          
                while (stdError.readLine() != null){
                    stdErr += stdErr;
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
                
        }
        
    public static void main(String[] args){
            
            Ipc pandoc = new Ipc("pandoc");
            
            List<String> params = new ArrayList<String>();
            
            params.add("test.html");
            params.add("-o");
            params.add("test.md");
            
            pandoc.setProgArgs(params); 
            
            pandoc.runProc();
    }

}
