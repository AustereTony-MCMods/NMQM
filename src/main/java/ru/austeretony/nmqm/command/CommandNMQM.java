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

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.FMLInjectionData;
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
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    	
        return true;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    	
		if (args.length != 1 || !(args[0].equals("latest") || args[0].equals("save") || args[0].equals("update")))		
			throw new WrongUsageException(this.getCommandUsage(sender));
		
		EntityPlayer player = getCommandSenderAsPlayer(sender);
		
		ITextComponent message1, message2, message3;
		
		if (ConfigLoader.latestContainer.isEmpty()) {
			
			message1 = new TextComponentString("[NMQM] ");
			message2 = new TextComponentTranslation("nmqm.command.noLatest");
			
			message1.getStyle().setColor(TextFormatting.RED);
			
			player.addChatMessage(message1.appendSibling(message2));
			
			return;
		}
		
		String containerClassString = "/" + ConfigLoader.latestContainer + "/";
		
		containerClassString = containerClassString.replace('/', '"');
		
		if (args[0].equals("latest")) {
						
			message1 = new TextComponentString("[NMQM] ");
			message2 = new TextComponentTranslation("nmqm.command.latest");
			message3 = new TextComponentString(": ");
			
			ITextComponent containerName = new TextComponentString(ConfigLoader.latestContainer);
			
			message1.getStyle().setColor(TextFormatting.AQUA);
			containerName.getStyle().setColor(TextFormatting.WHITE);
        	
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
						
						message1 = new TextComponentString("[NMQM] ");
						message2 = new TextComponentTranslation("nmqm.command.alreadySaved");
						
						message1.getStyle().setColor(TextFormatting.RED);
						
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
				        
						message1 = new TextComponentString("[NMQM] ");
						message2 = new TextComponentTranslation("nmqm.command.saved");
						
						message1.getStyle().setColor(TextFormatting.GREEN);
						
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
				        
						message1 = new TextComponentString("[NMQN] ");
						message2 = new TextComponentTranslation("nmqm.command.created");
						
						message1.getStyle().setColor(TextFormatting.GREEN);
											
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
				
				message1 = new TextComponentString("[NMQM] ");
				message2 = new TextComponentTranslation("nmqm.command.noExternal");
				
				message1.getStyle().setColor(TextFormatting.RED);
				
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
					
					message1 = new TextComponentString("[NMQM] ");
					message2 = new TextComponentTranslation("nmqm.command.alreadyUpdated");
					
					message1.getStyle().setColor(TextFormatting.RED);
					
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
			        
					message1 = new TextComponentString("[NMQM] ");
					message2 = new TextComponentTranslation("nmqm.command.updated");
					
					message1.getStyle().setColor(TextFormatting.GREEN);
					
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


