package austeretony.nmqm.common.network.client;

import austeretony.nmqm.common.config.ConfigLoader;
import austeretony.nmqm.common.config.EnumConfigSettings;
import austeretony.nmqm.common.network.ProxyPacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPSyncData extends ProxyPacket {

    private EnumAction enumAction;

    public CPSyncData() {}

    public CPSyncData(EnumAction enumAction) {
        this.enumAction = enumAction;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeByte((byte) enumAction.ordinal());
        switch (this.enumAction) {
        case SYNC_STATUS:
            buffer.writeBoolean(ConfigLoader.isSettingsEnabled());
            break;
        case SYNC_MODE:
            buffer.writeByte(EnumConfigSettings.MODE.getIntValue());  
            break;
        case SYNC_LIST:
            buffer.writeByte(ConfigLoader.containersServer.size());
            for (String s : ConfigLoader.containersServer) {
                buffer.writeByte(s.length());
                buffer.writeString(s);
            }  
            break;
        case SYNC_LATEST:
        case REMOVE_LATEST:
            buffer.writeByte(ConfigLoader.latestContainer.length());
            buffer.writeString(ConfigLoader.latestContainer);
            break;
        default:
            break;
        }
    }

    @Override
    public void read(PacketBuffer buffer) {
        this.enumAction = EnumAction.values()[buffer.readByte()];
        switch (this.enumAction) {
        case SYNC_STATUS:
            ConfigLoader.setSettingsEnabledClient(buffer.readBoolean());            
            break;
        case SYNC_MODE:
            ConfigLoader.setModeClient(buffer.readByte());
            break;
        case SYNC_LIST:
            ConfigLoader.containersClient.clear();
            int amount = buffer.readByte();
            for (int i = 0; i < amount; i++)
                ConfigLoader.containersClient.add(buffer.readString(buffer.readByte()));
            break;
        case SYNC_LATEST:
            ConfigLoader.containersClient.add(buffer.readString(buffer.readByte()));   
            break;
        case CLEAR_LIST:
            ConfigLoader.containersClient.clear();
            break;
        case REMOVE_LATEST:
            ConfigLoader.containersClient.remove(buffer.readString(buffer.readByte()));   
            break;
        }
    }

    @Override
    public void process(INetHandler netHandler) {}

    public enum EnumAction {

        SYNC_STATUS,
        SYNC_MODE,
        SYNC_LIST,
        SYNC_LATEST,
        CLEAR_LIST,
        REMOVE_LATEST;
    }
}
