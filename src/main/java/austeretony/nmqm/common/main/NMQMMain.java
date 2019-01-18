package austeretony.nmqm.common.main;

import java.io.PrintStream;

import com.google.gson.JsonSyntaxException;

import austeretony.nmqm.common.commands.CommandNMQM;
import austeretony.nmqm.common.network.NetworkHandler;
import austeretony.nmqm.common.origin.CommonReference;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import net.minecraft.entity.player.EntityPlayer;

@Mod(modid = NMQMMain.MODID, name = NMQMMain.NAME, version = NMQMMain.VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = true, channels = {NMQMMain.MODID + "_list", NMQMMain.MODID + "_msg"}, packetHandler = NetworkHandler.class)
public class NMQMMain {
	
    public static final String 
	MODID = "nmqm",
    NAME = "No More Quick Move",
    VERSION = "1.1.2",
    GAME_VERSION = "1.6.4",
    VERSIONS_URL = "https://raw.githubusercontent.com/AustereTony-MCMods/NMQM/info/versions.json",
    PROJECT_LOCATION = "minecraft.curseforge.com",
    PROJECT_URL = "https://minecraft.curseforge.com/projects/nmqm";
    
	public static final PrintStream LOGGER = System.out;
        
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) { 	
		try {			
			NMQMDataLoader.load();
		} catch (JsonSyntaxException exception) {			
			LOGGER.println("[NMQM][ERROR] Config parsing failure! Fix syntax errors!");
			exception.printStackTrace();
		}
    	CommonReference.registerCommand(event, new CommandNMQM());
    }
	
	@EventHandler
	public void init(FMLInitializationEvent event) {	
		CommonReference.registerEvent(new NMQMServerEvents());
    	UpdateChecker updateChecker = new UpdateChecker();    		
    	CommonReference.registerEvent(new UpdateChecker());
    	new Thread(updateChecker, "NMQM Update Check").start();   		
    	LOGGER.println("[NMQM][INFO] Update check started...");
	}
	
	public static void showMessage(EntityPlayer player, EnumNMQMChatMessages msg, String ... args) {
		NetworkHandler.sendMessageData(player, msg, args);
	}
}
