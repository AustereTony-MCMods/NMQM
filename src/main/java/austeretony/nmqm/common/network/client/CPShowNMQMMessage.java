package austeretony.nmqm.common.network.client;

import java.io.IOException;

import austeretony.nmqm.common.main.EnumNMQMChatMessages;
import austeretony.nmqm.common.network.AbstractMessage.AbstractClientMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;

public class CPShowNMQMMessage extends AbstractClientMessage<CPShowNMQMMessage> {

	private byte msgId, amount;
	
	private byte[] lengths;
	
	private String[] args;
	
	public CPShowNMQMMessage() {}
	
	public CPShowNMQMMessage(EnumNMQMChatMessages msg, String ... args) {
		this.msgId = (byte) msg.getId();
		this.amount = (byte) args.length;
		if (this.amount != 0) {
			this.lengths = new byte[this.amount];
			this.args = new String[this.amount];
			for (int i = 0; i < this.amount; i++) {
				this.args[i] = args[i];
				this.lengths[i] = (byte) args[i].length();
				i++;
			}
		}
	}

	@Override
	protected void writeData(PacketBuffer buffer) throws IOException {
		buffer.writeByte(this.msgId);
		buffer.writeByte(this.amount);
		for (int i = 0; i < this.amount; i++) {
			buffer.writeByte(this.lengths[i]);
			buffer.writeString(this.args[i]);
		}
	}

	@Override
	protected void readData(PacketBuffer buffer) throws IOException {
		this.msgId = buffer.readByte();
		this.amount = buffer.readByte();
		if (this.amount != 0) {
			this.args = new String[this.amount];
			for (int i = 0; i < this.amount; i++)
				this.args[i] = buffer.readStringFromBuffer(buffer.readByte());	
		}
	}

	@Override
	public void performProcess(EntityPlayer player, Side side) {
		EnumNMQMChatMessages.showMessage(this.msgId, (Object[]) this.args);
	}
}
