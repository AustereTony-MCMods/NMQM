package austeretony.nmqm.common.core;

import austeretony.nmqm.common.main.DataLoader;
import austeretony.nmqm.common.main.EnumChatMessages;
import austeretony.nmqm.common.reference.CommonReference;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class NMQMHooks {

    public static void getContainerClassName(Container container, EntityPlayer player) {		
        if (DataLoader.isConfigModeEnabled() && CommonReference.isOpped(player) && !player.worldObj.isRemote) {			
            String containerClassName = container.getClass().getName();						
            if (!DataLoader.latestContainer.equals(containerClassName)) {				
                DataLoader.latestContainer = containerClassName;
                EnumChatMessages.LATEST_CONTAINER.sendMessage(player, containerClassName);
            }
        }
    }

    public static int verifyQuickMoveClient(int clickType) {  
        if (clickType == 1) {                                           
            String containerClassname = Minecraft.getMinecraft().thePlayer.openContainer.getClass().getName();                      
            if (DataLoader.getMode() == 0) {                            
                if (DataLoader.CONTAINERS_CLIENT.contains(containerClassname))
                    clickType = 0;
            } else if (DataLoader.getMode() == 1) {                             
                if (!DataLoader.CONTAINERS_CLIENT.contains(containerClassname))
                    clickType = 0;
            }
        }               
        return clickType;
    }

    public static int verifyQuickMoveServer(INetHandlerPlayServer handler, int clickType) {    
        if (DataLoader.isSettingsDisabled())
            return clickType;
        if (clickType == 1) {                   
            EntityPlayer player = ((NetHandlerPlayServer) handler).playerEntity;                    
            String containerClassname = player.openContainer.getClass().getName();                  
            if (DataLoader.getMode() == 0) {                            
                if (DataLoader.CONTAINERS_SERVER.contains(containerClassname))
                    clickType = 0;
            } else if (DataLoader.getMode() == 1) {                             
                if (!DataLoader.CONTAINERS_SERVER.contains(containerClassname))
                    clickType = 0;
            }
        }               
        return clickType;
    }
}
