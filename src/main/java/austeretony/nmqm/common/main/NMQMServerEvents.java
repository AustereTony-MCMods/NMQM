package austeretony.nmqm.common.main;

import austeretony.nmqm.common.events.PlayerLoggedInEvent;
import austeretony.nmqm.common.network.NetworkHandler;
import net.minecraftforge.event.ForgeSubscribe;

public class NMQMServerEvents {

	@ForgeSubscribe
	public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
		if (NMQMDataLoader.isClientSyncEnabled())
			NetworkHandler.syncContainers(event.player);
	}
}
