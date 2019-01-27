package austeretony.nmqm.common.network;

import austeretony.nmqm.common.main.NMQMMain;
import austeretony.nmqm.common.network.client.CPSendMessage;
import austeretony.nmqm.common.network.client.CPSyncContainers;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayerMP;

public class NetworkHandler {

    private static byte packetId = 0;

    private static final SimpleNetworkWrapper DISPATCHER = NetworkRegistry.INSTANCE.newSimpleChannel(NMQMMain.MODID);

    public static final void registerPackets() {
        registerMessage(CPSyncContainers.class);
        registerMessage(CPSendMessage.class);
    }

    private static final <T extends AbstractMessage<T> & IMessageHandler<T, IMessage>> void registerMessage(Class<T> clazz) {
        if (AbstractMessage.AbstractClientMessage.class.isAssignableFrom(clazz)) {			
            NetworkHandler.DISPATCHER.registerMessage(clazz, clazz, packetId++, Side.CLIENT);
        } else if (AbstractMessage.AbstractServerMessage.class.isAssignableFrom(clazz)) {			
            NetworkHandler.DISPATCHER.registerMessage(clazz, clazz, packetId++, Side.SERVER);
        } else {
            NetworkHandler.DISPATCHER.registerMessage(clazz, clazz, packetId, Side.CLIENT);
            NetworkHandler.DISPATCHER.registerMessage(clazz, clazz, packetId++, Side.SERVER);
        }
    }

    public static final void sendToPlayer(IMessage message, EntityPlayerMP player) {  	
        NetworkHandler.DISPATCHER.sendTo(message, player);
    }
}
