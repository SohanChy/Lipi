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
    
    public String htmltoMd(String html){
    
        String md = null;
        try{
            String outFile = FileHandler.getExpiringTempFile().getCanonicalPath();
            
            htmltoMdFile(html,outFile);
            
            md = FileHandler.readFile(outFile);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return md;
    }
    
    public boolean htmltoMdFile(String html,String outFile){
        
        try{
            File tmpHtml = FileHandler.stringToTempFile(html);
            
            List<String> params = new ArrayList<String>();
            params.addAll(Arrays.asList(paramHTMLtoMD));
            
            //System.out.println(tmpHtml.getCanonicalPath());
            
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
    
    
    
    public static void main(String[] args){
        Pandoc PandocParser = new Pandoc();
        
        PandocParser.htmltoMdFile(FileHandler.readFile("test.html"),"test.md");
        
        System.out.println("DONE");
    }
    
}
    
