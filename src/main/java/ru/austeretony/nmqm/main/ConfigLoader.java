package ru.austeretony.nmqm.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cpw.mods.fml.relauncher.FMLInjectionData;

public class ConfigLoader {
	
	public static String latestContainer = "";
	
	public static final Set<String> CONTAINERS = new HashSet<String>();
 
	private static boolean 
	checkForUpdates, 
	useExternalConfig, 
	enableDebugMode;
	
	private static int mode;

	public static void loadConfiguration() {
		
        try {       
        	
        	InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream("assets/nmqm/nmqm.json");
        	        	
            JsonObject internalConfig = (JsonObject) new JsonParser().parse(new InputStreamReader(inputStream, "UTF-8"));  
            
            inputStream.close();   
                                    
            useExternalConfig = internalConfig.get("main").getAsJsonObject().get("external_config").getAsBoolean();   
            
            if (!useExternalConfig)           	
            	loadData(internalConfig);
            else           	
            	loadExternalConfig(internalConfig);
        }
        
        catch (IOException exception) {
        	
        	exception.printStackTrace();
        }
    }
    
	private static void loadExternalConfig(JsonObject internalConfig) {

		String 
		gameDirPath = ((File) (FMLInjectionData.data()[6])).getAbsolutePath(),
		configPath = gameDirPath + "/config/nmqm/nmqm.json";

		Path path = Paths.get(configPath);
		
		if (Files.exists(path)) {

        	try {
        		
				InputStream inputStream = new FileInputStream(new File(configPath));
				
	            JsonObject externalConfig = (JsonObject) new JsonParser().parse(new InputStreamReader(inputStream, "UTF-8"));  
				
				inputStream.close();
				
            	loadData(externalConfig);
			}
        	
        	catch (IOException exception) {
        		
        		exception.printStackTrace();
			} 	
		}
		
		else {
			
            try {
            	
				Files.createDirectories(path.getParent());
				
				createExternalCopyAndLoad(configPath, internalConfig);												
			} 
            
            catch (IOException exception) {
            	
            	exception.printStackTrace();
			}			
		}
	}
	
	private static void createExternalCopyAndLoad(String configPath, JsonObject internalConfig) {
    	
        try {
        	
        	InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream("assets/nmqm/nmqm.json");
        	        	        	
			List<String> configData = IOUtils.readLines(new InputStreamReader(inputStream, "UTF-8"));
			
			inputStream.close();
						
            PrintStream fileStream = new PrintStream(new File(configPath));
            
            for (String line : configData)           	
            	fileStream.println(line);
            
            fileStream.close();
                    	        				            			
        	loadData(internalConfig);
		} 
        
        catch (IOException exception) {
        	
        	exception.printStackTrace();
		}
    }
	
	private static void loadData(JsonObject configFile) {
		
        JsonObject mainSettings = configFile.get("main").getAsJsonObject();
        
        enableDebugMode = mainSettings.get("debug_mode").getAsBoolean();      
        checkForUpdates = mainSettings.get("update_checker").getAsBoolean();	
        
        mode = mainSettings.get("mode").getAsInt();				
                
    	for (JsonElement element : configFile.get("containers").getAsJsonArray())       		
    		CONTAINERS.add(element.getAsString());
	}
	
	public static boolean isUpdateCheckerEnabled() {
		
		return checkForUpdates;
	}
	
	public static boolean isExternalConfigEnabled() {
		
		return useExternalConfig;
	}
	
	public static boolean isDebugModeEnabled() {
		
		return enableDebugMode;
	}
	
	public static int getMode() {
		
		return mode;
	}
}
