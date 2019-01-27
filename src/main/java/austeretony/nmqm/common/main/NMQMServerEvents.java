package austeretony.nmqm.common.main;

import austeretony.nmqm.common.network.NetworkHandler;
import austeretony.nmqm.common.network.client.CPSyncContainers;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraft.entity.player.EntityPlayerMP;

public class NMQMServerEvents {

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        if (DataLoader.isClientSyncEnabled())
            NetworkHandler.sendToPlayer(new CPSyncContainers(DataLoader.isSettingsDisabled() ? DataLoader.EMPTY_SET : DataLoader.CONTAINERS_SERVER), (EntityPlayerMP) event.player);
    }
}
