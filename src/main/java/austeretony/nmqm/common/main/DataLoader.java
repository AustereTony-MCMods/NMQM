package austeretony.nmqm.common.main;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import austeretony.nmqm.common.reference.CommonReference;
import austeretony.nmqm.util.NMQMUtils;

public class DataLoader {

    private static final String 
    EXT_CONFIGURAION_FILE = CommonReference.getGameFolder() + "/config/nmqm/nmqm_config.json",
    EXT_CONTAINERS_FILE = CommonReference.getGameFolder() + "/config/nmqm/nmqm_containers.json";

    private static final DateFormat BACKUP_DATE_FORMAT = new SimpleDateFormat("yy_MM_dd-HH-mm-ss");

    public static String latestContainer = "";

    public static final Set<String>
    EMPTY_SET = new HashSet<String>(),
    CONTAINERS_SERVER = new HashSet<String>(),
    CONTAINERS_CLIENT = new HashSet<String>();

    private static boolean 
    showUpdateMessages, 
    useExternalConfig, 
    enableClientSync,
    isSettingsDisabled,
    isConfigModeEnabled;

    private static int mode;

    public static void load() {		
        JsonObject internalConfig, internalContainers;
        try {       
            internalConfig = (JsonObject) NMQMUtils.getInternalJsonData("assets/nmqm/nmqm_config.json");  
            internalContainers = (JsonObject) NMQMUtils.getInternalJsonData("assets/nmqm/nmqm_containers.json");  
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
                externalConfig = (JsonObject) NMQMUtils.getExternalJsonData(configFolder);	
                externalContainers = (JsonObject) NMQMUtils.getExternalJsonData(containersFolder);	
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
            NMQMUtils.createExternalJsonFile(EXT_CONFIGURAION_FILE, internalConfig); 
            NMQMUtils.createExternalJsonFile(EXT_CONTAINERS_FILE, internalContainers);                    	        				            			
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

    public static void saveSettings() {
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

    public static void backupSettings() {
        try {
            NMQMUtils.createExternalJsonFile(
                    CommonReference.getGameFolder() + "/config/nmqm/nmqm_containers_" + BACKUP_DATE_FORMAT.format(new Date()) + ".json", 
                    NMQMUtils.getExternalJsonData(EXT_CONTAINERS_FILE));         
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

    public static boolean isSettingsDisabled() {
        return isSettingsDisabled;
    }

    public static void setSettingsDisabled(boolean isEnabled) {
        isSettingsDisabled = isEnabled;
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
