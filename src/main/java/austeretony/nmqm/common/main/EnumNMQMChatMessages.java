package austeretony.nmqm.common.main;

import austeretony.nmqm.client.origin.ClientReference;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	public ITextComponent getLocalizedMessage(Object ... args) {
		ITextComponent 
		message = null,
		modPrefix = new TextComponentString("[NMQM] "),
		containerName, shortName, msg1, msg2, msg3;
		modPrefix.getStyle().setColor(TextFormatting.AQUA);
		switch (this) {
			case UPDATE_MESSAGE_HEAD:
				msg1 = new TextComponentTranslation("nmqm.update.newVersion");
				msg2 = new TextComponentString(" [" + NMQMMain.VERSION + "/" + args[0] + "]");        
	        	message = modPrefix.appendSibling(msg1).appendSibling(msg2);
				break;
			case UPDATE_MESSAGE_LINK:
				msg1 = new TextComponentTranslation("nmqm.update.projectPage");
				msg2 = new TextComponentString(": ");
				msg3 = new TextComponentString(NMQMMain.PROJECT_LOCATION);	
				msg1.getStyle().setColor(TextFormatting.AQUA);
				msg3.getStyle().setColor(TextFormatting.WHITE);		        	
				msg3.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, NMQMMain.PROJECT_URL));		
				message = msg1.appendSibling(msg2).appendSibling(msg3);	
				break;
			case LATEST_CONTAINER:
				msg1 = new TextComponentTranslation("nmqm.message.latestContainer");
				msg2 = new TextComponentString(": ");
				containerName = new TextComponentString((String) args[0]);				
				containerName.getStyle().setColor(TextFormatting.WHITE);  
				message = modPrefix.appendSibling(msg1).appendSibling(msg2).appendSibling(containerName);
				break;
			case CONFIGURATION_ENABLED:
				msg1 = new TextComponentTranslation("nmqm.command.enable");
				message = modPrefix.appendSibling(msg1);
				break;		
			case CONFIGURATION_DISABLED:
				msg1 = new TextComponentTranslation("nmqm.command.disable");
				message = modPrefix.appendSibling(msg1);
				break;		
			case CONTAINERS_LIST:
				msg1 = new TextComponentTranslation("nmqm.command.list");
				message = modPrefix.appendSibling(msg1);
				break;	
			case EMPTY:
				message = new TextComponentTranslation("nmqm.command.list.empty");
				break;	
			case CONTAINER:
				message = new TextComponentString((String) args[0]);
				break;	
			case NEED_ENABLE_CONFIGURATION:
				msg1 = new TextComponentTranslation("nmqm.command.err.debugMode");			
				msg1.getStyle().setColor(TextFormatting.RED);			
				message = modPrefix.appendSibling(msg1);	
				break;
			case NO_LATEST_CONTAINER:
				msg1 = new TextComponentTranslation("nmqm.command.latest.notExist");			
				msg1.getStyle().setColor(TextFormatting.RED);			
				message = modPrefix.appendSibling(msg1);	
				break;
			case ADDED_TO_LIST:
				msg1 = new TextComponentTranslation("nmqm.command.add");
				msg2 = new TextComponentString(": ");
				shortName = new TextComponentString((String) args[0]);
				shortName.getStyle().setColor(TextFormatting.WHITE);  
				message = modPrefix.appendSibling(msg1).appendSibling(msg2).appendSibling(shortName);
				break;
			case REMOVED_FROM_LIST:
				msg1 = new TextComponentTranslation("nmqm.command.remove");
				msg2 = new TextComponentString(": ");
				shortName = new TextComponentString((String) args[0]);
				shortName.getStyle().setColor(TextFormatting.WHITE);  
				message = modPrefix.appendSibling(msg1).appendSibling(msg2).appendSibling(shortName);
				break;
			case CONTAINER_ABSENT:
				msg1 = new TextComponentTranslation("nmqm.command.remove.absent");			
				msg1.getStyle().setColor(TextFormatting.RED);			
				message = modPrefix.appendSibling(msg1);
				break;
			case CONTAINERS_LIST_CLEARED:
				msg1 = new TextComponentTranslation("nmqm.command.clear");
				message = modPrefix.appendSibling(msg1);
				break;
			case CHANGES_SAVED:
				msg1 = new TextComponentTranslation("nmqm.command.save");
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
