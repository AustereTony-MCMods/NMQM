package austeretony.nmqm.common.core;

import austeretony.nmqm.common.main.EnumNMQMChatMessages;
import austeretony.nmqm.common.main.NMQMDataLoader;
import austeretony.nmqm.common.main.NMQMMain;
import austeretony.nmqm.common.origin.CommonReference;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class NMQMHooks {
	
	public static void getContainerClassName(Container container, EntityPlayer player) {		
		if (NMQMDataLoader.isConfigModeEnabled() && CommonReference.isOpped(player) && !player.world.isRemote) {			
			String containerClassName = container.getClass().getName();						
			if (!NMQMDataLoader.latestContainer.equals(containerClassName)) {				
				NMQMDataLoader.latestContainer = containerClassName;
				NMQMMain.showMessage(player, EnumNMQMChatMessages.LATEST_CONTAINER, containerClassName);
			}
		}
	}
	
	public static ClickType verifyQuickMoveClient(ClickType clickType) {		
		if (clickType == ClickType.QUICK_MOVE) {						
			String containerClassname = Minecraft.getMinecraft().player.openContainer.getClass().getName();		
			if (NMQMDataLoader.getMode() == 0) {				
				if (NMQMDataLoader.CONTAINERS_CLIENT.contains(containerClassname))
					clickType = ClickType.PICKUP;
			} else if (NMQMDataLoader.getMode() == 1) {				
				if (!NMQMDataLoader.CONTAINERS_CLIENT.contains(containerClassname))
					clickType = ClickType.PICKUP;
			}
		}		
		return clickType;
	}
	
	public static ClickType verifyQuickMoveServer(INetHandlerPlayServer handler, ClickType clickType) {				
		if (clickType == ClickType.QUICK_MOVE) {			
			EntityPlayer player = ((NetHandlerPlayServer) handler).playerEntity;			
			String containerClassname = player.openContainer.getClass().getName();			
			if (NMQMDataLoader.getMode() == 0) {				
				if (NMQMDataLoader.CONTAINERS_SERVER.contains(containerClassname))
					clickType = ClickType.PICKUP;
			} else if (NMQMDataLoader.getMode() == 1) {				
				if (!NMQMDataLoader.CONTAINERS_SERVER.contains(containerClassname))
					clickType = ClickType.PICKUP;
			}
		}		
		return clickType;
	}
}
