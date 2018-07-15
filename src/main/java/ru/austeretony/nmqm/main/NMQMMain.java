package ru.austeretony.nmqm.main;

import java.io.PrintStream;

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
    GAME_VERSION = "1.6.4",
    VERSIONS_URL = "https://raw.githubusercontent.com/AustereTony-MCMods/NMQM/info/versions.json",
    PROJECT_URL = "https://minecraft.curseforge.com/projects/no-more-quick-move-nmqm";
    
	public static final PrintStream LOGGER = System.out;
        
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
