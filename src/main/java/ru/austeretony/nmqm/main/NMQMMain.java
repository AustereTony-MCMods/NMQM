package ru.austeretony.nmqm.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.common.MinecraftForge;
import ru.austeretony.nmqm.command.CommandNMQM;

@Mod(modid = NMQMMain.MODID, name = NMQMMain.NAME, version = NMQMMain.VERSION)
public class NMQMMain {
	
    public static final String 
	MODID = "nmqm",
    NAME = "No More Quick Move",
    VERSION = "1.0.0",
    GAME_VERSION = "1.7.10",
    VERSIONS_URL = "https://raw.githubusercontent.com/AustereTony-MCMods/NMQM/info/versions.json",
    PROJECT_URL = "https://minecraft.curseforge.com/projects/no-more-quick-move-nmqm";
    
	public static final Logger LOGGER = LogManager.getLogger("NMQM");
        
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
    	
    	if (ConfigLoader.isDebugModeEnabled())
    		event.registerServerCommand(new CommandNMQM());
    }
	
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	
    	if (ConfigLoader.isUpdateCheckerEnabled())
    		MinecraftForge.EVENT_BUS.register(new UpdateChecker());
    }
}
