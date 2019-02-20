package austeretony.nmqm.common.config;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import austeretony.nmqm.common.main.NMQMMain;
import austeretony.nmqm.common.main.UpdateChecker;
import austeretony.nmqm.common.reference.CommonReference;
import austeretony.nmqm.util.NMQMUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ConfigLoader {

    private static final String 
    EXT_CONFIGURATION_FILE = CommonReference.getGameFolder() + "/config/nmqm/config.json",
    EXT_CONTAINERS_FILE = CommonReference.getGameFolder() + "/config/nmqm/containers.json";

    private static final DateFormat BACKUP_DATE_FORMAT = new SimpleDateFormat("yy_MM_dd-HH-mm-ss");

    public static String latestContainer = "";

    public static Set<String> containersServer, containersClient;

    private static boolean 
    isSettingsEnabled = true,
    isConfigModeEnabled;

    @SideOnly(Side.CLIENT)
    private static boolean
    isSettingsEnabledClient;

    @SideOnly(Side.CLIENT)
    private static int modeClient;

    public static void init() {
        containersServer = new HashSet<String>();
        load();
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        containersClient = new HashSet<String>();
    }

    private static void load() {	
        try {             
            JsonObject
            internalConfig = (JsonObject) NMQMUtils.getInternalJsonData("assets/nmqm/config.json"),
            internalContainers = (JsonObject) NMQMUtils.getInternalJsonData("assets/nmqm/containers.json");  
            EnumConfigSettings.EXTERNAL_CONFIG.initByType(internalConfig);
            if (EnumConfigSettings.EXTERNAL_CONFIG.isEnabled())
                loadExternalConfig(internalConfig, internalContainers);
            else    
                loadConfigData(internalConfig, internalContainers);
        } catch (IOException exception) {  	
            NMQMMain.LOGGER.error("Internal configuration files damaged!");
            exception.printStackTrace();
        }
    }

    private static void loadExternalConfig(JsonObject internalConfig, JsonObject internalContainers) {
        Path configPath = Paths.get(EXT_CONFIGURATION_FILE);	
        if (Files.exists(configPath)) {
            try {       
                loadConfigData(updateConfig(internalConfig), (JsonObject) NMQMUtils.getExternalJsonData(EXT_CONTAINERS_FILE).getAsJsonObject());
            } catch (IOException exception) {  
                NMQMMain.LOGGER.error("External configuration file damaged!");
                exception.printStackTrace();
            } 	
        } else {		
            Path containersPath = Paths.get(EXT_CONTAINERS_FILE);	
            try {            	
                Files.createDirectories(configPath.getParent());				
                Files.createDirectories(containersPath.getParent());				
                createExternalCopyAndLoad(internalConfig, internalContainers);	
            } catch (IOException exception) {           	
                exception.printStackTrace();
            }			
        }
    }

    private static JsonObject updateConfig(JsonObject internalConfig) throws IOException {
        try {            
            JsonObject externalConfigOld, externalConfigNew, externalGroupNew;
            externalConfigOld = NMQMUtils.getExternalJsonData(EXT_CONFIGURATION_FILE).getAsJsonObject();   
            JsonElement versionElement = externalConfigOld.get("version");
            if (versionElement == null || UpdateChecker.isOutdated(versionElement.getAsString(), NMQMMain.VERSION)) {
                NMQMMain.LOGGER.info("Updating external config file...");
                externalConfigNew = new JsonObject();
                externalConfigNew.add("version", new JsonPrimitive(NMQMMain.VERSION));
                Map<String, JsonElement> 
                internalData = new LinkedHashMap<String, JsonElement>(),
                externlDataOld = new HashMap<String, JsonElement>(),
                internalGroup, externlGroupOld;
                for (Map.Entry<String, JsonElement> entry : internalConfig.entrySet())
                    internalData.put(entry.getKey(), entry.getValue());
                for (Map.Entry<String, JsonElement> entry : externalConfigOld.entrySet())
                    externlDataOld.put(entry.getKey(), entry.getValue());      
                for (String key : internalData.keySet()) {
                    internalGroup = new LinkedHashMap<String, JsonElement>();
                    externlGroupOld = new HashMap<String, JsonElement>();
                    externalGroupNew = new JsonObject();
                    for (Map.Entry<String, JsonElement> entry : internalData.get(key).getAsJsonObject().entrySet())
                        internalGroup.put(entry.getKey(), entry.getValue());
                    if (externlDataOld.containsKey(key)) {                    
                        for (Map.Entry<String, JsonElement> entry : externlDataOld.get(key).getAsJsonObject().entrySet())
                            externlGroupOld.put(entry.getKey(), entry.getValue());   
                        for (String k : internalGroup.keySet()) {
                            if (externlGroupOld.containsKey(k))
                                externalGroupNew.add(k, externlGroupOld.get(k));
                            else 
                                externalGroupNew.add(k, internalGroup.get(k));
                        }
                    } else {
                        for (String k : internalGroup.keySet())
                            externalGroupNew.add(k, internalGroup.get(k));
                    }
                    externalConfigNew.add(key, externalGroupNew);
                    NMQMUtils.createExternalJsonFile(EXT_CONFIGURATION_FILE, externalConfigNew);
                }
                return externalConfigNew;
            }
            NMQMMain.LOGGER.info("External config up-to-date!");
            return externalConfigOld;            
        } catch (IOException exception) {  
            NMQMMain.LOGGER.error("External configuration file damaged!");
            exception.printStackTrace();
        }
        return null;
    }

    private static void createExternalCopyAndLoad(JsonObject internalConfig, JsonObject internalContainers) {  	
        try {        	
            NMQMUtils.createExternalJsonFile(EXT_CONFIGURATION_FILE, internalConfig); 
            NMQMUtils.createExternalJsonFile(EXT_CONTAINERS_FILE, internalContainers);   
            loadConfigData(internalConfig, internalContainers);
        } catch (IOException exception) {        	
            exception.printStackTrace();
        }
    }

    private static void loadConfigData(JsonObject config, JsonObject containers) {		
        EnumConfigSettings.initAll(config);
        setSettingsEnabled(containers.get("enabled").getAsBoolean());
        for (JsonElement element : containers.get("containers").getAsJsonArray())       		
            containersServer.add(element.getAsString());
    }

    public static void save() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        jsonObject.add("enabled", new JsonPrimitive(isSettingsEnabled));
        for (String s : containersServer)
            jsonArray.add(new JsonPrimitive(s));
        jsonObject.add("containers", jsonArray);
        try (Writer writer = new FileWriter(EXT_CONTAINERS_FILE)) {        	
            gson.toJson(jsonObject, writer);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void backup() {
        try {
            NMQMUtils.createExternalJsonFile(
                    CommonReference.getGameFolder() + "/config/nmqm/containers_" + BACKUP_DATE_FORMAT.format(new Date()) + ".json", 
                    NMQMUtils.getExternalJsonData(EXT_CONTAINERS_FILE));         
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static boolean isSettingsEnabled() {
        return isSettingsEnabled;
    }

    public static void setSettingsEnabled(boolean flag) {
        isSettingsEnabled = flag;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isSettingsEnabledClient() {
        return isSettingsEnabledClient;
    }

    @SideOnly(Side.CLIENT)
    public static void setSettingsEnabledClient(boolean flag) {
        isSettingsEnabledClient = flag;
    }

    @SideOnly(Side.CLIENT)
    public static int getModeClient() {
        return modeClient;
    }

    @SideOnly(Side.CLIENT)
    public static void setModeClient(int value) {
        modeClient = value;
    }

    public static boolean isConfigModeEnabled() {           
        return isConfigModeEnabled;
    }

    public static void setConfigModeEnabled(boolean isEnabled) {            
        isConfigModeEnabled = isEnabled;
    }
}
