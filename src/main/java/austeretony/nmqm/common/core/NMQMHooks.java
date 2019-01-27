package austeretony.nmqm.common.core;

import austeretony.nmqm.common.main.DataLoader;
import austeretony.nmqm.common.main.EnumChatMessages;
import austeretony.nmqm.common.reference.CommonReference;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
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

    public static ClickType verifyQuickMoveClient(ClickType clickType) {
        if (clickType == ClickType.QUICK_MOVE) {
            String containerClassname = Minecraft.getMinecraft().thePlayer.openContainer.getClass().getName();		
            if (DataLoader.getMode() == 0) {				
                if (DataLoader.CONTAINERS_CLIENT.contains(containerClassname))
                    clickType = ClickType.PICKUP;
            } else if (DataLoader.getMode() == 1) {				
                if (!DataLoader.CONTAINERS_CLIENT.contains(containerClassname))
                    clickType = ClickType.PICKUP;
            }
        }		
        return clickType;
    }

    public static ClickType verifyQuickMoveServer(INetHandlerPlayServer handler, ClickType clickType) {	
        if (DataLoader.isSettingsDisabled())
            return clickType;
        if (clickType == ClickType.QUICK_MOVE) {	
            EntityPlayer player = ((NetHandlerPlayServer) handler).playerEntity;	
            String containerClassname = player.openContainer.getClass().getName();			
            if (DataLoader.getMode() == 0) {				
                if (DataLoader.CONTAINERS_SERVER.contains(containerClassname))
                    clickType = ClickType.PICKUP;
            } else if (DataLoader.getMode() == 1) {				
                if (!DataLoader.CONTAINERS_SERVER.contains(containerClassname))
                    clickType = ClickType.PICKUP;
            }
        }		
        return clickType;
    }
}
