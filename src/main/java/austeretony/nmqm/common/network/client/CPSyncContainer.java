package austeretony.nmqm.common.network.client;

import java.io.IOException;

import austeretony.nmqm.common.main.NMQMDataLoader;
import austeretony.nmqm.common.network.AbstractMessage.AbstractClientMessage;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

public class CPSyncContainer extends AbstractClientMessage<CPSyncContainer> {

	private byte nameLength;
	
	private String containerName;
		
	public CPSyncContainer() {}
	
	public CPSyncContainer(String containerName) {
		this.nameLength = (byte) containerName.length();
		this.containerName = containerName;
	}
	
	@Override
	protected void writeData(PacketBuffer buffer) throws IOException {
		buffer.writeByte(this.nameLength);
		buffer.writeStringToBuffer(this.containerName);
	}

	@Override
	protected void readData(PacketBuffer buffer) throws IOException {
		this.nameLength = buffer.readByte();
		this.containerName = buffer.readStringFromBuffer(this.nameLength);
	}

	@Override
	public void performProcess(EntityPlayer player, Side side) {
		NMQMDataLoader.CONTAINERS_CLIENT.add(this.containerName);
	}
}
