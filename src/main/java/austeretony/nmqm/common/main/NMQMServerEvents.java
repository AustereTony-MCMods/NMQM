package austeretony.nmqm.common.main;

import austeretony.nmqm.common.config.ConfigLoader;
import austeretony.nmqm.common.network.NetworkHandler;
import austeretony.nmqm.common.network.client.CPSyncData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class NMQMServerEvents {

    @SubscribeEvent
    public void onPlayerConnectsToServer(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        ConfigLoader.initClient();
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        if (ConfigLoader.isSettingsEnabled()) {
            NetworkHandler.sendTo(new CPSyncData(CPSyncData.EnumAction.SYNC_STATUS), (EntityPlayerMP) event.player);
            NetworkHandler.sendTo(new CPSyncData(CPSyncData.EnumAction.SYNC_MODE), (EntityPlayerMP) event.player);
            NetworkHandler.sendTo(new CPSyncData(CPSyncData.EnumAction.SYNC_LIST), (EntityPlayerMP) event.player);
        }
    }
}
