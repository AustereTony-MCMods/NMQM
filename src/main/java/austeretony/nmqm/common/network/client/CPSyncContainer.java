package austeretony.nmqm.common.network.client;

import java.io.IOException;

import austeretony.nmqm.common.main.NMQMDataLoader;
import austeretony.nmqm.common.network.AbstractMessage.AbstractClientMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;

public class CPSyncContainer extends AbstractClientMessage<CPSyncContainer> {

	private byte operation, nameLength;
	
	private String containerName;
		
	public CPSyncContainer() {}
	
	public CPSyncContainer(EnumOperation operation, String containerName) {
		this.operation = (byte) (operation == EnumOperation.ADD ? 0 : 1);
		this.nameLength = (byte) containerName.length();
		this.containerName = containerName;
	}
	
	@Override
	protected void writeData(PacketBuffer buffer) throws IOException {
		buffer.writeByte(this.operation);
		buffer.writeByte(this.nameLength);
		buffer.writeString(this.containerName);
	}

	@Override
	protected void readData(PacketBuffer buffer) throws IOException {
		this.operation = buffer.readByte();
		this.nameLength = buffer.readByte();
		this.containerName = buffer.readString(this.nameLength);
	}

	@Override
	public void performProcess(EntityPlayer player, Side side) {
		if (this.operation == 0)
			NMQMDataLoader.CONTAINERS_CLIENT.add(this.containerName);
		else 
			NMQMDataLoader.CONTAINERS_CLIENT.remove(this.containerName);
	}
	
	public enum EnumOperation {
		
		ADD,
		REMOVE
	}
}
