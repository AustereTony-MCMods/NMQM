package ru.austeretony.nmqm.coremod;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import ru.austeretony.nmqm.main.ConfigLoader;

public class NMQMHooks {
	
	public static void getContainerClassName(Container container, EntityPlayer player) {
		
		if (!player.worldObj.isRemote) {
			
			String containerClassName = container.getClass().getName();
						
			if (!ConfigLoader.latestContainer.equals(containerClassName)) {
				
				ConfigLoader.latestContainer = containerClassName;
				
				IChatComponent 
				message1 = new ChatComponentText("[NMQM] "),
				message2 = new ChatComponentTranslation("nmqm.message.latestContainer"),
				message3 = new ChatComponentText(": "),
				containerName = new ChatComponentText(containerClassName);
				
				message1.getChatStyle().setColor(EnumChatFormatting.AQUA);
				containerName.getChatStyle().setColor(EnumChatFormatting.WHITE);
            	
            	player.addChatMessage(message1.appendSibling(message2).appendSibling(message3).appendSibling(containerName));
			}
		}
	}
	
	public static int verifyQuickMoveClient(int clickType) {
		
		if (clickType == 1) {
						
			String containerClassname = Minecraft.getMinecraft().thePlayer.openContainer.getClass().getName();
			
			if (ConfigLoader.getMode() == 0) {
				
				if (ConfigLoader.CONTAINERS.contains(containerClassname))
					clickType = 0;
			}
			
			else if (ConfigLoader.getMode() == 1) {
				
				if (!ConfigLoader.CONTAINERS.contains(containerClassname))
					clickType = 0;
			}
		}
		
		return clickType;
	}
	
	public static int verifyQuickMoveServer(INetHandlerPlayServer handler, int clickType) {
				
		if (clickType == 1) {
			
			EntityPlayer player = ((NetHandlerPlayServer) handler).playerEntity;
			
			String containerClassname = player.openContainer.getClass().getName();
			
			if (ConfigLoader.getMode() == 0) {
				
				if (ConfigLoader.CONTAINERS.contains(containerClassname))
					clickType = 0;
			}
			
			else if (ConfigLoader.getMode() == 1) {
				
				if (!ConfigLoader.CONTAINERS.contains(containerClassname))
					clickType = 0;
			}
		}
		
		return clickType;
	}
}
