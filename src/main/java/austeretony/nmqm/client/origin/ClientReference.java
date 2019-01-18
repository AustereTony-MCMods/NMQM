package austeretony.nmqm.client.origin;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;

public class ClientReference {
	
	@SideOnly(Side.CLIENT)
	public static Minecraft getMinecraft() {
		return Minecraft.getMinecraft();
	}
	
	@SideOnly(Side.CLIENT)
	public static EntityPlayer getClientPlayer() {
		return getMinecraft().thePlayer;
	}
	
	@SideOnly(Side.CLIENT)
	public static void sendChatMessage(IChatComponent chatComponent) {
		getClientPlayer().addChatMessage(chatComponent);
	}
}
