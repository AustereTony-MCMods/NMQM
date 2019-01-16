package austeretony.nmqm.common.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonSyntaxException;

import austeretony.nmqm.common.command.CommandNMQM;
import austeretony.nmqm.common.network.NetworkHandler;
import austeretony.nmqm.common.origin.CommonReference;
import austeretony.nmqm.common.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = NMQMMain.MODID, name = NMQMMain.NAME, version = NMQMMain.VERSION)
public class NMQMMain {
	
    public static final String 
	MODID = "nmqm",
    NAME = "No More Quick Move",
    VERSION = "1.1.0",
    GAME_VERSION = "1.10.2",
    VERSIONS_URL = "https://raw.githubusercontent.com/AustereTony-MCMods/NMQM/info/versions.json",
    PROJECT_LOCATION = "minecraft.curseforge.com",
    PROJECT_URL = "https://minecraft.curseforge.com/projects/no-more-quick-move-nmqm";
    
	@SidedProxy(clientSide = "austeretony.nmqm.common.proxy.ClientProxy", serverSide = "austeretony.nmqm.common.proxy.CommonProxy")
	public static CommonProxy proxy;
    
	public static final Logger LOGGER = LogManager.getLogger("NMQM");
        
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) { 	
		try {			
			NMQMDataLoader.load();
		} catch (JsonSyntaxException exception) {			
			LOGGER.error("Config parsing failure! Fix syntax errors!");			
			exception.printStackTrace();
		}
    	CommonReference.registerCommand(event, new CommandNMQM());
    }
    
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {	
		NetworkHandler.registerPackets();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {	
		CommonReference.registerEvent(new NMQMServerEvents());
    	UpdateChecker updateChecker = new UpdateChecker();    		
    	CommonReference.registerEvent(new UpdateChecker());
    	new Thread(updateChecker, "NMQM Update Check").start();   		
    	LOGGER.info("Update check started...");
	}
}
