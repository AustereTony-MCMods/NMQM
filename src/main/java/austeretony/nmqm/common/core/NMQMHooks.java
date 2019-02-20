package austeretony.nmqm.common.core;

import austeretony.nmqm.client.reference.ClientReference;
import austeretony.nmqm.common.config.ConfigLoader;
import austeretony.nmqm.common.config.EnumConfigSettings;
import austeretony.nmqm.common.main.EnumChatMessages;
import austeretony.nmqm.common.reference.CommonReference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class NMQMHooks {

    public static void getContainerClassName(Container container, EntityPlayer player) {		
        if (ConfigLoader.isConfigModeEnabled() && CommonReference.isOpped(player) && !player.world.isRemote) {			
            String name = container.getClass().getName();						
            if (!ConfigLoader.latestContainer.equals(name)) {				
                ConfigLoader.latestContainer = name;
                EnumChatMessages.COMMAND_NMQM_LATEST.sendMessage(player, name);
            }
        }
    }

    public static ClickType verifyQuickMoveClient(ClickType clickType) {
        if (!ConfigLoader.isSettingsEnabledClient())
            return clickType;
        if (clickType == ClickType.QUICK_MOVE) {
            String containerClassname = ClientReference.getClientPlayer().openContainer.getClass().getName();		
            if (ConfigLoader.getModeClient() == 0) {				
                if (ConfigLoader.containersClient.contains(containerClassname))
                    clickType = ClickType.PICKUP;
            } else {				
                if (!ConfigLoader.containersClient.contains(containerClassname))
                    clickType = ClickType.PICKUP;
            }
        }		
        return clickType;
    }

    public static ClickType verifyQuickMoveServer(INetHandlerPlayServer handler, ClickType clickType) {	
        if (!ConfigLoader.isSettingsEnabled())
            return clickType;
        if (clickType == ClickType.QUICK_MOVE) {	
            EntityPlayer player = ((NetHandlerPlayServer) handler).player;	
            String containerClassname = player.openContainer.getClass().getName();			
            if (EnumConfigSettings.MODE.getIntValue() == 0) {				
                if (ConfigLoader.containersServer.contains(containerClassname))
                    clickType = ClickType.PICKUP;
            } else {				
                if (!ConfigLoader.containersServer.contains(containerClassname))
                    clickType = ClickType.PICKUP;
            }
        }		
        return clickType;
    }
}
