package austeretony.nmqm.common.network.client;

import java.io.IOException;

import austeretony.nmqm.common.main.NMQMDataLoader;
import austeretony.nmqm.common.network.AbstractMessage.AbstractClientMessage;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

public class CPClearContainers extends AbstractClientMessage<CPClearContainers> {
		
	public CPClearContainers() {}
	
	@Override
	protected void writeData(PacketBuffer buffer) throws IOException {}

	@Override
	protected void readData(PacketBuffer buffer) throws IOException {}

	@Override
	public void performProcess(EntityPlayer player, Side side) {
		NMQMDataLoader.CONTAINERS_CLIENT.clear();
	}
}
