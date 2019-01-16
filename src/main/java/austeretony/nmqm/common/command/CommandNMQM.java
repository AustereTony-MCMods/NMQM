package austeretony.nmqm.common.command;

import austeretony.nmqm.common.main.NMQMDataLoader;
import austeretony.nmqm.common.network.NetworkHandler;
import austeretony.nmqm.common.network.client.CPClearContainers;
import austeretony.nmqm.common.network.client.CPSyncContainer;
import austeretony.nmqm.common.origin.CommonReference;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class CommandNMQM extends CommandBase {
	
	public static final String 
	NAME = "nmqm",
	USAGE = "/mmqm <enable, list, latest, add, remove, clear, save, disable>";

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
        return sender instanceof EntityPlayerMP && CommonReference.isOpped((EntityPlayerMP) sender);
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {   	
		if (args.length != 1 || !(
				args[0].equals("enable") || 
				args[0].equals("list") || 
				args[0].equals("latest") || 
				args[0].equals("add") || 
				args[0].equals("remove") || 
				args[0].equals("clear") || 
				args[0].equals("save") ||
				args[0].equals("disable")))		
			throw new WrongUsageException(this.getCommandUsage(sender));	
		EntityPlayer player = getCommandSenderAsPlayer(sender);		
		String[] containerArray = NMQMDataLoader.latestContainer.split("[.]");
		IChatComponent 
		modPrefix = new ChatComponentText("[NMQM] "),
		containerName = new ChatComponentText(NMQMDataLoader.latestContainer),
		shortName = new ChatComponentText(containerArray[containerArray.length - 1]),
		msg1, msg2, msg3;
		modPrefix.getChatStyle().setColor(EnumChatFormatting.AQUA);		
		containerName.getChatStyle().setColor(EnumChatFormatting.WHITE);  
		shortName.getChatStyle().setColor(EnumChatFormatting.WHITE); 
		switch(args[0]) {
			case "enable":
				NMQMDataLoader.setDebugModeEnabled(true);
				msg1 = new ChatComponentTranslation("nmqm.command.enable");
	        	player.addChatMessage(modPrefix.appendSibling(msg1));
				break;
			case "disable":
				NMQMDataLoader.setDebugModeEnabled(false);
				NMQMDataLoader.latestContainer = "";
				msg1 = new ChatComponentTranslation("nmqm.command.disable");
	        	player.addChatMessage(modPrefix.appendSibling(msg1));
				break;	
			case "list":
				msg1 = new ChatComponentTranslation("nmqm.command.list");
	        	player.addChatMessage(modPrefix.appendSibling(msg1));
	        	if (NMQMDataLoader.CONTAINERS_SERVER.isEmpty())
	        		player.addChatMessage(new ChatComponentTranslation("nmqm.command.list.empty"));
	        	for (String s : NMQMDataLoader.CONTAINERS_SERVER) 
	        		player.addChatMessage(new ChatComponentText(s));
				break;
			case "latest":
				if (!NMQMDataLoader.isDebugModeEnabled()) {
					msg1 = new ChatComponentTranslation("nmqm.command.err.debugMode");			
					msg1.getChatStyle().setColor(EnumChatFormatting.RED);			
					player.addChatMessage(modPrefix.appendSibling(msg1));			
					return;
				} else if (NMQMDataLoader.latestContainer.isEmpty()) {			
					msg1 = new ChatComponentTranslation("nmqm.command.latest.notExist");			
					msg1.getChatStyle().setColor(EnumChatFormatting.RED);			
					player.addChatMessage(modPrefix.appendSibling(msg1));			
					return;
				}
				msg1 = new ChatComponentTranslation("nmqm.command.latest");
	        	player.addChatMessage(modPrefix.appendSibling(msg1));
	        	player.addChatMessage(containerName);
	        	break;
			case "add":
				if (!NMQMDataLoader.isDebugModeEnabled()) {
					msg1 = new ChatComponentTranslation("nmqm.command.err.debugMode");			
					msg1.getChatStyle().setColor(EnumChatFormatting.RED);			
					player.addChatMessage(modPrefix.appendSibling(msg1));			
					return;
				} else if (NMQMDataLoader.latestContainer.isEmpty()) {			
					msg1 = new ChatComponentTranslation("nmqm.command.latest.notExist");			
					msg1.getChatStyle().setColor(EnumChatFormatting.RED);			
					player.addChatMessage(modPrefix.appendSibling(msg1));			
					return;
				}
				NMQMDataLoader.CONTAINERS_SERVER.add(NMQMDataLoader.latestContainer);
				msg1 = new ChatComponentTranslation("nmqm.command.add");
				msg2 = new ChatComponentText(": ");
	        	player.addChatMessage(modPrefix.appendSibling(msg1).appendSibling(msg2).appendSibling(shortName));
	        	break;
			case "remove":
				if (!NMQMDataLoader.isDebugModeEnabled()) {
					msg1 = new ChatComponentTranslation("nmqm.command.err.debugMode");			
					msg1.getChatStyle().setColor(EnumChatFormatting.RED);			
					player.addChatMessage(modPrefix.appendSibling(msg1));			
					return;
				} else if (NMQMDataLoader.latestContainer.isEmpty()) {			
					msg1 = new ChatComponentTranslation("nmqm.command.latest.notExist");			
					msg1.getChatStyle().setColor(EnumChatFormatting.RED);			
					player.addChatMessage(modPrefix.appendSibling(msg1));			
					return;
				}
				if (NMQMDataLoader.CONTAINERS_SERVER.contains(NMQMDataLoader.latestContainer))
					NMQMDataLoader.CONTAINERS_SERVER.remove(NMQMDataLoader.latestContainer);
				else {
					msg1 = new ChatComponentTranslation("nmqm.command.remove.absent");			
					msg1.getChatStyle().setColor(EnumChatFormatting.RED);			
					player.addChatMessage(modPrefix.appendSibling(msg1));			
					return;
				}
				msg1 = new ChatComponentTranslation("nmqm.command.remove");
				msg2 = new ChatComponentText(": ");
	        	player.addChatMessage(modPrefix.appendSibling(msg1).appendSibling(msg2).appendSibling(shortName));
	        	break;
			case "clear":
				if (!NMQMDataLoader.isDebugModeEnabled()) {
					msg1 = new ChatComponentTranslation("nmqm.command.err.debugMode");			
					msg1.getChatStyle().setColor(EnumChatFormatting.RED);			
					player.addChatMessage(modPrefix.appendSibling(msg1));			
					return;
				}
				NMQMDataLoader.CONTAINERS_SERVER.clear();
				msg1 = new ChatComponentTranslation("nmqm.command.clear");
	        	player.addChatMessage(modPrefix.appendSibling(msg1));
	        	break;
			case "save":
				if (!NMQMDataLoader.isDebugModeEnabled()) {
					msg1 = new ChatComponentTranslation("nmqm.command.err.debugMode");			
					msg1.getChatStyle().setColor(EnumChatFormatting.RED);			
					player.addChatMessage(modPrefix.appendSibling(msg1));			
					return;
				}
				NMQMDataLoader.saveContainersData();
				if (NMQMDataLoader.isClientSyncEnabled()) {
					for (EntityPlayerMP playerMP : CommonReference.getPlayersServer()) {
						NetworkHandler.sendToPlayer(new CPClearContainers(), playerMP);
						for (String s : NMQMDataLoader.CONTAINERS_SERVER)
							NetworkHandler.sendToPlayer(new CPSyncContainer(s), playerMP);
					}
				}
				msg1 = new ChatComponentTranslation("nmqm.command.save");
	        	player.addChatMessage(modPrefix.appendSibling(msg1));
	        	break;
		}
    }
}


