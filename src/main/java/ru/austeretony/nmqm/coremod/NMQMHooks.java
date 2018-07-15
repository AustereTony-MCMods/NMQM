package ru.austeretony.nmqm.coremod;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.NetServerHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import ru.austeretony.nmqm.main.ConfigLoader;

public class NMQMHooks {
	
	public static void getContainerClassName(Container container, EntityPlayer player) {
		
		if (!player.worldObj.isRemote) {
			
			String containerClassName = container.getClass().getName();
						
			if (!ConfigLoader.latestContainer.equals(containerClassName)) {
				
				ConfigLoader.latestContainer = containerClassName;
				
				ChatMessageComponent 
				message1 = new ChatMessageComponent().addText("[NMQM] "),
				message2 = new ChatMessageComponent().addKey("nmqm.message.latestContainer"),
				message3 = new ChatMessageComponent().addText(": "),
				containerName = new ChatMessageComponent().addText(containerClassName);
            	
            	player.addChatMessage(message1.appendComponent(message2).appendComponent(message3).appendComponent(containerName).toString());
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
	
	public static int verifyQuickMoveServer(NetHandler handler, int clickType) {
				
		if (clickType == 1) {
			
			EntityPlayer player = ((NetServerHandler) handler).playerEntity;
			
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
