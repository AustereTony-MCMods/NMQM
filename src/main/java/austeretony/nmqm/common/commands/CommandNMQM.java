package austeretony.nmqm.common.commands;

import austeretony.nmqm.common.main.EnumNMQMChatMessages;
import austeretony.nmqm.common.main.NMQMDataLoader;
import austeretony.nmqm.common.main.NMQMMain;
import austeretony.nmqm.common.network.NetworkHandler;
import austeretony.nmqm.common.network.client.CPSyncContainers;
import austeretony.nmqm.common.origin.CommonReference;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

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
		String[] splittedName = NMQMDataLoader.latestContainer.split("[.]");
		String shortName = splittedName[splittedName.length - 1];
		switch(args[0]) {
			case "enable":
				NMQMDataLoader.setConfigModeEnabled(true);
				NMQMMain.showMessage(player, EnumNMQMChatMessages.CONFIGURATION_ENABLED);
				break;
			case "disable":
				NMQMDataLoader.setConfigModeEnabled(false);
				NMQMDataLoader.latestContainer = "";
				NMQMMain.showMessage(player, EnumNMQMChatMessages.CONFIGURATION_DISABLED);
				break;	
			case "list":
				NMQMMain.showMessage(player, EnumNMQMChatMessages.CONTAINERS_LIST);
	        	if (NMQMDataLoader.CONTAINERS_SERVER.isEmpty())
					NMQMMain.showMessage(player, EnumNMQMChatMessages.EMPTY);
	        	for (String s : NMQMDataLoader.CONTAINERS_SERVER) 
					NMQMMain.showMessage(player, EnumNMQMChatMessages.CONTAINER, s);
				break;
			case "latest":
				if (!NMQMDataLoader.isConfigModeEnabled()) {
					NMQMMain.showMessage(player, EnumNMQMChatMessages.NEED_ENABLE_CONFIGURATION);	
					return;
				} else if (NMQMDataLoader.latestContainer.isEmpty()) {			
					NMQMMain.showMessage(player, EnumNMQMChatMessages.NO_LATEST_CONTAINER);			
					return;
				}
				NMQMMain.showMessage(player, EnumNMQMChatMessages.LATEST_CONTAINER, NMQMDataLoader.latestContainer);
	        	break;
			case "add":
				if (!NMQMDataLoader.isConfigModeEnabled()) {
					NMQMMain.showMessage(player, EnumNMQMChatMessages.NEED_ENABLE_CONFIGURATION);			
					return;
				} else if (NMQMDataLoader.latestContainer.isEmpty()) {			
					NMQMMain.showMessage(player, EnumNMQMChatMessages.NO_LATEST_CONTAINER);					
					return;
				}
				NMQMDataLoader.CONTAINERS_SERVER.add(NMQMDataLoader.latestContainer);
				NMQMMain.showMessage(player, EnumNMQMChatMessages.ADDED_TO_LIST, shortName);					
	        	break;
			case "remove":
				if (!NMQMDataLoader.isConfigModeEnabled()) {
					NMQMMain.showMessage(player, EnumNMQMChatMessages.NEED_ENABLE_CONFIGURATION);					
					return;
				} else if (NMQMDataLoader.latestContainer.isEmpty()) {			
					NMQMMain.showMessage(player, EnumNMQMChatMessages.NO_LATEST_CONTAINER);							
					return;
				}
				if (!NMQMDataLoader.CONTAINERS_SERVER.contains(NMQMDataLoader.latestContainer)) {
					NMQMMain.showMessage(player, EnumNMQMChatMessages.CONTAINER_ABSENT);									
					return;
				}
				NMQMDataLoader.CONTAINERS_SERVER.remove(NMQMDataLoader.latestContainer);
				NMQMMain.showMessage(player, EnumNMQMChatMessages.REMOVED_FROM_LIST, shortName);				
	        	break;
			case "save":
				if (!NMQMDataLoader.isConfigModeEnabled()) {
					NMQMMain.showMessage(player, EnumNMQMChatMessages.NEED_ENABLE_CONFIGURATION);							
					return;
				}
				NMQMDataLoader.saveContainersData();
				NMQMMain.showMessage(player, EnumNMQMChatMessages.CHANGES_SAVED);							
				if (NMQMDataLoader.isClientSyncEnabled())
					for (EntityPlayerMP playerMP : CommonReference.getPlayersServer())
						NetworkHandler.sendToPlayer(new CPSyncContainers(NMQMDataLoader.CONTAINERS_SERVER), playerMP);
	        	break;
			case "clear":
				if (!NMQMDataLoader.isConfigModeEnabled()) {
					NMQMMain.showMessage(player, EnumNMQMChatMessages.NEED_ENABLE_CONFIGURATION);							
					return;
				}
				NMQMDataLoader.CONTAINERS_SERVER.clear();
				NMQMDataLoader.saveContainersData();
				if (NMQMDataLoader.isClientSyncEnabled())
					for (EntityPlayerMP playerMP : CommonReference.getPlayersServer())
						NetworkHandler.sendToPlayer(new CPSyncContainers(NMQMDataLoader.CONTAINERS_SERVER), playerMP);
				NMQMMain.showMessage(player, EnumNMQMChatMessages.CONTAINERS_LIST_CLEARED);							
	        	break;
		}
    }
}


