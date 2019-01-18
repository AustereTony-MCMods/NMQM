package austeretony.nmqm.common.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import austeretony.nmqm.common.origin.CommonReference;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class UpdateChecker implements Runnable {
	
	private static String availableVersion = NMQMMain.VERSION;
	
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
		if (NMQMDataLoader.isUpdateMessagesEnabled() && CommonReference.isOpped(event.player)) {
			if (this.compareVersions(NMQMMain.VERSION, availableVersion)) {	
				NMQMMain.showMessage(event.player, EnumNMQMChatMessages.UPDATE_MESSAGE_HEAD, availableVersion);
				NMQMMain.showMessage(event.player, EnumNMQMChatMessages.UPDATE_MESSAGE_LINK);
			}
		}
	}
	
	@Override
	public void run() {
		URL versionsURL;		
		try {			
			versionsURL = new URL(NMQMMain.VERSIONS_URL);
		} catch (MalformedURLException exception) {			
			exception.printStackTrace();			
			return;
		}
		JsonObject remoteData;					
		try (InputStream inputStream = versionsURL.openStream()) {			
			remoteData = (JsonObject) new JsonParser().parse(new InputStreamReader(inputStream, "UTF-8")); 
		} catch (UnknownHostException exception) {		
			NMQMMain.LOGGER.error("Update check failed, no internet connection.");		
			return;
		} catch (FileNotFoundException exception) {			
			NMQMMain.LOGGER.error("Update check failed, remote file is absent.");			
			return;
		} catch (IOException exception) {						
			exception.printStackTrace();			
			return;
		}				        
        JsonObject data;          
        try {        	
        	data = remoteData.get(NMQMMain.GAME_VERSION).getAsJsonObject();      
        } catch (NullPointerException exception) {        	
        	NMQMMain.LOGGER.error("Update check failed, data is undefined for " + NMQMMain.GAME_VERSION + " version.");        	
        	return;
        }        
        availableVersion = data.get("available").getAsString();
	}
	
	private boolean compareVersions(String currentVersion, String availableVersion) {								
		String[] 
		cVer = currentVersion.split("[.]"),
		aVer = availableVersion.split("[.]");				
		int diff;		
		for (int i = 0; i < cVer.length; i++) {					
			try {				
				diff = Integer.parseInt(aVer[i]) - Integer.parseInt(cVer[i]);												
				if (diff > 0)
					return true;				
				if (diff < 0)
					return false;
			} catch (NumberFormatException exception) {				
				exception.printStackTrace();
			}
		}		
		return false;
	}
}
