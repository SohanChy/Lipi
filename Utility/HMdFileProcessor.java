import java.io.*;
import java.lang.*;
import java.util.*;
import java.nio.charset.*;
import java.nio.file.*;

public class HMdFileProcessor{
    private String frontMatter,postContent;
    private String filePath;
    private File mdFile;
    
    public HMdFileProcessor(String filePath){
        this.filePath = filePath;
        setupFile(this.filePath);
    }
    
    private void setupFile(String filepath){
        this.filePath = filePath;
        //System.out.println(filePath);
        this.mdFile = new File(filePath);
    }
    
    public String getFrontMatter(){
        return frontMatter;
    }

    public String getPostContent(){
        return postContent;
    }
    
    public void setFrontMatter(String frontMatter){
        this.frontMatter = frontMatter.trim() + "\n";
    }

    public void setPostContent(String postContent){
        this.postContent = postContent.trim() + "\n";
    }
    
    public boolean readHMdFile(){

        try{
            BufferedReader br = new BufferedReader(
            new InputStreamReader(
                        new FileInputStream(mdFile), StandardCharsets.UTF_8));
                        
            
            String line = br.readLine();
            if(!line.contains(TomlParser.TOML_IDENTIFIER)){
                throw new Exception("no.toml.frontmatter");
            }
            
            frontMatter = postContent = "";
            for (line = br.readLine(); 
                    line != null && !line.contains(TomlParser.TOML_IDENTIFIER); 
                        line = br.readLine()) {
                    
                frontMatter += (line + "\n"); 
            }
            
            if(line == null){
                throw new Exception("no.toml.frontmatter");
            }
            
            frontMatter = frontMatter.trim() + "\n";
            
            for(line = br.readLine(); 
                    line != null; 
                        line = br.readLine()){
                        
                postContent += (line + "\n"); 
            }
            
            postContent = postContent.trim() + "\n";
            
            return true;
            
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }


    }
    
    public boolean writeHMdFile(){
        String combinedMdText = TomlParser.TOML_IDENTIFIER + "\n"
            + frontMatter + TomlParser.TOML_IDENTIFIER + "\n \n"
            + postContent; 
            
            try{
                FileHandler.writeFile(combinedMdText,mdFile);
                return true;
                }
                catch(IOException e){
                    e.printStackTrace();
                    return false;
                }
    }
    
    public static void main(String[] args){
    
        HMdFileProcessor mdObject = new HMdFileProcessor("test.md");
        
        
        mdObject.readHMdFile();
        mdObject.setPostContent((mdObject.getPostContent() + "-----TEST-----"));
        
        mdObject.writeHMdFile();
        
//         System.out.println(mdObject.getFrontMatter());
//         System.out.println(mdObject.getPostContent());
    
    }
    

}
