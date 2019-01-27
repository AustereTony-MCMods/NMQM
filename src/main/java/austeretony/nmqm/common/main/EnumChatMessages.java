package austeretony.nmqm.common.main;

import austeretony.nmqm.client.reference.ClientReference;
import austeretony.nmqm.common.commands.EnumCommandNMQMArgs;
import austeretony.nmqm.common.network.NetworkHandler;
import austeretony.nmqm.common.network.client.CPSendMessage;
import austeretony.nmqm.common.reference.CommonReference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public enum EnumChatMessages {

    UPDATE_MESSAGE {

        @Override
        public void sendMessage(EntityPlayer player, String... args) {
            NetworkHandler.sendToPlayer(new CPSendMessage(this,  args), (EntityPlayerMP) player);
        }

        @Override
        public void sendMesssageClient(String... args) {
            IChatComponent msg1, msg2, msg3;
            msg1 = new ChatComponentTranslation("nmqm.update.newVersion");
            msg2 = new ChatComponentText(" [" + NMQMMain.VERSION + "/" + args[0] + "]");        
            ClientReference.sendMessage(prefix().appendSibling(msg1).appendSibling(msg2));
            msg1 = new ChatComponentTranslation("nmqm.update.projectPage");
            msg2 = new ChatComponentText(": ");
            msg3 = new ChatComponentText(NMQMMain.PROJECT_LOCATION);   
            msg1.getChatStyle().setColor(EnumChatFormatting.AQUA);      
            msg3.getChatStyle().setColor(EnumChatFormatting.WHITE);                             
            msg3.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, NMQMMain.PROJECT_URL));             
            ClientReference.sendMessage(msg1.appendSibling(msg2).appendSibling(msg3));     
        }
    },
    COMMAND_HELP {

        @Override
        public void sendMessage(EntityPlayer player, String... args) {
            NetworkHandler.sendToPlayer(new CPSendMessage(this,  args), (EntityPlayerMP) player);
        }

        @Override
        public void sendMesssageClient(String... args) {      
            IChatComponent msg1, msg2;
            ClientReference.sendMessage(prefix().appendSibling(new ChatComponentTranslation("nmqm.command.help.title")));
            for (EnumCommandNMQMArgs arg : EnumCommandNMQMArgs.values()) {
                if (arg != EnumCommandNMQMArgs.HELP) {
                    msg1 = new ChatComponentText("/nmqm " + arg);
                    msg2 = new ChatComponentText(" - ");
                    msg1.getChatStyle().setColor(EnumChatFormatting.GREEN);  
                    msg2.getChatStyle().setColor(EnumChatFormatting.WHITE); 
                    ClientReference.sendMessage(msg1.appendSibling(msg2.appendSibling(new ChatComponentTranslation("nmqm.command.help." + arg))));
                }
            }
        }
    },
    SETTINGS_ENABLED {

        @Override
        public void sendMessage(EntityPlayer player, String... args) {
            NetworkHandler.sendToPlayer(new CPSendMessage(this,  args), (EntityPlayerMP) player);
        }

        @Override
        public void sendMesssageClient(String... args) {
            ClientReference.sendMessage(prefix().appendSibling(new ChatComponentTranslation("nmqm.command.enable")));
        }    
    },
    SETTINGS_DISABLED {

        @Override
        public void sendMessage(EntityPlayer player, String... args) {
            NetworkHandler.sendToPlayer(new CPSendMessage(this,  args), (EntityPlayerMP) player);
        }

        @Override
        public void sendMesssageClient(String... args) {
            ClientReference.sendMessage(prefix().appendSibling(new ChatComponentTranslation("nmqm.command.disable")));
        }   
    },
    STATUS {

        @Override
        public void sendMessage(EntityPlayer player, String... args) {
            NetworkHandler.sendToPlayer(new CPSendMessage(this,  args), (EntityPlayerMP) player);
        }

        @Override
        public void sendMesssageClient(String... args) {            
            IChatComponent msg1;
            boolean flag = Boolean.parseBoolean(args[0]);
            msg1 = flag ? new ChatComponentTranslation("nmqm.status.disabled") : new ChatComponentTranslation("nmqm.status.enabled");
            msg1.getChatStyle().setColor(flag ? EnumChatFormatting.RED : EnumChatFormatting.GREEN);        
            ClientReference.sendMessage(prefix().appendSibling(new ChatComponentTranslation("nmqm.command.status").appendSibling(new ChatComponentText(": ")).appendSibling(msg1)));     
        }   
    },
    CONFIGURATION_ENABLED {

        @Override
        public void sendMessage(EntityPlayer player, String... args) {
            NetworkHandler.sendToPlayer(new CPSendMessage(this,  args), (EntityPlayerMP) player);
        }

        @Override
        public void sendMesssageClient(String... args) {
            ClientReference.sendMessage(prefix().appendSibling(new ChatComponentTranslation("nmqm.command.enable.conf")));
        }    
    },
    CONFIGURATION_DISABLED {

        @Override
        public void sendMessage(EntityPlayer player, String... args) {
            NetworkHandler.sendToPlayer(new CPSendMessage(this,  args), (EntityPlayerMP) player);
        }

        @Override
        public void sendMesssageClient(String... args) {
            ClientReference.sendMessage(prefix().appendSibling(new ChatComponentTranslation("nmqm.command.disable.conf")));
        }   
    },
    SETTINGS_LIST {

        @Override
        public void sendMessage(EntityPlayer player, String... args) {
            STATUS.sendMessage(player, String.valueOf(DataLoader.isSettingsDisabled()));
            IChatComponent msg1, msg2, msg3;
            CommonReference.sendMessage(player, prefix().appendSibling(new ChatComponentTranslation("nmqm.command.settings")));
            if (DataLoader.CONTAINERS_SERVER.isEmpty())
                CommonReference.sendMessage(player, new ChatComponentTranslation("nmqm.command.settings.empty"));
            for (String container : DataLoader.CONTAINERS_SERVER)
                CommonReference.sendMessage(player, new ChatComponentTranslation(" - " + container));
        }

        @Override
        public void sendMesssageClient(String... args) {}  
    },
    LATEST_CONTAINER {

        @Override
        public void sendMessage(EntityPlayer player, String... args) {
            NetworkHandler.sendToPlayer(new CPSendMessage(this,  args), (EntityPlayerMP) player);
        }

        @Override
        public void sendMesssageClient(String... args) {
            IChatComponent containerName, msg1, msg2;
            msg1 = new ChatComponentTranslation("nmqm.command.latest");
            msg2 = new ChatComponentText(": ");
            containerName = new ChatComponentText(args[0]);                          
            containerName.getChatStyle().setColor(EnumChatFormatting.WHITE);  
            ClientReference.sendMessage(prefix().appendSibling(msg1).appendSibling(msg2).appendSibling(containerName));
        }  
    },
    NEED_ENABLE_CONFIGURATION {

        @Override
        public void sendMessage(EntityPlayer player, String... args) {
            NetworkHandler.sendToPlayer(new CPSendMessage(this,  args), (EntityPlayerMP) player);
        }

        @Override
        public void sendMesssageClient(String... args) {
            IChatComponent msg1;
            msg1 = new ChatComponentTranslation("nmqm.command.err.debugMode");                        
            msg1.getChatStyle().setColor(EnumChatFormatting.RED);                       
            ClientReference.sendMessage(prefix().appendSibling(msg1));    
        }
    },
    NO_LATEST_CONTAINER {

        @Override
        public void sendMessage(EntityPlayer player, String... args) {
            NetworkHandler.sendToPlayer(new CPSendMessage(this,  args), (EntityPlayerMP) player);
        }

        @Override
        public void sendMesssageClient(String... args) {
            IChatComponent msg1;
            msg1 = new ChatComponentTranslation("nmqm.command.latest.notExist");                   
            msg1.getChatStyle().setColor(EnumChatFormatting.RED);                       
            ClientReference.sendMessage(prefix().appendSibling(msg1));    
        }
    },
    QUICK_MOVE_DENIED {

        @Override
        public void sendMessage(EntityPlayer player, String... args) {
            NetworkHandler.sendToPlayer(new CPSendMessage(this,  args), (EntityPlayerMP) player);
        }

        @Override
        public void sendMesssageClient(String... args) {
            IChatComponent shortName, msg1, msg2;
            msg1 = new ChatComponentTranslation("nmqm.command.deny");
            msg2 = new ChatComponentText(": ");
            shortName = new ChatComponentText(args[0]);
            shortName.getChatStyle().setColor(EnumChatFormatting.WHITE);  
            ClientReference.sendMessage(prefix().appendSibling(msg1).appendSibling(msg2).appendSibling(shortName));
        }
    },
    QUICK_MOVE_ALLOWED {

        @Override
        public void sendMessage(EntityPlayer player, String... args) {
            NetworkHandler.sendToPlayer(new CPSendMessage(this,  args), (EntityPlayerMP) player);
        }

        @Override
        public void sendMesssageClient(String... args) {
            IChatComponent shortName, msg1, msg2;
            msg1 = new ChatComponentTranslation("nmqm.command.allow");
            msg2 = new ChatComponentText(": ");
            shortName = new ChatComponentText(args[0]);
            shortName.getChatStyle().setColor(EnumChatFormatting.WHITE);  
            ClientReference.sendMessage(prefix().appendSibling(msg1).appendSibling(msg2).appendSibling(shortName)); 
        }
    },
    CONTAINER_ABSENT {

        @Override
        public void sendMessage(EntityPlayer player, String... args) {
            NetworkHandler.sendToPlayer(new CPSendMessage(this,  args), (EntityPlayerMP) player);
        }

        @Override
        public void sendMesssageClient(String... args) {
            IChatComponent msg1;
            msg1 = new ChatComponentTranslation("nmqm.command.allow.absent");                     
            msg1.getChatStyle().setColor(EnumChatFormatting.RED);                       
            ClientReference.sendMessage(prefix().appendSibling(msg1));    
        }
    },
    NO_DATA {

        @Override
        public void sendMessage(EntityPlayer player, String... args) {
            NetworkHandler.sendToPlayer(new CPSendMessage(this,  args), (EntityPlayerMP) player);
        }

        @Override
        public void sendMesssageClient(String... args) {   
            IChatComponent msg1;
            msg1 = new ChatComponentTranslation("nmqm.command.noData");                    
            msg1.getChatStyle().setColor(EnumChatFormatting.RED);    
            ClientReference.sendMessage(prefix().appendSibling(msg1));    
        }
    },
    PLANTS_LIST_CLEARED {

        @Override
        public void sendMessage(EntityPlayer player, String... args) {
            NetworkHandler.sendToPlayer(new CPSendMessage(this,  args), (EntityPlayerMP) player);
        }

        @Override
        public void sendMesssageClient(String... args) {                    
            ClientReference.sendMessage(prefix().createCopy().appendSibling(new ChatComponentTranslation("nmqm.command.clear.all")));    
        }
    },
    SETTINGS_SAVED {

        @Override
        public void sendMessage(EntityPlayer player, String... args) {
            NetworkHandler.sendToPlayer(new CPSendMessage(this,  args), (EntityPlayerMP) player);
        }

        @Override
        public void sendMesssageClient(String... args) {                    
            ClientReference.sendMessage(prefix().appendSibling(new ChatComponentTranslation("nmqm.command.save")));    
        }
    },
    BACKUP_CREATED {

        @Override
        public void sendMessage(EntityPlayer player, String... args) {
            NetworkHandler.sendToPlayer(new CPSendMessage(this,  args), (EntityPlayerMP) player);
        }

        @Override
        public void sendMesssageClient(String... args) {                    
            ClientReference.sendMessage(prefix().appendSibling(new ChatComponentTranslation("nmqm.command.backup")));    
        }
    };

    private static int index;

    public final int id;

    EnumChatMessages() {
        this.id = createId();
    }

    private int createId() {
        return index++;
    }

    public static final IChatComponent PREFIX;

    static {

        PREFIX = new ChatComponentText("[NMQM] ");
        PREFIX.getChatStyle().setColor(EnumChatFormatting.AQUA);                   
    }

    private static IChatComponent prefix() {
        return PREFIX.createCopy();
    }

    public abstract void sendMessage(EntityPlayer player, String... args);

    @SideOnly(Side.CLIENT)
    public abstract void sendMesssageClient(String... args);
}
