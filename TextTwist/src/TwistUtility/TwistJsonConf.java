package TwistUtility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.json.simple.*;
import org.json.simple.parser.*;

public class TwistJsonConf {

	private JSONObject container ;
	private JSONParser parser ;
	private String filename ;
    public TwistJsonConf(String filename)
    {
    	if( filename == null ) throw new NullPointerException();
    	
    	this.filename = filename ;
    	parser = new JSONParser();
    	try {
    		container = (JSONObject) parser.parse(new FileReader(filename));
		} catch (FileNotFoundException e) {
			System.out.println("[Exception] File not found: " + filename);
		    e.printStackTrace();
		} catch (IOException e) {
			System.out.println("[Exception] IO Exception on file: " + filename);
		    e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("[Exception] Parse Exception on file: " + filename);
		    e.printStackTrace();
		}	
	}

    public String getString(String key)
    {
		String ret = null ;
		ret = (String) container.get(key);	  
		return ret ;
    }
    public Long getLong(String key)
    {
		Long ret = null ;
		ret = (Long) container.get(key);	  
		return ret ;
    }

    @SuppressWarnings("unchecked")
	public void setString(String key, String value)
    {
        try {  
            
            File file=new File(filename);  
            file.createNewFile();  
            FileWriter fileWriter = new FileWriter(file);
            container.put(key, value);
            fileWriter.write(container.toJSONString());  
            fileWriter.flush();  
            fileWriter.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }
    
    @SuppressWarnings("unchecked")
	public void setLong(String key, Long value)
    {
        try {  
            
            File file=new File(filename);  
            file.createNewFile();  
            FileWriter fileWriter = new FileWriter(file);
            container.put(key, value);
            fileWriter.write(container.toJSONString());  
            fileWriter.flush();  
            fileWriter.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }
    
    @SuppressWarnings("rawtypes")
	public HashMap getContent()
    {
    	return (HashMap) this.container.clone();
    }
    
}



















