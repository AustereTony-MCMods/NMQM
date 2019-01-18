package austeretony.nmqm.common.main;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import austeretony.nmqm.common.origin.CommonReference;
import austeretony.nmqm.util.NMQMUtil;

public class NMQMDataLoader {
	
	private static final String 
	EXT_CONFIGURAION_FILE = CommonReference.getGameFolder() + "/config/nmqm/nmqm_config.json",
	EXT_CONTAINERS_FILE = CommonReference.getGameFolder() + "/config/nmqm/nmqm_containers.json";
	
	public static String latestContainer = "";
	
	public static final Set<String> 
	CONTAINERS_SERVER = new LinkedHashSet<String>(),
	CONTAINERS_CLIENT = new HashSet<String>();
 
	private static boolean 
	showUpdateMessages, 
	useExternalConfig, 
	enableClientSync,
	isConfigModeEnabled;
	
	private static int mode;

	public static void load() {		
		JsonObject internalConfig, internalContainers;
        try {       
        	internalConfig = (JsonObject) NMQMUtil.getInternalJsonData("assets/nmqm/nmqm_config.json");  
        	internalContainers = (JsonObject) NMQMUtil.getInternalJsonData("assets/nmqm/nmqm_containers.json");  
        } catch (IOException exception) {  	
        	NMQMMain.LOGGER.error("Internal configuration files damaged!");
        	exception.printStackTrace();
        	return;
        }
        useExternalConfig = internalConfig.get("main").getAsJsonObject().get("external_config").getAsBoolean();          
        if (!useExternalConfig) {          	
        	loadConfigData(internalConfig, internalContainers);
        } else           	
        	loadExternalConfig(internalConfig, internalContainers);
    }
    
	private static void loadExternalConfig(JsonObject internalConfig, JsonObject internalContainers) {
		String 
		gameFolder = CommonReference.getGameFolder(),
		configFolder = gameFolder + "/config/nmqm/nmqm_config.json",
		containersFolder = gameFolder + "/config/nmqm/nmqm_containers.json";
		Path configPath = Paths.get(configFolder);	
		if (Files.exists(configPath)) {
			JsonObject externalConfig, externalContainers;
        	try {       		
        		externalConfig = (JsonObject) NMQMUtil.getExternalJsonData(configFolder);	
        		externalContainers = (JsonObject) NMQMUtil.getExternalJsonData(containersFolder);	
			} catch (IOException exception) {  
	        	NMQMMain.LOGGER.error("External configuration file damaged!");
        		exception.printStackTrace();
        		return;
			} 	
        	loadConfigData(externalConfig, externalContainers);
		} else {		
			Path containersPath = Paths.get(containersFolder);	
            try {            	
				Files.createDirectories(configPath.getParent());				
				Files.createDirectories(containersPath.getParent());				
				createExternalCopyAndLoad(internalConfig, internalContainers);	
			} catch (IOException exception) {           	
            	exception.printStackTrace();
			}			
		}
	}
	
	private static void createExternalCopyAndLoad(JsonObject internalConfig, JsonObject internalContainers) {  	
        try {        	
        	NMQMUtil.createAbsoluteFileCopy(EXT_CONFIGURAION_FILE, NMQMDataLoader.class.getClassLoader().getResourceAsStream("assets/nmqm/nmqm_config.json")); 
        	NMQMUtil.createAbsoluteFileCopy(EXT_CONTAINERS_FILE, NMQMDataLoader.class.getClassLoader().getResourceAsStream("assets/nmqm/nmqm_containers.json"));                    	        				            			
		} catch (IOException exception) {        	
        	exception.printStackTrace();
		}
    	loadConfigData(internalConfig, internalContainers);
    }
	
	private static void loadConfigData(JsonObject config, JsonObject containers) {		
        JsonObject mainSettings = config.get("main").getAsJsonObject();       
        enableClientSync = mainSettings.get("client_sync").getAsBoolean();	        
        showUpdateMessages = mainSettings.get("update_checker").getAsBoolean();	        
        mode = mainSettings.get("mode").getAsInt();		
    	for (JsonElement element : containers.get("containers").getAsJsonArray())       		
    		CONTAINERS_SERVER.add(element.getAsString());
	}
	
	public static void saveContainersData() {
        Gson gson = new GsonBuilder()
    			.setPrettyPrinting()
    			.create();
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        for (String s : CONTAINERS_SERVER)
        	jsonArray.add(new JsonPrimitive(s));
        jsonObject.add("containers", jsonArray);
        try (Writer writer = new FileWriter(EXT_CONTAINERS_FILE)) {        	
        	gson.toJson(jsonObject, writer);
        } catch (IOException exception) {
        	exception.printStackTrace();
		}
	}
	
	public static boolean isUpdateMessagesEnabled() {		
		return showUpdateMessages;
	}
	
	public static boolean isExternalConfigEnabled() {		
		return useExternalConfig;
	}
	
	public static boolean isConfigModeEnabled() {		
		return isConfigModeEnabled;
	}
	
	public static void setConfigModeEnabled(boolean isEnabled) {		
		isConfigModeEnabled = isEnabled;
	}
	
	public static boolean isClientSyncEnabled() {		
		return enableClientSync;
	}
	
	public static void setClientSyncEnabled(boolean isEnabled) {		
		enableClientSync = isEnabled;
	}
	
	public static int getMode() {		
		return mode;
	}
}
