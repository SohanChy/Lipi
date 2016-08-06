import hugo.*;
import utility.*;

public class TestRun{

    public static void main(String[] args){
    
    
    
        System.out.println( System.getProperty("user.dir") );
        HMdFileProcessor mdObject = new HMdFileProcessor("test.md");
        
        
        mdObject.readHMdFile();
        mdObject.setPostContent((mdObject.getPostContent() + "-----TEST-----"));
        
        mdObject.writeHMdFile();
        
         System.out.println(mdObject.getFrontMatter());
         System.out.println(mdObject.getPostContent());
    
    }

}
    
