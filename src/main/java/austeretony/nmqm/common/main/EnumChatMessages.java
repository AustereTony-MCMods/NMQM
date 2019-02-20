package austeretony.nmqm.common.main;

import austeretony.nmqm.common.commands.EnumCommandNMQMArgs;
import austeretony.nmqm.common.config.ConfigLoader;
import austeretony.nmqm.common.reference.CommonReference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;

public enum EnumChatMessages {

    UPDATE_MESSAGE,
    COMMAND_NMQM_HELP,
    COMMAND_NMQM_ENABLE,
    COMMAND_NMQM_DISABLE,
    COMMAND_NMQM_STATUS,
    COMMAND_NMQM_ENABLE_CONF,
    COMMAND_NMQM_DISABLE_CONF,
    COMMAND_NMQM_LIST,
    COMMAND_NMQM_LATEST,
    COMMAND_NMQM_DENY,
    COMMAND_NMQM_ALLOW,
    COMMAND_NMQM_CLEAR_ALL,
    COMMAND_NMQM_SAVE,
    COMMAND_NMQM_RELOAD,
    COMMAND_NMQM_BACKUP,
    COMMAND_NMQM_ERR_EXTERNAL_CONFIG_DISABLED,
    COMMAND_NMQM_ERR_NEED_ENABLE_CONFIGURATION,
    COMMAND_NMQM_ERR_NO_LATEST,
    COMMAND_NMQM_ERR_CONTAINER_ABSENT,
    COMMAND_NMQM_ERR_NO_DATA;

    public static final ITextComponent PREFIX;

    static {
        PREFIX = new TextComponentString("[NMQM] ");
        PREFIX.getStyle().setColor(TextFormatting.AQUA);                   
    }

    private static ITextComponent prefix() {
        return PREFIX.createCopy();
    }

