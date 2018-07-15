package ru.austeretony.nmqm.coremod;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import ru.austeretony.nmqm.main.ConfigLoader;

public class NMQMHooks {
	
	public static void getContainerClassName(Container container, EntityPlayer player) {
		
		if (!player.worldObj.isRemote) {
			
			String containerClassName = container.getClass().getName();
						
			if (!ConfigLoader.latestContainer.equals(containerClassName)) {
				
				ConfigLoader.latestContainer = containerClassName;
				
				ITextComponent 
				message1 = new TextComponentString("[NMQM] "),
				message2 = new TextComponentTranslation("nmqm.message.latestContainer"),
				message3 = new TextComponentString(": "),
				containerName = new TextComponentString(containerClassName);
				
				message1.getStyle().setColor(TextFormatting.AQUA);
				containerName.getStyle().setColor(TextFormatting.WHITE);
            	
            	player.addChatMessage(message1.appendSibling(message2).appendSibling(message3).appendSibling(containerName));
			}
		}
	}
	
	public static ClickType verifyQuickMoveClient(ClickType clickType) {
		
		if (clickType == ClickType.QUICK_MOVE) {
						
			String containerClassname = Minecraft.getMinecraft().thePlayer.openContainer.getClass().getName();
			
			if (ConfigLoader.getMode() == 0) {
				
				if (ConfigLoader.CONTAINERS.contains(containerClassname))
					clickType = ClickType.PICKUP;
			}
			
			else if (ConfigLoader.getMode() == 1) {
				
				if (!ConfigLoader.CONTAINERS.contains(containerClassname))
					clickType = ClickType.PICKUP;
			}
		}
		
		return clickType;
	}
	
	public static ClickType verifyQuickMoveServer(INetHandlerPlayServer handler, ClickType clickType) {
				
		if (clickType == ClickType.QUICK_MOVE) {
			
			EntityPlayer player = ((NetHandlerPlayServer) handler).playerEntity;
			
			String containerClassname = player.openContainer.getClass().getName();
			
			if (ConfigLoader.getMode() == 0) {
				
				if (ConfigLoader.CONTAINERS.contains(containerClassname))
					clickType = ClickType.PICKUP;
			}
			
			else if (ConfigLoader.getMode() == 1) {
				
				if (!ConfigLoader.CONTAINERS.contains(containerClassname))
					clickType = ClickType.PICKUP;
			}
		}
		
		return clickType;
	}
}
