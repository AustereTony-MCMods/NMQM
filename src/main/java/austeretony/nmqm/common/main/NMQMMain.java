package austeretony.nmqm.common.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import austeretony.nmqm.common.commands.CommandNMQM;
import austeretony.nmqm.common.config.ConfigLoader;
import austeretony.nmqm.common.config.EnumConfigSettings;
import austeretony.nmqm.common.network.NetworkHandler;
import austeretony.nmqm.common.reference.CommonReference;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(
        modid = NMQMMain.MODID, 
        name = NMQMMain.NAME, 
        version = NMQMMain.VERSION,
        acceptableRemoteVersions = "*",
        certificateFingerprint = "@FINGERPRINT@",
        updateJSON = NMQMMain.VERSIONS_FORGE_URL)
public class NMQMMain {

    public static final String 
    MODID = "nmqm",
    NAME = "NMQM",
    VERSION = "1.2.1",
    GAME_VERSION = "1.12.2",
    VERSIONS_FORGE_URL = "https://raw.githubusercontent.com/AustereTony-MCMods/NMQM/info/mod_versions_forge.json",
    VERSIONS_CUSTOM_URL = "https://raw.githubusercontent.com/AustereTony-MCMods/NMQM/info/versions.json",
    PROJECT_LOCATION = "minecraft.curseforge.com",
    PROJECT_URL = "https://minecraft.curseforge.com/projects/nmqm";

    public static final Logger LOGGER = LogManager.getLogger(NAME);

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {	
        ConfigLoader.init();
        if (!EnumConfigSettings.SERVER_ONLY.isEnabled())
            NetworkHandler.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {	
        if (!EnumConfigSettings.SERVER_ONLY.isEnabled())
            CommonReference.registerEvent(new NMQMServerEvents());
        if (EnumConfigSettings.CUSTOM_UPDATE_CHECKER.isEnabled()) {
            UpdateChecker updateChecker = new UpdateChecker();                  
            CommonReference.registerEvent(new UpdateChecker());
            new Thread(updateChecker, "NMQM Update Check").start();   
        }
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {  
        CommonReference.registerCommand(event, new CommandNMQM());
    }
}
