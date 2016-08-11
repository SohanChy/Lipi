package model.utility;

import java.util.*;
import com.moandjiezana.toml.*;

public class TomlParser{
    
    public static final String TOML_IDENTIFIER = "+++";

    private String toml;
    private Map<String, Object> tomlMap;
    private Map<String, String> tomlMapStr;

    public TomlParser(String toml){
        this.toml = toml;
    }

    public void readToml(String toml){
        this.toml = toml;
        readToml();
    }
        
    public boolean readToml(){
        tomlMap = new Toml().read(toml).toMap();
        return buildTomlMapStr();
    }

    private boolean buildTomlMapStr(){
        tomlMapStr = new HashMap<String, String>();
        
        if(tomlMap != null){
            for(Map.Entry<String, Object> entry : tomlMap.entrySet()) {
                    String key = entry.getKey();
                    String val = convertToPlainString(key, entry.getValue());
                    tomlMapStr.put(key,val);
                    }
                    return true;
        }
        else return false;
    }

    private static String convertToPlainString(String key,Object ob){
            
                try{
                        if(ob instanceof String){
                                return (String)ob;
                        }
                        else if(ob instanceof List){
                        
                            List<?> list = (List<?>)ob;
                            StringBuilder builder = new StringBuilder();      
                            
                            for (Object obStr : list){
                                if(obStr instanceof String){
                                        String str = (String) obStr;
                                        builder.append(str + ", ");
                                    }
                                else{
                                        throw new Exception( 
                                        "Array with non String content for key" 
                                        + key + "\n Datatype :" 
                                        + obStr.getClass().getName() );
                                    }
                            }
                            
                            String plainVals = builder.toString();
                            plainVals = plainVals.substring( 0, plainVals.length()-2 );
                            
                            return plainVals;
                        }
                        else {
                                throw new Exception( "Toml contains invalid contents for key " 
                                + key + "\n Datatype :" + ob.getClass().getName() );
                        }
                } catch(Exception e){
                        System.out.println(e.getMessage());
                }
            return null;
        }

    public Map<String, String> getTomlMapStr(){
                return this.tomlMapStr;
        }
}

