package austeretony.nmqm.common.network.client;

import java.io.IOException;
import java.util.Set;

import austeretony.nmqm.common.main.NMQMDataLoader;
import austeretony.nmqm.common.network.AbstractMessage.AbstractClientMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;

public class CPSyncContainers extends AbstractClientMessage<CPSyncContainers> {
	
	private byte amount;
	
	private byte[] lengths;
	
	private String[] containers;
	
	public CPSyncContainers() {}
	
	public CPSyncContainers(Set<String> containers) {
		this.amount = (byte) containers.size();
		if (this.amount != 0) {
			this.lengths = new byte[this.amount];
			this.containers = new String[this.amount];
			int index = 0;
			for (String s : containers) {
				this.containers[index] = s;
				this.lengths[index] = (byte) s.length();
				index++;
			}
		}
	}
	
	@Override
	protected void writeData(PacketBuffer buffer) throws IOException {
		buffer.writeByte(this.amount);
		for (int i = 0; i < this.amount; i++) {
			buffer.writeByte(this.lengths[i]);
			buffer.writeString(this.containers[i]);
		}
	}

	@Override
	protected void readData(PacketBuffer buffer) throws IOException {
		NMQMDataLoader.CONTAINERS_CLIENT.clear();
		this.amount = buffer.readByte();
		for (int i = 0; i < this.amount; i++)
			NMQMDataLoader.CONTAINERS_CLIENT.add(buffer.readStringFromBuffer(buffer.readByte()));	
	}

	@Override
	public void performProcess(EntityPlayer player, Side side) {}
}
