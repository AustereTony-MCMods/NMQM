package austeretony.nmqm.common.main;

import austeretony.nmqm.common.network.NetworkHandler;
import austeretony.nmqm.common.network.client.CPSyncContainers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class NMQMServerEvents {

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
		if (NMQMDataLoader.isClientSyncEnabled())
			NetworkHandler.sendToPlayer(new CPSyncContainers(NMQMDataLoader.CONTAINERS_SERVER), (EntityPlayerMP) event.player);
	}
}
