package ru.austeretony.nmqm.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.IOUtils;

import cpw.mods.fml.relauncher.FMLInjectionData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import ru.austeretony.nmqm.main.ConfigLoader;

public class CommandNMQM extends CommandBase {
	
	public static final String 
	NAME = "nmqm",
	USAGE = "/mmqm <latest, save, update>";

	@Override
	public String getCommandName() {
		
		return NAME;
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		
		return USAGE;
	}
	
	@Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
    	
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
    	
		if (args.length != 1 || !(args[0].equals("latest") || args[0].equals("save") || args[0].equals("update")))		
			throw new WrongUsageException(this.getCommandUsage(sender));
		
		EntityPlayer player = getCommandSenderAsPlayer(sender);
		
		IChatComponent message1, message2, message3;
		
		if (ConfigLoader.latestContainer.isEmpty()) {
			
			message1 = new ChatComponentText("[NMQM] ");
			message2 = new ChatComponentTranslation("nmqm.command.noLatest");
			
			message1.getChatStyle().setColor(EnumChatFormatting.RED);
			
			player.addChatMessage(message1.appendSibling(message2));
			
			return;
		}
		
		String containerClassString = "/" + ConfigLoader.latestContainer + "/";
		
		containerClassString = containerClassString.replace('/', '"');
		
		if (args[0].equals("latest")) {
						
			message1 = new ChatComponentText("[NMQM] ");
			message2 = new ChatComponentTranslation("nmqm.command.latest");
			message3 = new ChatComponentText(": ");
			
			IChatComponent containerName = new ChatComponentText(ConfigLoader.latestContainer);
			
			message1.getChatStyle().setColor(EnumChatFormatting.AQUA);
			containerName.getChatStyle().setColor(EnumChatFormatting.WHITE);
        	
        	player.addChatMessage(message1.appendSibling(message2).appendSibling(message3).appendSibling(containerName));
		}
								
		if (args[0].equals("save")) {
						
			String 
			gameDirPath = ((File) (FMLInjectionData.data()[6])).getAbsolutePath(),
			filePath = gameDirPath + "/config/nmqm/containers.json";			

			Path path = Paths.get(filePath);
			
			if (Files.exists(path)) {
				
				try {
				
					InputStream inputStream = new FileInputStream(new File(filePath));
					
					List<String> containersData = IOUtils.readLines(new InputStreamReader(inputStream, "UTF-8"));
					
					inputStream.close();
					
					String lastLine = containersData.get(containersData.size() - 2);
					
					if (lastLine.equals(containerClassString)) {
						
						message1 = new ChatComponentText("[NMQM] ");
						message2 = new ChatComponentTranslation("nmqm.command.alreadySaved");
						
						message1.getChatStyle().setColor(EnumChatFormatting.RED);
						
						player.addChatMessage(message1.appendSibling(message2));
						
						return;
					}
					
					containersData.remove("]}");									
					containersData.remove(lastLine);
					
					containersData.add(lastLine + ",");					
					containersData.add(containerClassString);
					containersData.add("]}");
					
					try {
						
				        PrintStream fileStream = new PrintStream(new File(filePath));
				        
				        for (String line : containersData)			        	
				        	fileStream.println(line);
				        
				        fileStream.close();
				        
						message1 = new ChatComponentText("[NMQM] ");
						message2 = new ChatComponentTranslation("nmqm.command.saved");
						
						message1.getChatStyle().setColor(EnumChatFormatting.GREEN);
						
						player.addChatMessage(message1.appendSibling(message2));
					}
			        
			        catch (IOException exception) {
			        	
			        	exception.printStackTrace();
					}
				}
	        	
	        	catch (IOException exception) {
	        		
	        		exception.printStackTrace();
				} 	
			}
			
			else {
				
	            try {
	            	
					Files.createDirectories(path.getParent());									
										
					try {
						
				        PrintStream fileStream = new PrintStream(new File(filePath));
				        
				        fileStream.println("{/containers/: [".replace('/', '"'));							
				        fileStream.println(containerClassString);					
				        fileStream.println("]}");	
				        
				        fileStream.close();
				        
						message1 = new ChatComponentText("[NMQN] ");
						message2 = new ChatComponentTranslation("nmqm.command.created");
						
						message1.getChatStyle().setColor(EnumChatFormatting.GREEN);
											
						player.addChatMessage(message1.appendSibling(message2));
					}
			        
			        catch (IOException exception) {
			        	
			        	exception.printStackTrace();
					}
				} 
	            
	            catch (IOException exception) {
	            	
	            	exception.printStackTrace();
				}	
			}
		}
		
		if (args[0].equals("update")) {
						
			if (!ConfigLoader.isExternalConfigEnabled()) {
				
				message1 = new ChatComponentText("[NMQM] ");
				message2 = new ChatComponentTranslation("nmqm.command.noExternal");
				
				message1.getChatStyle().setColor(EnumChatFormatting.RED);
				
				player.addChatMessage(message1.appendSibling(message2));
				
				return;
			}
			
			String 
			gameDirPath = ((File) (FMLInjectionData.data()[6])).getAbsolutePath(),
			filePath = gameDirPath + "/config/nmqm/nmqm.json";
						
			try {
				
				InputStream inputStream = new FileInputStream(new File(filePath));
				
				List<String> containersData = IOUtils.readLines(new InputStreamReader(inputStream, "UTF-8"));
				
				inputStream.close();
				
				String lastLine = containersData.get(containersData.size() - 2);
									
				if (lastLine.equals(containerClassString)) {
					
					message1 = new ChatComponentText("[NMQM] ");
					message2 = new ChatComponentTranslation("nmqm.command.alreadyUpdated");
					
					message1.getChatStyle().setColor(EnumChatFormatting.RED);
					
					player.addChatMessage(message1.appendSibling(message2));
					
					return;
				}
				
				else if (lastLine.equals("")) {
					
					containersData.remove("]}");									
					containersData.remove(containersData.size() - 1);
					
					containersData.add(containerClassString);
					containersData.add("]}");
				}
				
				else {
					
					containersData.remove("]}");									
					containersData.remove(lastLine);
					
					containersData.add(lastLine + ",");					
					containersData.add(containerClassString);
					containersData.add("]}");
				}				
				
				try {
					
			        PrintStream fileStream = new PrintStream(new File(filePath));
			        
			        for (String line : containersData)			        	
			        	fileStream.println(line);
			        
			        fileStream.close();
			        
					message1 = new ChatComponentText("[NMQM] ");
					message2 = new ChatComponentTranslation("nmqm.command.updated");
					
					message1.getChatStyle().setColor(EnumChatFormatting.GREEN);
					
					player.addChatMessage(message1.appendSibling(message2));
				}
		        
		        catch (IOException exception) {
		        	
		        	exception.printStackTrace();
				}
			}
			
        	catch (IOException exception) {
        		
        		exception.printStackTrace();
			} 	
		}
    }
}


