package austeretony.nmqm.common.main;

import austeretony.nmqm.common.network.NetworkHandler;
import austeretony.nmqm.common.network.client.CPSyncContainer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class NMQMServerEvents {

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
		if (NMQMDataLoader.isClientSyncEnabled()) {
			for (String s : NMQMDataLoader.CONTAINERS_SERVER)
				NetworkHandler.sendToPlayer(new CPSyncContainer(CPSyncContainer.EnumOperation.ADD, s), (EntityPlayerMP) event.player);
	 	}
	}
}
