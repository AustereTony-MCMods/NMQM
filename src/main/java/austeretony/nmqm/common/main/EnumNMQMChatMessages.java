package austeretony.nmqm.common.main;

import austeretony.nmqm.client.origin.ClientReference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;

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
	public ChatMessageComponent getLocalizedMessage(Object ... args) {
		ChatMessageComponent
		message = null,
		modPrefix = new ChatMessageComponent().addText("[NMQM] "),
		containerName, shortName, msg1, msg2, msg3;
		modPrefix.setColor(EnumChatFormatting.AQUA);
		switch (this) {
			case UPDATE_MESSAGE_HEAD:
				msg1 = new ChatMessageComponent().addKey("nmqm.update.newVersion");
				msg2 = new ChatMessageComponent().addText(" [" + NMQMMain.VERSION + "/" + args[0] + "]");        
				message = modPrefix.appendComponent(msg1).appendComponent(msg2);
				break;
			case UPDATE_MESSAGE_LINK:
				msg1 = new ChatMessageComponent().addKey("nmqm.update.projectPage");
				msg2 = new ChatMessageComponent().addText(": ");
				msg3 = new ChatMessageComponent().addText(NMQMMain.PROJECT_URL);	
				msg1.setColor(EnumChatFormatting.AQUA);
				msg3.setColor(EnumChatFormatting.WHITE);		        	
				message = msg1.appendComponent(msg2).appendComponent(msg3);	
				break;
			case LATEST_CONTAINER:
				msg1 = new ChatMessageComponent().addKey("nmqm.message.latestContainer");
				msg2 = new ChatMessageComponent().addText(": ");
				containerName = new ChatMessageComponent().addText((String) args[0]);				
				containerName.setColor(EnumChatFormatting.WHITE);  
				message = modPrefix.appendComponent(msg1).appendComponent(msg2).appendComponent(containerName);
				break;
			case CONFIGURATION_ENABLED:
				msg1 = new ChatMessageComponent().addKey("nmqm.command.enable");
				message = modPrefix.appendComponent(msg1);
				break;		
			case CONFIGURATION_DISABLED:
				msg1 = new ChatMessageComponent().addKey("nmqm.command.disable");
				message = modPrefix.appendComponent(msg1);
				break;		
			case CONTAINERS_LIST:
				msg1 = new ChatMessageComponent().addKey("nmqm.command.list");
				message = modPrefix.appendComponent(msg1);
				break;	
			case EMPTY:
				message = new ChatMessageComponent().addKey("nmqm.command.list.empty");
				break;	
			case CONTAINER:
				message = new ChatMessageComponent().addText((String) args[0]);
				break;	
			case NEED_ENABLE_CONFIGURATION:
				msg1 = new ChatMessageComponent().addKey("nmqm.command.err.debugMode");			
				msg1.setColor(EnumChatFormatting.RED);			
				message = modPrefix.appendComponent(msg1);	
				break;
			case NO_LATEST_CONTAINER:
				msg1 = new ChatMessageComponent().addKey("nmqm.command.latest.notExist");			
				msg1.setColor(EnumChatFormatting.RED);			
				message = modPrefix.appendComponent(msg1);	
				break;
			case ADDED_TO_LIST:
				msg1 = new ChatMessageComponent().addKey("nmqm.command.add");
				msg2 = new ChatMessageComponent().addText(": ");
				shortName = new ChatMessageComponent().addText((String) args[0]);
				shortName.setColor(EnumChatFormatting.WHITE);  
				message = modPrefix.appendComponent(msg1).appendComponent(msg2).appendComponent(shortName);
				break;
			case REMOVED_FROM_LIST:
				msg1 = new ChatMessageComponent().addKey("nmqm.command.remove");
				msg2 = new ChatMessageComponent().addText(": ");
				shortName = new ChatMessageComponent().addText((String) args[0]);
				shortName.setColor(EnumChatFormatting.WHITE);  
				message = modPrefix.appendComponent(msg1).appendComponent(msg2).appendComponent(shortName);
				break;
			case CONTAINER_ABSENT:
				msg1 = new ChatMessageComponent().addKey("nmqm.command.remove.absent");			
				msg1.setColor(EnumChatFormatting.RED);			
				message = modPrefix.appendComponent(msg1);
				break;
			case CONTAINERS_LIST_CLEARED:
				msg1 = new ChatMessageComponent().addKey("nmqm.command.clear");
				message = modPrefix.appendComponent(msg1);
				break;
			case CHANGES_SAVED:
				msg1 = new ChatMessageComponent().addKey("nmqm.command.save");
				message = modPrefix.appendComponent(msg1);
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
