package model.utility;

import com.moandjiezana.toml.*;

public class TomlParser{
    
    public static final String TOML_IDENTIFIER = "+++";

    private Map<String, Object> tomlMap;
    private Map<String, String> tomlMapStr;

    public void readToml(String toml){

		tomlMap = new Toml().read("toml").toMap();
	}

	private void buildTomlMapStr(){
		for (Map.Entry<String, Object> entry : tomlMap.entrySet()) {
			String key = entry.getKey();
		     convertToPlainString(key, entry.getValue());


			}
	}

	private static String convertToPlainString(String key,Object ob){
		
		try{
			if(ob instanceof String){
				return String;
			}
			else if(ob instanceof String[]){
				
				StringBuilder builder = new StringBuilder();
				for(String s : arr) {
	    			builder.append(s + ", ");
				}
				return builder.toString();
			}

			else {
				throw new Exception("Toml contains invalid contents for key " + key);
			}
		} catch(Exception e){
			System.out.println(e.getMessage());
		}

	}

	public Map<String, String> getTomlMapStr(){
		return this.tomlMapStr;
	}
}

