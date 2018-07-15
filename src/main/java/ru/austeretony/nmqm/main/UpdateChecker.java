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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class UpdateChecker {

	@SubscribeEvent
	public void onPlayerJoinedWorld(EntityJoinWorldEvent event) {
		
		if (!event.getWorld().isRemote && event.getEntity() instanceof EntityPlayer) {
					
			if (ConfigLoader.isUpdateCheckerEnabled())				
				this.checkForUpdates((EntityPlayer) event.getEntity());
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
														
				NMQMMain.LOGGER.error("Update check failed, no internet connection.");
				
				return;
			}
			
            JsonObject remoteData = (JsonObject) new JsonParser().parse(new InputStreamReader(inputStream, "UTF-8"));  			
			
            inputStream.close();
            
            JsonObject data;  
            
            try {
            	
            	data = remoteData.get(NMQMMain.GAME_VERSION).getAsJsonObject();      
            }
            
            catch (NullPointerException exception) {
            	
				NMQMMain.LOGGER.error("Update check failed, remote data is undefined for " + NMQMMain.GAME_VERSION + " version.");
            	
            	return;
            }
            
            String availableVersion = data.get("available").getAsString();
            
            if (this.compareVersions(NMQMMain.VERSION, availableVersion)) {	
            	            	
            	ITextComponent 
            	updateMessage1 = new TextComponentString("[NMQM] "),
                updateMessage2 = new TextComponentTranslation("nmqm.update.newVersion"),
                updateMessage3 = new TextComponentString(" [" + NMQMMain.VERSION + "/" + availableVersion + "]"),
            	pageMessage1 = new TextComponentTranslation("nmqm.update.projectPage"),
                pageMessage2 = new TextComponentString(": "),
            	urlMessage = new TextComponentString(NMQMMain.PROJECT_URL);
            
            	updateMessage1.getStyle().setColor(TextFormatting.AQUA);
            	pageMessage1.getStyle().setColor(TextFormatting.AQUA);
            	urlMessage.getStyle().setColor(TextFormatting.WHITE);
            	
            	urlMessage.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, urlMessage.getUnformattedText()));
            	
            	player.sendMessage(updateMessage1.appendSibling(updateMessage2).appendSibling(updateMessage3));
            	player.sendMessage(pageMessage1.appendSibling(pageMessage2).appendSibling(urlMessage));
            }
		}
		
		catch (MalformedURLException exception) {
			
			exception.printStackTrace();
		}
		
		catch (FileNotFoundException exception) {
			
			NMQMMain.LOGGER.error("Update check failed, remote file is absent.");			
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
