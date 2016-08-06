package utility;

import java.io.*;
import java.lang.*;
import java.util.*;
import java.io.File;

public class Pandoc{
    
    private Ipc pandoc;
    private List<String> params;
    private static String[] paramHTMLtoMD = {"-f","html","-t","markdown"};
    private static String[] paramMDtoHTML = {"-f","markdown","-t","html"};
    
    Pandoc(){
        pandoc = new Ipc("exec" + File.separator + "pandoc");
        
    }
    
    public String htmlToMd(String html){
    
        String md = null;
        try{
            String outFile = FileHandler.getExpiringTempFile().getCanonicalPath();
            
            htmlToMdFile(html,outFile);
            
            md = FileHandler.readFile(outFile);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return md;
    }
    
    public String mdToHtml(String md){
    
        String html = null;
        try{
            String outFile = FileHandler.getExpiringTempFile().getCanonicalPath();
            
            mdToHtmlFile(md,outFile);
            
            html = FileHandler.readFile(outFile);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return html;
    }
    
    private boolean htmlToMdFile(String html,String outFile){
        
        try{
            File tmpHtml = FileHandler.stringToTempFile(html);
            
            params = new ArrayList<String>();
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
    
    private boolean mdToHtmlFile(String md, String outFile){
    
        try{
            File tmpMd = FileHandler.stringToTempFile(md);
            
            params = new ArrayList<String>();
            params.addAll( Arrays.asList(paramMDtoHTML) );
            
            //System.out.println(tmpHtml.getCanonicalPath());
            
            params.add(tmpMd.getCanonicalPath());
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
        
        //System.out.println( PandocParser.htmlToMd( FileHandler.readFile("test.html") ) );
        System.out.println( PandocParser.mdToHtml( "HTML" + FileHandler.readFile("test.md") ) );
    }
    
}
    
