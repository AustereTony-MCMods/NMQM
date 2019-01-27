package austeretony.nmqm.common.main;

import austeretony.nmqm.client.reference.ClientReference;
import austeretony.nmqm.common.commands.EnumCommandNMQMArgs;
import austeretony.nmqm.common.network.NetworkHandler;
import austeretony.nmqm.common.network.client.CPSendMessage;
import austeretony.nmqm.common.reference.CommonReference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum EnumChatMessages {

    UPDATE_MESSAGE {

        @Override
        public void sendMessage(EntityPlayer player, String... args) {
            NetworkHandler.sendToPlayer(new CPSendMessage(this,  args), (EntityPlayerMP) player);
        }

        @Override
        public void sendMesssageClient(String... args) {
            ITextComponent msg1, msg2, msg3;
            msg1 = new TextComponentTranslation("nmqm.update.newVersion");
            msg2 = new TextComponentString(" [" + NMQMMain.VERSION + "/" + args[0] + "]");        
            ClientReference.sendMessage(prefix().appendSibling(msg1).appendSibling(msg2));
            msg1 = new TextComponentTranslation("nmqm.update.projectPage");
            msg2 = new TextComponentString(": ");
            msg3 = new TextComponentString(NMQMMain.PROJECT_LOCATION);   
            msg1.getStyle().setColor(TextFormatting.AQUA);      
            msg3.getStyle().setColor(TextFormatting.WHITE);                             
            msg3.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, NMQMMain.PROJECT_URL));             
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
            ITextComponent msg1, msg2;
            ClientReference.sendMessage(prefix().appendSibling(new TextComponentTranslation("nmqm.command.help.title")));
            for (EnumCommandNMQMArgs arg : EnumCommandNMQMArgs.values()) {
                if (arg != EnumCommandNMQMArgs.HELP) {
                    msg1 = new TextComponentString("/nmqm " + arg);
                    msg2 = new TextComponentString(" - ");
                    msg1.getStyle().setColor(TextFormatting.GREEN);  
                    msg2.getStyle().setColor(TextFormatting.WHITE); 
                    ClientReference.sendMessage(msg1.appendSibling(msg2.appendSibling(new TextComponentTranslation("nmqm.command.help." + arg))));
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
            ClientReference.sendMessage(prefix().appendSibling(new TextComponentTranslation("nmqm.command.enable")));
        }    
    },
    SETTINGS_DISABLED {

        @Override
        public void sendMessage(EntityPlayer player, String... args) {
            NetworkHandler.sendToPlayer(new CPSendMessage(this,  args), (EntityPlayerMP) player);
        }

        @Override
        public void sendMesssageClient(String... args) {
            ClientReference.sendMessage(prefix().appendSibling(new TextComponentTranslation("nmqm.command.disable")));
        }   
    },
    STATUS {

        @Override
        public void sendMessage(EntityPlayer player, String... args) {
            NetworkHandler.sendToPlayer(new CPSendMessage(this,  args), (EntityPlayerMP) player);
        }

        @Override
        public void sendMesssageClient(String... args) {            
            ITextComponent msg1;
            boolean flag = Boolean.parseBoolean(args[0]);
            msg1 = flag ? new TextComponentTranslation("nmqm.status.disabled") : new TextComponentTranslation("nmqm.status.enabled");
            msg1.getStyle().setColor(flag ? TextFormatting.RED : TextFormatting.GREEN);        
            ClientReference.sendMessage(prefix().appendSibling(new TextComponentTranslation("nmqm.command.status").appendSibling(new TextComponentString(": ")).appendSibling(msg1)));     
        }   
    },
    CONFIGURATION_ENABLED {

        @Override
        public void sendMessage(EntityPlayer player, String... args) {
            NetworkHandler.sendToPlayer(new CPSendMessage(this,  args), (EntityPlayerMP) player);
        }

        @Override
        public void sendMesssageClient(String... args) {
            ClientReference.sendMessage(prefix().appendSibling(new TextComponentTranslation("nmqm.command.enable.conf")));
        }    
    },
    CONFIGURATION_DISABLED {

        @Override
        public void sendMessage(EntityPlayer player, String... args) {
            NetworkHandler.sendToPlayer(new CPSendMessage(this,  args), (EntityPlayerMP) player);
        }

        @Override
        public void sendMesssageClient(String... args) {
            ClientReference.sendMessage(prefix().appendSibling(new TextComponentTranslation("nmqm.command.disable.conf")));
        }   
    },
    SETTINGS_LIST {

        @Override
        public void sendMessage(EntityPlayer player, String... args) {
            STATUS.sendMessage(player, String.valueOf(DataLoader.isSettingsDisabled()));
            ITextComponent msg1, msg2, msg3;
            CommonReference.sendMessage(player, prefix().appendSibling(new TextComponentTranslation("nmqm.command.settings")));
            if (DataLoader.CONTAINERS_SERVER.isEmpty())
                CommonReference.sendMessage(player, new TextComponentTranslation("nmqm.command.settings.empty"));
            for (String container : DataLoader.CONTAINERS_SERVER)
                CommonReference.sendMessage(player, new TextComponentTranslation(" - " + container));
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
            ITextComponent containerName, msg1, msg2;
            msg1 = new TextComponentTranslation("nmqm.command.latest");
            msg2 = new TextComponentString(": ");
            containerName = new TextComponentString(args[0]);                          
            containerName.getStyle().setColor(TextFormatting.WHITE);  
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
            ITextComponent msg1;
            msg1 = new TextComponentTranslation("nmqm.command.err.debugMode");                        
            msg1.getStyle().setColor(TextFormatting.RED);                       
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
            ITextComponent msg1;
            msg1 = new TextComponentTranslation("nmqm.command.latest.notExist");                   
            msg1.getStyle().setColor(TextFormatting.RED);                       
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
            ITextComponent shortName, msg1, msg2;
            msg1 = new TextComponentTranslation("nmqm.command.deny");
            msg2 = new TextComponentString(": ");
            shortName = new TextComponentString(args[0]);
            shortName.getStyle().setColor(TextFormatting.WHITE);  
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
            ITextComponent shortName, msg1, msg2;
            msg1 = new TextComponentTranslation("nmqm.command.allow");
            msg2 = new TextComponentString(": ");
            shortName = new TextComponentString(args[0]);
            shortName.getStyle().setColor(TextFormatting.WHITE);  
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
            ITextComponent msg1;
            msg1 = new TextComponentTranslation("nmqm.command.allow.absent");                     
            msg1.getStyle().setColor(TextFormatting.RED);                       
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
            ITextComponent msg1;
            msg1 = new TextComponentTranslation("nmqm.command.noData");                    
            msg1.getStyle().setColor(TextFormatting.RED);    
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
            ClientReference.sendMessage(prefix().createCopy().appendSibling(new TextComponentTranslation("nmqm.command.clear.all")));    
        }
    },
    SETTINGS_SAVED {

        @Override
        public void sendMessage(EntityPlayer player, String... args) {
            NetworkHandler.sendToPlayer(new CPSendMessage(this,  args), (EntityPlayerMP) player);
        }

        @Override
        public void sendMesssageClient(String... args) {                    
            ClientReference.sendMessage(prefix().appendSibling(new TextComponentTranslation("nmqm.command.save")));    
        }
    },
    BACKUP_CREATED {

        @Override
        public void sendMessage(EntityPlayer player, String... args) {
            NetworkHandler.sendToPlayer(new CPSendMessage(this,  args), (EntityPlayerMP) player);
        }

        @Override
        public void sendMesssageClient(String... args) {                    
            ClientReference.sendMessage(prefix().appendSibling(new TextComponentTranslation("nmqm.command.backup")));    
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

    public static final ITextComponent PREFIX;

    static {

        PREFIX = new TextComponentString("[NMQM] ");
        PREFIX.getStyle().setColor(TextFormatting.AQUA);                   
    }

    private static ITextComponent prefix() {
        return PREFIX.createCopy();
    }

    public abstract void sendMessage(EntityPlayer player, String... args);

    @SideOnly(Side.CLIENT)
    public abstract void sendMesssageClient(String... args);
}
