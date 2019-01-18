package austeretony.nmqm.client.origin;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
