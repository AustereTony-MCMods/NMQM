package austeretony.nmqm.common.main;

import austeretony.nmqm.common.network.NetworkHandler;
import austeretony.nmqm.common.network.client.CPClearContainers;
import austeretony.nmqm.common.network.client.CPSyncContainer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraft.entity.player.EntityPlayerMP;

public class NMQMServerEvents {

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
		if (NMQMDataLoader.isClientSyncEnabled()) {
			NetworkHandler.sendToPlayer(new CPClearContainers(), (EntityPlayerMP) event.player);
			for (String s : NMQMDataLoader.CONTAINERS_SERVER)
				NetworkHandler.sendToPlayer(new CPSyncContainer(s), (EntityPlayerMP) event.player);
	 	}
	}
}
