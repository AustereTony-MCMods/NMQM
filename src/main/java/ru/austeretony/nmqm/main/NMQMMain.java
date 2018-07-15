package ru.austeretony.nmqm.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import ru.austeretony.nmqm.command.CommandNMQM;

@Mod(modid = NMQMMain.MODID, name = NMQMMain.NAME, version = NMQMMain.VERSION)
public class NMQMMain {
	
    public static final String 
	MODID = "nmqm",
    NAME = "No More Quick Move",
    VERSION = "1.0.0",
    GAME_VERSION = "1.9.4",
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
