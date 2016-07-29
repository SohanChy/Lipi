import java.io.*;
import java.lang.*;
import java.util.*;

public class Pandoc{
    
    private Ipc pandoc;
    private String[] params;
    private static String[] paramHTMLtoMD = {"-f","html","-t","markdown"};
    
    Pandoc(){
        pandoc = new Ipc("pandoc");
        
    }
    
    public boolean htmltoMd(String html,String outFile){
        
        try{
            File tmpHtml = stringToTempFile(html);
            
            List<String> params = new ArrayList<String>();
            params.addAll(Arrays.asList(paramHTMLtoMD));
            
            System.out.println(tmpHtml.getCanonicalPath());
            
            params.add(tmpHtml.getCanonicalPath());
            params.add("-o");
            params.add(outFile);
            
            pandoc.setProgArgs(params); 
            
            pandoc.runProc();
            
            return true;
           }
            catch (Exception e){
                e.printStackTrace();
            }
            
        return false;
    }
    
    private File stringToTempFile(String textString){
        File f = null;
        try
        { 
            f = getTempFile();
            
            Writer fWriter = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(f), "UTF-8"));
            try {
                fWriter.write(textString);
            } finally {
                fWriter.close();
            }
            
            System.out.println(textString);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return f;
    }
    
    private File getTempFile() throws IOException{
        File f = File.createTempFile("JavaFX_pd_", ".tmp");
        //f.deleteOnExit();
        return f;
    }
    
    public static void main(String[] args){
        Pandoc PandocParser = new Pandoc();
        
        PandocParser.htmltoMd("<h1>Hi</h1><hr><p>aaa</p>","mdtest.md");
    }
    
}
    
