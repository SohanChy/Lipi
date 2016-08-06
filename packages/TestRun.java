import hugo.*;
import utility.*;
import java.lang.Thread;

public class TestRun{

    public static void main(String[] args){
    
        Hugo testing = new Hugo("/home/sohan/Sandbox/Java/blog");
        testing.startHugoServer();
        
        try{
        Thread.sleep(3000);
        }
        catch (Exception e){
        }
        
        System.out.println("here");
        testing.stopHugoServer();
        
        //System.out.println(testing.getHugoOut());
    }

}
    
