package austeretony.nmqm.common.network;

import com.google.common.collect.HashBiMap;

import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public abstract class ProxyPacket {

    public static ProxyPacket create(HashBiMap<Integer, Class<? extends ProxyPacket>> packets, int id) {
        try {
            Class<? extends ProxyPacket> packetClass = (Class) packets.get(id);
            return packetClass == null ? null : (ProxyPacket) packetClass.newInstance();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public abstract void write(PacketBuffer buffer);

    public abstract void read(PacketBuffer buffer);

    public abstract void process(INetHandler netHandler);
}
