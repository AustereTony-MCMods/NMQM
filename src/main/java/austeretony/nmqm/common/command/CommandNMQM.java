package austeretony.nmqm.common.command;

import austeretony.nmqm.common.main.NMQMDataLoader;
import austeretony.nmqm.common.network.NetworkHandler;
import austeretony.nmqm.common.network.client.CPSyncContainer;
import austeretony.nmqm.common.origin.CommonReference;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class CommandNMQM extends CommandBase {
	
	public static final String 
	NAME = "nmqm",
	USAGE = "/mmqm <enable, list, latest, add, remove, clear, save, disable>";

	@Override
	public String getName() {		
		return NAME;
	}

	@Override
	public String getUsage(ICommandSender sender) {		
		return USAGE;
	}
	
	@Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {   	
        return sender instanceof EntityPlayerMP && CommonReference.isOpped((EntityPlayerMP) sender);
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {   	
		if (args.length != 1 || !(
				args[0].equals("enable") || 
				args[0].equals("list") || 
				args[0].equals("latest") || 
				args[0].equals("add") || 
				args[0].equals("remove") || 
				args[0].equals("clear") || 
				args[0].equals("save") ||
				args[0].equals("disable")))		
			throw new WrongUsageException(this.getUsage(sender));	
		EntityPlayer player = getCommandSenderAsPlayer(sender);		
		String[] containerArray = NMQMDataLoader.latestContainer.split("[.]");
		ITextComponent 
		modPrefix = new TextComponentString("[NMQM] "),
		containerName = new TextComponentString(NMQMDataLoader.latestContainer),
		shortName = new TextComponentString(containerArray[containerArray.length - 1]),
		msg1, msg2, msg3;
		modPrefix.getStyle().setColor(TextFormatting.AQUA);		
		containerName.getStyle().setColor(TextFormatting.WHITE);  
		shortName.getStyle().setColor(TextFormatting.WHITE); 
		switch(args[0]) {
			case "enable":
				NMQMDataLoader.setDebugModeEnabled(true);
				msg1 = new TextComponentTranslation("nmqm.command.enable");
	        	player.sendMessage(modPrefix.appendSibling(msg1));
				break;
			case "disable":
				NMQMDataLoader.setDebugModeEnabled(false);
				NMQMDataLoader.latestContainer = "";
				msg1 = new TextComponentTranslation("nmqm.command.disable");
	        	player.sendMessage(modPrefix.appendSibling(msg1));
				break;	
			case "list":
				msg1 = new TextComponentTranslation("nmqm.command.list");
	        	player.sendMessage(modPrefix.appendSibling(msg1));
	        	if (NMQMDataLoader.CONTAINERS_SERVER.isEmpty())
	        		player.sendMessage(new TextComponentTranslation("nmqm.command.list.empty"));
	        	for (String s : NMQMDataLoader.CONTAINERS_SERVER) 
	        		player.sendMessage(new TextComponentString(s));
				break;
			case "latest":
				if (!NMQMDataLoader.isDebugModeEnabled()) {
					msg1 = new TextComponentTranslation("nmqm.command.err.debugMode");			
					msg1.getStyle().setColor(TextFormatting.RED);			
					player.sendMessage(modPrefix.appendSibling(msg1));			
					return;
				} else if (NMQMDataLoader.latestContainer.isEmpty()) {			
					msg1 = new TextComponentTranslation("nmqm.command.latest.notExist");			
					msg1.getStyle().setColor(TextFormatting.RED);			
					player.sendMessage(modPrefix.appendSibling(msg1));			
					return;
				}
				msg1 = new TextComponentTranslation("nmqm.command.latest");
	        	player.sendMessage(modPrefix.appendSibling(msg1));
	        	player.sendMessage(containerName);
	        	break;
			case "add":
				if (!NMQMDataLoader.isDebugModeEnabled()) {
					msg1 = new TextComponentTranslation("nmqm.command.err.debugMode");			
					msg1.getStyle().setColor(TextFormatting.RED);			
					player.sendMessage(modPrefix.appendSibling(msg1));			
					return;
				} else if (NMQMDataLoader.latestContainer.isEmpty()) {			
					msg1 = new TextComponentTranslation("nmqm.command.latest.notExist");			
					msg1.getStyle().setColor(TextFormatting.RED);			
					player.sendMessage(modPrefix.appendSibling(msg1));			
					return;
				}
				NMQMDataLoader.CONTAINERS_SERVER.add(NMQMDataLoader.latestContainer);
				msg1 = new TextComponentTranslation("nmqm.command.add");
				msg2 = new TextComponentString(": ");
	        	player.sendMessage(modPrefix.appendSibling(msg1).appendSibling(msg2).appendSibling(shortName));
	        	break;
			case "remove":
				if (!NMQMDataLoader.isDebugModeEnabled()) {
					msg1 = new TextComponentTranslation("nmqm.command.err.debugMode");			
					msg1.getStyle().setColor(TextFormatting.RED);			
					player.sendMessage(modPrefix.appendSibling(msg1));			
					return;
				} else if (NMQMDataLoader.latestContainer.isEmpty()) {			
					msg1 = new TextComponentTranslation("nmqm.command.latest.notExist");			
					msg1.getStyle().setColor(TextFormatting.RED);			
					player.sendMessage(modPrefix.appendSibling(msg1));			
					return;
				}
				if (NMQMDataLoader.CONTAINERS_SERVER.contains(NMQMDataLoader.latestContainer))
					NMQMDataLoader.CONTAINERS_SERVER.remove(NMQMDataLoader.latestContainer);
				else {
					msg1 = new TextComponentTranslation("nmqm.command.remove.absent");			
					msg1.getStyle().setColor(TextFormatting.RED);			
					player.sendMessage(modPrefix.appendSibling(msg1));			
					return;
				}
				msg1 = new TextComponentTranslation("nmqm.command.remove");
				msg2 = new TextComponentString(": ");
	        	player.sendMessage(modPrefix.appendSibling(msg1).appendSibling(msg2).appendSibling(shortName));
	        	break;
			case "clear":
				if (!NMQMDataLoader.isDebugModeEnabled()) {
					msg1 = new TextComponentTranslation("nmqm.command.err.debugMode");			
					msg1.getStyle().setColor(TextFormatting.RED);			
					player.sendMessage(modPrefix.appendSibling(msg1));			
					return;
				}
				NMQMDataLoader.CONTAINERS_SERVER.clear();
				msg1 = new TextComponentTranslation("nmqm.command.clear");
	        	player.sendMessage(modPrefix.appendSibling(msg1));
	        	break;
			case "save":
				if (!NMQMDataLoader.isDebugModeEnabled()) {
					msg1 = new TextComponentTranslation("nmqm.command.err.debugMode");			
					msg1.getStyle().setColor(TextFormatting.RED);			
					player.sendMessage(modPrefix.appendSibling(msg1));			
					return;
				}
				NMQMDataLoader.saveContainersData();
				if (NMQMDataLoader.isClientSyncEnabled()) {
					for (EntityPlayerMP playerMP : CommonReference.getPlayersListServer()) {
						for (String s : NMQMDataLoader.CONTAINERS_SERVER)
							NetworkHandler.sendToPlayer(new CPSyncContainer(CPSyncContainer.EnumOperation.ADD, s), playerMP);
					}
				}
				msg1 = new TextComponentTranslation("nmqm.command.save");
	        	player.sendMessage(modPrefix.appendSibling(msg1));
	        	break;
		}
    }
}


