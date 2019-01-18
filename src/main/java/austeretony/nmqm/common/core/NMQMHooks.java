package austeretony.nmqm.common.core;

import austeretony.nmqm.common.events.PlayerLoggedInEvent;
import austeretony.nmqm.common.main.EnumNMQMChatMessages;
import austeretony.nmqm.common.main.NMQMDataLoader;
import austeretony.nmqm.common.main.NMQMMain;
import austeretony.nmqm.common.origin.CommonReference;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.network.NetServerHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraftforge.common.MinecraftForge;

public class NMQMHooks {
	
	public static void getContainerClassName(Container container, EntityPlayer player) {		
		if (NMQMDataLoader.isConfigModeEnabled() && CommonReference.isOpped(player) && !player.worldObj.isRemote) {			
			String containerClassName = container.getClass().getName();						
			if (!NMQMDataLoader.latestContainer.equals(containerClassName)) {						
				NMQMDataLoader.latestContainer = containerClassName;
				NMQMMain.showMessage(player, EnumNMQMChatMessages.LATEST_CONTAINER, containerClassName);
			}
		}
	}
	
	public static int verifyQuickMoveClient(int clickType) {		
		if (clickType == 1) {						
			String containerClassname = Minecraft.getMinecraft().thePlayer.openContainer.getClass().getName();			
			if (NMQMDataLoader.getMode() == 0) {				
				if (NMQMDataLoader.CONTAINERS_CLIENT.contains(containerClassname))
					clickType = 0;
			} else if (NMQMDataLoader.getMode() == 1) {				
				if (!NMQMDataLoader.CONTAINERS_CLIENT.contains(containerClassname))
					clickType = 0;
			}
		}		
		return clickType;
	}
	
	public static int verifyQuickMoveServer(NetHandler handler, int clickType) {				
		if (clickType == 1) {			
			EntityPlayer player = ((NetServerHandler) handler).playerEntity;			
			String containerClassname = player.openContainer.getClass().getName();			
			if (NMQMDataLoader.getMode() == 0) {				
				if (NMQMDataLoader.CONTAINERS_SERVER.contains(containerClassname))
					clickType = 0;
			} else if (NMQMDataLoader.getMode() == 1) {				
				if (!NMQMDataLoader.CONTAINERS_SERVER.contains(containerClassname))
					clickType = 0;
			}
		}		
		return clickType;
	}
	
	public static void onPlayerLoggedIn(EntityPlayerMP playerMP) {		
		MinecraftForge.EVENT_BUS.post(new PlayerLoggedInEvent(playerMP));
	}
}
