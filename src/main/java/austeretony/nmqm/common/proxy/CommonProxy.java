package austeretony.nmqm.common.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CommonProxy {
	
	public EntityPlayer getPlayerEntity(MessageContext ctx) {		
		return ctx.getServerHandler().playerEntity;
	}
}
