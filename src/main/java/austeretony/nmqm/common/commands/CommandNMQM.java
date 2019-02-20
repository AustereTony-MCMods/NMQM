package austeretony.nmqm.common.commands;

import austeretony.nmqm.common.config.ConfigLoader;
import austeretony.nmqm.common.config.EnumConfigSettings;
import austeretony.nmqm.common.main.EnumChatMessages;
import austeretony.nmqm.common.network.NetworkHandler;
import austeretony.nmqm.common.network.client.CPSyncData;
import austeretony.nmqm.common.reference.CommonReference;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandNMQM extends CommandBase {

    public static final String 
    NAME = "nmqm",
    USAGE = "/nmqm <arg>, type </nmqm help> for available arguments.";

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
        EnumCommandNMQMArgs arg;
        if (args.length != 1 || (arg = EnumCommandNMQMArgs.get(args[0])) == null)  
            throw new WrongUsageException(this.getUsage(sender));   
        EntityPlayer player = getCommandSenderAsPlayer(sender);		
        String[] splittedName = ConfigLoader.latestContainer.split("[.]");
        String shortName = splittedName[splittedName.length - 1];
        switch(arg) {
        case HELP:
            EnumChatMessages.COMMAND_NMQM_HELP.sendMessage(player);
            break;
        case ENABLE:
            ConfigLoader.setSettingsEnabled(true);
            if (EnumConfigSettings.EXTERNAL_CONFIG.isEnabled())
                this.save();
            this.sync(CPSyncData.EnumAction.SYNC_STATUS);
            this.sync(CPSyncData.EnumAction.SYNC_LIST);
            EnumChatMessages.COMMAND_NMQM_ENABLE.sendMessage(player);
            break;
        case DISABLE:
            ConfigLoader.setSettingsEnabled(false);
            if (EnumConfigSettings.EXTERNAL_CONFIG.isEnabled())
                this.save();
            this.sync(CPSyncData.EnumAction.SYNC_STATUS);
            EnumChatMessages.COMMAND_NMQM_DISABLE.sendMessage(player);
            break;  
        case STATUS:
            EnumChatMessages.COMMAND_NMQM_STATUS.sendMessage(player);
            break;  
        case ENABLE_CONFIG:
            if (!this.validAction(player, false, false, false, false)) break;
            ConfigLoader.setConfigModeEnabled(true);
            EnumChatMessages.COMMAND_NMQM_ENABLE_CONF.sendMessage(player);
            break;
        case DISABLE_CONFIG:
            if (!this.validAction(player, false, false, false, false)) break;
            ConfigLoader.setConfigModeEnabled(false);
            ConfigLoader.latestContainer = "";
            EnumChatMessages.COMMAND_NMQM_DISABLE_CONF.sendMessage(player);
            break;  
        case LIST:
            EnumChatMessages.COMMAND_NMQM_LIST.sendMessage(player);
            break;
        case LATEST:
            if (!this.validAction(player, true, true, false, false)) break;
            EnumChatMessages.COMMAND_NMQM_LATEST.sendMessage(player, shortName);
            break;
        case DENY:
            if (!this.validAction(player, true, true, false, false))  break;
            ConfigLoader.containersServer.add(ConfigLoader.latestContainer);
            this.save();
            this.sync(CPSyncData.EnumAction.SYNC_LATEST);
            EnumChatMessages.COMMAND_NMQM_DENY.sendMessage(player, shortName);
            break;
        case ALLOW:
            if (!this.validAction(player, true, true, true, false)) break;
            ConfigLoader.containersServer.remove(ConfigLoader.latestContainer);
            this.save();
            this.sync(CPSyncData.EnumAction.REMOVE_LATEST);
            EnumChatMessages.COMMAND_NMQM_ALLOW.sendMessage(player, shortName);
            break;
        case CLEAR_ALL:
            if (!this.validAction(player, true, false, false, true)) break; 
            ConfigLoader.containersServer.clear();
            ConfigLoader.save();
            this.sync(CPSyncData.EnumAction.CLEAR_LIST);
            EnumChatMessages.COMMAND_NMQM_CLEAR_ALL.sendMessage(player);
            break;
        case SAVE:
            if (!this.validAction(player, true, false, false, true)) break;
            ConfigLoader.save();
            EnumChatMessages.COMMAND_NMQM_SAVE.sendMessage(player);
            break;
        case RELOAD:
            if (!this.validAction(player, true, false, false, false)) break;
            ConfigLoader.init();
            this.sync(CPSyncData.EnumAction.SYNC_STATUS);
            this.sync(CPSyncData.EnumAction.SYNC_MODE);
            this.sync(CPSyncData.EnumAction.SYNC_LIST);
            EnumChatMessages.COMMAND_NMQM_RELOAD.sendMessage(player);
            break;
        case BACKUP:
            if (!this.validAction(player, true, false, false, true)) break;
            ConfigLoader.backup();
            EnumChatMessages.COMMAND_NMQM_BACKUP.sendMessage(player);
            break;
        }
    }

    private boolean validAction(EntityPlayer player, boolean checkMode, boolean checkLatest, boolean checkDataLatest, boolean checkDataAll) {
        if (!EnumConfigSettings.EXTERNAL_CONFIG.isEnabled()) {
            EnumChatMessages.COMMAND_NMQM_ERR_EXTERNAL_CONFIG_DISABLED.sendMessage(player);
            return false;
        } else if (checkMode && !ConfigLoader.isConfigModeEnabled()) {
            EnumChatMessages.COMMAND_NMQM_ERR_NEED_ENABLE_CONFIGURATION.sendMessage(player);
            return false;
        } else if (checkLatest && ConfigLoader.latestContainer.isEmpty()) {   
            EnumChatMessages.COMMAND_NMQM_ERR_NO_LATEST.sendMessage(player);
            return false;
        } else if (checkDataLatest && !ConfigLoader.containersServer.contains(ConfigLoader.latestContainer)) {
            EnumChatMessages.COMMAND_NMQM_ERR_CONTAINER_ABSENT.sendMessage(player);
            return false;
        } else if (checkDataAll && ConfigLoader.containersServer.isEmpty()) {
            EnumChatMessages.COMMAND_NMQM_ERR_NO_DATA.sendMessage(player);
            return false;
        }
        return true;
    }

    private void save() {
        if (EnumConfigSettings.AUTOSAVE.isEnabled())
            ConfigLoader.save();
    }

    private void sync(CPSyncData.EnumAction enumAction) {
        if (!EnumConfigSettings.SERVER_ONLY.isEnabled()) 
            for (EntityPlayerMP playerMP : CommonReference.getPlayersListServer()) 
                NetworkHandler.sendTo(new CPSyncData(enumAction), playerMP);
    }
}