    public void sendMessage(EntityPlayer player, String... args) {
        ITextComponent msg1, msg2, msg3;
        switch (this) {
        case UPDATE_MESSAGE:
            msg1 = new TextComponentTranslation("nmqm.update.newVersion");
            msg2 = new TextComponentString(" [" + NMQMMain.VERSION + "/" + args[0] + "]");        
            CommonReference.sendMessage(player, prefix().appendSibling(msg1).appendSibling(msg2));
            msg1 = new TextComponentTranslation("nmqm.update.projectPage");
            msg2 = new TextComponentString(": ");
            msg3 = new TextComponentString(NMQMMain.PROJECT_LOCATION);   
            msg1.getStyle().setColor(TextFormatting.AQUA);      
            msg3.getStyle().setColor(TextFormatting.WHITE);                             
            msg3.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, NMQMMain.PROJECT_URL));             
            CommonReference.sendMessage(player, msg1.appendSibling(msg2).appendSibling(msg3));    
            break;
        case COMMAND_NMQM_HELP:
            CommonReference.sendMessage(player, prefix().appendSibling(new TextComponentTranslation("nmqm.command.help.title")));
            for (EnumCommandNMQMArgs arg : EnumCommandNMQMArgs.values()) {
                if (arg != EnumCommandNMQMArgs.HELP) {
                    msg1 = new TextComponentString("/nmqm " + arg);
                    msg2 = new TextComponentString(" - ");
                    msg1.getStyle().setColor(TextFormatting.GREEN);  
                    msg2.getStyle().setColor(TextFormatting.WHITE); 
                    CommonReference.sendMessage(player, msg1.appendSibling(msg2.appendSibling(new TextComponentTranslation("nmqm.command.help." + arg))));
                }
            }
            break;
        case COMMAND_NMQM_ENABLE:
            CommonReference.sendMessage(player, prefix().appendSibling(new TextComponentTranslation("nmqm.command.enable")));
            break;
        case COMMAND_NMQM_DISABLE:
            CommonReference.sendMessage(player, prefix().appendSibling(new TextComponentTranslation("nmqm.command.disable")));
            break;
        case COMMAND_NMQM_STATUS:
            msg1 = ConfigLoader.isSettingsEnabled() ? new TextComponentTranslation("nmqm.command.status.enabled") : new TextComponentTranslation("nmqm.command.status.disabled");
            msg1.getStyle().setColor(ConfigLoader.isSettingsEnabled() ? TextFormatting.GREEN : TextFormatting.RED);        
            CommonReference.sendMessage(player, prefix().appendSibling(new TextComponentTranslation("nmqm.command.status").appendSibling(new TextComponentString(": ")).appendSibling(msg1)));
            break;
        case COMMAND_NMQM_ENABLE_CONF:
            CommonReference.sendMessage(player, prefix().appendSibling(new TextComponentTranslation("nmqm.command.enable-conf")));
            break;
        case COMMAND_NMQM_DISABLE_CONF:
            CommonReference.sendMessage(player, prefix().appendSibling(new TextComponentTranslation("nmqm.command.disable-conf")));
            break;
        case COMMAND_NMQM_LIST:
            CommonReference.sendMessage(player, prefix().appendSibling(new TextComponentTranslation("nmqm.command.list")));
            if (ConfigLoader.containersServer.isEmpty())
                CommonReference.sendMessage(player, new TextComponentTranslation("nmqm.command.list.empty"));
            for (String container : ConfigLoader.containersServer)
                CommonReference.sendMessage(player, new TextComponentTranslation(" - " + container));
            break;
        case COMMAND_NMQM_LATEST:
            msg1 = new TextComponentTranslation("nmqm.command.latest");
            msg2 = new TextComponentString(": ");
            msg3 = new TextComponentString(args[0]);                          
            msg3.getStyle().setColor(TextFormatting.WHITE);  
            CommonReference.sendMessage(player, prefix().appendSibling(msg1).appendSibling(msg2).appendSibling(msg3));
            break;
        case COMMAND_NMQM_DENY:
            msg1 = new TextComponentTranslation("nmqm.command.deny");
            msg2 = new TextComponentString(": ");
            msg3 = new TextComponentString(args[0]);
            msg3.getStyle().setColor(TextFormatting.WHITE);  
            CommonReference.sendMessage(player, prefix().appendSibling(msg1).appendSibling(msg2).appendSibling(msg3));
            break;
        case COMMAND_NMQM_ALLOW:
            msg1 = new TextComponentTranslation("nmqm.command.allow");
            msg2 = new TextComponentString(": ");
            msg3 = new TextComponentString(args[0]);
            msg3.getStyle().setColor(TextFormatting.WHITE);  
            CommonReference.sendMessage(player, prefix().appendSibling(msg1).appendSibling(msg2).appendSibling(msg3)); 
            break;
        case COMMAND_NMQM_CLEAR_ALL:
            CommonReference.sendMessage(player, prefix().createCopy().appendSibling(new TextComponentTranslation("nmqm.command.clear-all")));    
            break;
        case COMMAND_NMQM_SAVE:
            CommonReference.sendMessage(player, prefix().appendSibling(new TextComponentTranslation("nmqm.command.save")));    
            break;
        case COMMAND_NMQM_RELOAD:
            CommonReference.sendMessage(player, prefix().appendSibling(new TextComponentTranslation("nmqm.command.reload")));    
            break;
        case COMMAND_NMQM_BACKUP:
            CommonReference.sendMessage(player, prefix().appendSibling(new TextComponentTranslation("nmqm.command.backup")));    
            break;
        case COMMAND_NMQM_ERR_EXTERNAL_CONFIG_DISABLED:
            msg1 = new TextComponentTranslation("nmqm.command.err.externalConfigDisabled");                        
            msg1.getStyle().setColor(TextFormatting.RED);                       
            CommonReference.sendMessage(player, prefix().appendSibling(msg1));   
            break;
        case COMMAND_NMQM_ERR_NEED_ENABLE_CONFIGURATION:
            msg1 = new TextComponentTranslation("nmqm.command.err.debugMode");                        
            msg1.getStyle().setColor(TextFormatting.RED);                       
            CommonReference.sendMessage(player, prefix().appendSibling(msg1));   
            break;
        case COMMAND_NMQM_ERR_NO_LATEST:
            msg1 = new TextComponentTranslation("nmqm.command.err.noLatest");                   
            msg1.getStyle().setColor(TextFormatting.RED);                       
            CommonReference.sendMessage(player, prefix().appendSibling(msg1));  
            break;
        case COMMAND_NMQM_ERR_CONTAINER_ABSENT:
            msg1 = new TextComponentTranslation("nmqm.command.err.latestAbsentInList");                     
            msg1.getStyle().setColor(TextFormatting.RED);                       
            CommonReference.sendMessage(player, prefix().appendSibling(msg1));   
            break;
        case COMMAND_NMQM_ERR_NO_DATA:
            msg1 = new TextComponentTranslation("nmqm.command.err.noData");                    
            msg1.getStyle().setColor(TextFormatting.RED);    
            CommonReference.sendMessage(player, prefix().appendSibling(msg1));    
            break;          
        }
    }
}
