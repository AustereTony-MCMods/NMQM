package austeretony.nmqm.common.main;

import austeretony.nmqm.client.origin.ClientReference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public enum EnumNMQMChatMessages {

	UPDATE_MESSAGE_HEAD,
	UPDATE_MESSAGE_LINK,
	LATEST_CONTAINER,
	CONFIGURATION_ENABLED,
	CONFIGURATION_DISABLED,
	CONTAINERS_LIST,
	EMPTY,
	CONTAINER,
	NEED_ENABLE_CONFIGURATION,
	NO_LATEST_CONTAINER,
	ADDED_TO_LIST,
	REMOVED_FROM_LIST,
	CONTAINER_ABSENT,
	CONTAINERS_LIST_CLEARED,
	CHANGES_SAVED;
	
	private static int index;
	
	private final int id;
			
	EnumNMQMChatMessages() {
		this.id = createId();
	}
	
	private int createId() {
		return index++;
	}
	
	public int getId() {
		return this.id;
	}
	
	@SideOnly(Side.CLIENT)
	public IChatComponent getLocalizedMessage(Object ... args) {
		IChatComponent 
		message = null,
		modPrefix = new ChatComponentText("[NMQM] "),
		containerName, shortName, msg1, msg2, msg3;
		modPrefix.getChatStyle().setColor(EnumChatFormatting.AQUA);
		switch (this) {
			case UPDATE_MESSAGE_HEAD:
				msg1 = new ChatComponentTranslation("nmqm.update.newVersion");
				msg2 = new ChatComponentText(" [" + NMQMMain.VERSION + "/" + args[0] + "]");        
				message = modPrefix.appendSibling(msg1).appendSibling(msg2);
				break;
			case UPDATE_MESSAGE_LINK:
				msg1 = new ChatComponentTranslation("nmqm.update.projectPage");
				msg2 = new ChatComponentText(": ");
				msg3 = new ChatComponentText(NMQMMain.PROJECT_LOCATION);	
				msg1.getChatStyle().setColor(EnumChatFormatting.AQUA);
				msg3.getChatStyle().setColor(EnumChatFormatting.WHITE);		        	
				msg3.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, NMQMMain.PROJECT_URL));		
				message = msg1.appendSibling(msg2).appendSibling(msg3);	
				break;
			case LATEST_CONTAINER:
				msg1 = new ChatComponentTranslation("nmqm.message.latestContainer");
				msg2 = new ChatComponentText(": ");
				containerName = new ChatComponentText((String) args[0]);				
				containerName.getChatStyle().setColor(EnumChatFormatting.WHITE);  
				message = modPrefix.appendSibling(msg1).appendSibling(msg2).appendSibling(containerName);
				break;
			case CONFIGURATION_ENABLED:
				msg1 = new ChatComponentTranslation("nmqm.command.enable");
				message = modPrefix.appendSibling(msg1);
				break;		
			case CONFIGURATION_DISABLED:
				msg1 = new ChatComponentTranslation("nmqm.command.disable");
				message = modPrefix.appendSibling(msg1);
				break;		
			case CONTAINERS_LIST:
				msg1 = new ChatComponentTranslation("nmqm.command.list");
				message = modPrefix.appendSibling(msg1);
				break;	
			case EMPTY:
				message = new ChatComponentTranslation("nmqm.command.list.empty");
				break;	
			case CONTAINER:
				message = new ChatComponentText((String) args[0]);
				break;	
			case NEED_ENABLE_CONFIGURATION:
				msg1 = new ChatComponentTranslation("nmqm.command.err.debugMode");			
				msg1.getChatStyle().setColor(EnumChatFormatting.RED);			
				message = modPrefix.appendSibling(msg1);	
				break;
			case NO_LATEST_CONTAINER:
				msg1 = new ChatComponentTranslation("nmqm.command.latest.notExist");			
				msg1.getChatStyle().setColor(EnumChatFormatting.RED);			
				message = modPrefix.appendSibling(msg1);	
				break;
			case ADDED_TO_LIST:
				msg1 = new ChatComponentTranslation("nmqm.command.add");
				msg2 = new ChatComponentText(": ");
				shortName = new ChatComponentText((String) args[0]);
				shortName.getChatStyle().setColor(EnumChatFormatting.WHITE);  
				message = modPrefix.appendSibling(msg1).appendSibling(msg2).appendSibling(shortName);
				break;
			case REMOVED_FROM_LIST:
				msg1 = new ChatComponentTranslation("nmqm.command.remove");
				msg2 = new ChatComponentText(": ");
				shortName = new ChatComponentText((String) args[0]);
				shortName.getChatStyle().setColor(EnumChatFormatting.WHITE);  
				message = modPrefix.appendSibling(msg1).appendSibling(msg2).appendSibling(shortName);
				break;
			case CONTAINER_ABSENT:
				msg1 = new ChatComponentTranslation("nmqm.command.remove.absent");			
				msg1.getChatStyle().setColor(EnumChatFormatting.RED);			
				message = modPrefix.appendSibling(msg1);
				break;
			case CONTAINERS_LIST_CLEARED:
				msg1 = new ChatComponentTranslation("nmqm.command.clear");
				message = modPrefix.appendSibling(msg1);
				break;
			case CHANGES_SAVED:
				msg1 = new ChatComponentTranslation("nmqm.command.save");
				message = modPrefix.appendSibling(msg1);
				break;
			default:
				break;
		}
		return message;
	}
	
	@SideOnly(Side.CLIENT)
	public static void showMessage(int msgId, Object ... args) {
		for (EnumNMQMChatMessages msg : values()) {
			if (msg.getId() == msgId)
				ClientReference.sendChatMessage(msg.getLocalizedMessage(args));
		}
	}
}
