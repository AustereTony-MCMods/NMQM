package austeretony.nmqm.common.network;

import austeretony.nmqm.common.main.NMQMMain;
import austeretony.nmqm.common.network.client.CPClearContainers;
import austeretony.nmqm.common.network.client.CPSyncContainer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {

    private static byte packetId = 0;
	 
	private static final SimpleNetworkWrapper DISPATCHER = NetworkRegistry.INSTANCE.newSimpleChannel(NMQMMain.MODID);
	
	public static final void registerPackets() {
		registerMessage(CPSyncContainer.class);
		registerMessage(CPClearContainers.class);
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
