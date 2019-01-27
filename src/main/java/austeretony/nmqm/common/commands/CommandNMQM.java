package austeretony.nmqm.common.commands;

import austeretony.nmqm.common.main.DataLoader;
import austeretony.nmqm.common.main.EnumChatMessages;
import austeretony.nmqm.common.network.NetworkHandler;
import austeretony.nmqm.common.network.client.CPSyncContainers;
import austeretony.nmqm.common.reference.CommonReference;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandNMQM extends CommandBase {

    public static final String 
    NAME = "nmqm",
    USAGE = "/nmqm <arg>, type </nmqm help> for available arguments.";

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
        EnumCommandNMQMArgs arg;
        if (args.length != 1 || (arg = EnumCommandNMQMArgs.get(args[0])) == null)  
            throw new WrongUsageException(this.getCommandUsage(sender));   
        EntityPlayer player = getCommandSenderAsPlayer(sender);		
        String[] splittedName = DataLoader.latestContainer.split("[.]");
        String shortName = splittedName[splittedName.length - 1];
        switch(arg) {
        case HELP:
            EnumChatMessages.COMMAND_HELP.sendMessage(player);
            break;
        case ENABLE:
            DataLoader.setSettingsDisabled(false);
            if (DataLoader.isClientSyncEnabled())
                for (EntityPlayerMP playerMP : CommonReference.getPlayersServer())
                    NetworkHandler.sendToPlayer(new CPSyncContainers(DataLoader.CONTAINERS_SERVER), playerMP);
            EnumChatMessages.SETTINGS_ENABLED.sendMessage(player);
            break;
        case DISABLE:
            DataLoader.setSettingsDisabled(true);
            if (DataLoader.isClientSyncEnabled())
                for (EntityPlayerMP playerMP : CommonReference.getPlayersServer())
                    NetworkHandler.sendToPlayer(new CPSyncContainers(DataLoader.EMPTY_SET), playerMP);
            EnumChatMessages.SETTINGS_DISABLED.sendMessage(player);
            break;  
        case STATUS:
            EnumChatMessages.STATUS.sendMessage(player, String.valueOf(DataLoader.isSettingsDisabled()));
            break;  
        case ENABLE_CONFIG:
            DataLoader.setConfigModeEnabled(true);
            EnumChatMessages.CONFIGURATION_ENABLED.sendMessage(player);
            break;
        case DISABLE_CONFIG:
            DataLoader.setConfigModeEnabled(false);
            DataLoader.latestContainer = "";
            EnumChatMessages.CONFIGURATION_DISABLED.sendMessage(player);
            break;	
        case SETTINGS:
            EnumChatMessages.SETTINGS_LIST.sendMessage(player);
            break;
        case LATEST:
            if (!DataLoader.isConfigModeEnabled()) {
                EnumChatMessages.NEED_ENABLE_CONFIGURATION.sendMessage(player);
                return;
            } else if (DataLoader.latestContainer.isEmpty()) {   
                EnumChatMessages.NO_LATEST_CONTAINER.sendMessage(player);
                return;
            }
            EnumChatMessages.LATEST_CONTAINER.sendMessage(player, DataLoader.latestContainer);
            break;
        case DENY:
            if (!DataLoader.isConfigModeEnabled()) {
                EnumChatMessages.NEED_ENABLE_CONFIGURATION.sendMessage(player);
                return;
            } else if (DataLoader.latestContainer.isEmpty()) {   
                EnumChatMessages.NO_LATEST_CONTAINER.sendMessage(player);
                return;
            }
            DataLoader.CONTAINERS_SERVER.add(DataLoader.latestContainer);
            if (DataLoader.isClientSyncEnabled())
                NetworkHandler.sendToPlayer(new CPSyncContainers(DataLoader.CONTAINERS_SERVER), (EntityPlayerMP) player);
            EnumChatMessages.QUICK_MOVE_DENIED.sendMessage(player, shortName);
            break;
        case ALLOW:
            if (!DataLoader.isConfigModeEnabled()) {
                EnumChatMessages.NEED_ENABLE_CONFIGURATION.sendMessage(player);
                return;
            } else if (DataLoader.latestContainer.isEmpty()) {   
                EnumChatMessages.NO_LATEST_CONTAINER.sendMessage(player);
                return;
            }
            DataLoader.CONTAINERS_SERVER.remove(DataLoader.latestContainer);
            if (DataLoader.isClientSyncEnabled())
                NetworkHandler.sendToPlayer(new CPSyncContainers(DataLoader.CONTAINERS_SERVER), (EntityPlayerMP) player);
            EnumChatMessages.QUICK_MOVE_ALLOWED.sendMessage(player, shortName);
            break;
        case CLEAR_ALL:
            if (!DataLoader.isConfigModeEnabled()) {
                EnumChatMessages.NEED_ENABLE_CONFIGURATION.sendMessage(player);
                return;
            } else if (DataLoader.CONTAINERS_SERVER.isEmpty()) {
                EnumChatMessages.NO_DATA.sendMessage(player);
                return;
            }
            DataLoader.CONTAINERS_SERVER.clear();
            DataLoader.saveSettings();
            if (DataLoader.isClientSyncEnabled())
                for (EntityPlayerMP playerMP : CommonReference.getPlayersServer())
                    NetworkHandler.sendToPlayer(new CPSyncContainers(DataLoader.CONTAINERS_SERVER), playerMP);
            EnumChatMessages.PLANTS_LIST_CLEARED.sendMessage(player);
            break;
        case SAVE:
            if (!DataLoader.isConfigModeEnabled()) {
                EnumChatMessages.NEED_ENABLE_CONFIGURATION.sendMessage(player);
                return;
            }
            DataLoader.saveSettings();
            if (DataLoader.isClientSyncEnabled())
                for (EntityPlayerMP playerMP : CommonReference.getPlayersServer())
                    NetworkHandler.sendToPlayer(new CPSyncContainers(DataLoader.CONTAINERS_SERVER), playerMP);
            EnumChatMessages.SETTINGS_SAVED.sendMessage(player);
            break;
        case BACKUP:
            if (!DataLoader.isConfigModeEnabled()) {
                EnumChatMessages.NEED_ENABLE_CONFIGURATION.sendMessage(player);
                return;
            }
            DataLoader.backupSettings();
            EnumChatMessages.BACKUP_CREATED.sendMessage(player);
            break;
        }
    }
}


