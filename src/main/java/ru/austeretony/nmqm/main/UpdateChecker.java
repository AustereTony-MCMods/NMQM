package ru.austeretony.nmqm.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class UpdateChecker {

	@ForgeSubscribe
	public void onPlayerJoinedWorld(EntityJoinWorldEvent event) {
		
		if (!event.world.isRemote && event.entity instanceof EntityPlayer) {
					
			if (ConfigLoader.isUpdateCheckerEnabled())				
				this.checkForUpdates((EntityPlayer) event.entity);
		}
	}
	
	private void checkForUpdates(EntityPlayer player) {
							
		try {
			
			URL versionsURL = new URL(NMQMMain.VERSIONS_URL);
			
			InputStream inputStream = null;
			
			try {
				
				inputStream = versionsURL.openStream();
			}
			
			catch (UnknownHostException exception) {
														
				NMQMMain.LOGGER.println("[NMQM][ERROR] Update check failed, no internet connection.");
				
				return;
			}
			
            JsonObject remoteData = (JsonObject) new JsonParser().parse(new InputStreamReader(inputStream, "UTF-8"));  			
			
            inputStream.close();
            
            JsonObject data;  
            
            try {
            	
            	data = remoteData.get(NMQMMain.GAME_VERSION).getAsJsonObject();      
            }
            
            catch (NullPointerException exception) {
            	
				NMQMMain.LOGGER.println("[NMQM][ERROR] Update check failed, remote data is undefined for " + NMQMMain.GAME_VERSION + " version.");
            	
            	return;
            }
            
            String availableVersion = data.get("available").getAsString();
            
            if (this.compareVersions(NMQMMain.VERSION, availableVersion)) {	
            	            	
            	ChatMessageComponent 
            	updateMessage1 = new ChatMessageComponent().addText("[NMQM] "),
                updateMessage2 = new ChatMessageComponent().addKey("nmqm.update.newVersion"),
                updateMessage3 = new ChatMessageComponent().addText(" [" + NMQMMain.VERSION + "/" + availableVersion + "]");
                        	            	
            	player.addChatMessage(updateMessage1.appendComponent(updateMessage2).appendComponent(updateMessage3).toString());
            }
		}
		
		catch (MalformedURLException exception) {
			
			exception.printStackTrace();
		}
		
		catch (FileNotFoundException exception) {
			
			NMQMMain.LOGGER.println("[NMQM][ERROR] Update check failed, remote file is absent.");			
		}
		
		catch (IOException exception) {
			
			exception.printStackTrace();
		}
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
			}
			
			catch (NumberFormatException exception) {
				
				exception.printStackTrace();
			}
		}
		
		return false;
	}
}
