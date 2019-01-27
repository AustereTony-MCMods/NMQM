package austeretony.nmqm.common.network.client;

import java.io.IOException;

import austeretony.nmqm.common.main.EnumChatMessages;
import austeretony.nmqm.common.network.AbstractMessage.AbstractClientMessage;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

public class CPSendMessage extends AbstractClientMessage<CPSendMessage> {

    private byte msgId, amount;

    private byte[] lengths;

    private String[] args;

    public CPSendMessage() {}

    public CPSendMessage(EnumChatMessages msg, String... args) {
        this.msgId = (byte) msg.id;
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
            buffer.writeStringToBuffer(this.args[i]);
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
        EnumChatMessages.values()[this.msgId].sendMesssageClient(this.args);
    }
}
