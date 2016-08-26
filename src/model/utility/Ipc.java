package model.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

//Give executable filename, add optional arguments/commands
//Call runProc()
public class Ipc {
    public String stdOut, stdErr;
    private String progName, execLoc;
    private List<String> command;
    private Process proc;
    private boolean isDestroyed;

    public Ipc(String progName) {

        setProgName(progName);
    }

    public void setProgName(String progName) {

        this.progName = progName;
        setProgArgs();
        setExecLoc();
    }

    public void setProgArgs() {

        command = new ArrayList<String>();
        command.add(execLoc);
    }

    public void setProgArgs(List<String> args) {

        setProgArgs();
        //Converts Space separated args to list then add to end of command
        command.addAll(args);
    }

    private void setExecLoc() {

        execLoc = System.getProperty("user.dir") + "/" + progName;

        //Check for executable file and adjust
        File f = new File(execLoc);

        if (!f.exists() || f.isDirectory()) {
//            System.out.println("Maybe its Windows?");
            execLoc = execLoc + ".exe";

            if (!f.exists() || f.isDirectory()) {
                System.out.println("File not found at all");
            }

        } else {
            //System.out.println("File found,Assuming Linux");
        }

    }

    public void runProc() {

        try {
            isDestroyed = false;
            proc = new ProcessBuilder().command(command).redirectErrorStream(true).start();
            saveOutput();
            proc.waitFor();
        } catch (Exception e) {
            isDestroyed = true;
            e.printStackTrace();
        }

    }

    public void showOutput() {

        System.out.println("Here is the standard output of the command:\n"
                + stdOut);

        System.out.println("Here is the standard error of the command (if any):\n"
                + stdErr);

    }

    public void saveOutput() {
        stdOut = stdErr = "_";
        try {
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(proc.getErrorStream()));

            String strTmp;
            // read the output from the command
            while ((strTmp = stdInput.readLine()) != null) {
                stdOut += ("_" + strTmp + "\n");

                if (isDestroyed) {
                    break;
                }
            }

            // read any errors from the attempted command
            if (!isDestroyed) {
                while ((strTmp = stdError.readLine()) != null) {
                    stdErr += ("_" + strTmp + "\n");

                    if (isDestroyed) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void destroy() {
        isDestroyed = true;
        proc.destroy();
    }

}
