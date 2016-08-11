import model.hugo.*;
import model.utility.*;
import java.lang.Thread;
import java.util.*;

public class RunTests{
    public static final String testsFolder = "tests";

    public static void main(String[] args){
    
        System.out.println("This is the class to run tests on features.");
        
//         testHugo();
//            testHMdFileProcessor();

        testTomlParser();

    }

    public static void testTomlParser(){

        String inp = testsFolder + "/tomlParserTest.md";

        HMdFileProcessor mdObject = new HMdFileProcessor(inp);    
        mdObject.readHMdFile();
        
        TomlParser test = new TomlParser(mdObject.getFrontMatter());
        test.readToml();
        
        Map<String, String> testTomlMap = test.getTomlMapStr();
        if(testTomlMap == null){
            System.out.println("NULLZ");
        }
        else
            for(Map.Entry<String, String> entry : testTomlMap.entrySet()) {
                    System.out.println(entry.getKey() + " : " + entry.getValue());
                    }
    }    

    public static void testHugo(){
    
        Hugo testing = new Hugo("/home/sohan/Sandbox/Java/blog");
        testing.startHugoServer();
        
        try{
        Thread.sleep(3000);
        }
        catch (Exception e){
        }
        
        System.out.println("Slept 3000 ms");
        testing.stopHugoServer();
        
        //System.out.println(testing.getHugoOut());
    }
    
    
    public static void testHMdFileProcessor(){
    
        HMdFileProcessor mdObject = new HMdFileProcessor(testsFolder + "/test.md");
        
        
        mdObject.readHMdFile();
        //mdObject.setPostContent((mdObject.getPostContent() + "-----TEST-----"));
        
        mdObject.writeHMdFile(true);
        
//         System.out.println(mdObject.getFrontMatter());
//         System.out.println(mdObject.getPostContent());
    
    }
    
    public static void testIpc(){
            
            Ipc pandoc = new Ipc("pandoc");
            
            List<String> params = new ArrayList<String>();
            
            params.add("test.html");
            params.add("-o");
            params.add("test.md");
            
            pandoc.setProgArgs(params); 
            
            pandoc.runProc();
    }
    
    public static void testPandoc(){
        Pandoc PandocParser = new Pandoc();
        
        //System.out.println( PandocParser.htmlToMd( FileHandler.readFile("test.html") ) );
        System.out.println( PandocParser.htmlToMd( FileHandler.readFile(testsFolder + "/test.html") ) );
    }

}
