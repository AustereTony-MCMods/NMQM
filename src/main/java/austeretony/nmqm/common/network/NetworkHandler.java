package austeretony.nmqm.common.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

import austeretony.nmqm.common.main.EnumNMQMChatMessages;
import austeretony.nmqm.common.main.NMQMDataLoader;
import austeretony.nmqm.common.main.NMQMMain;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

public class NetworkHandler implements IPacketHandler {

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
    	InputStream is = new ByteArrayInputStream((byte[]) packet.data);
    	DataInputStream inputStream = new DataInputStream(is);   
        if (packet.channel.equals(NMQMMain.MODID + "_list")) {       
        	int amount;
        	try {
        		NMQMDataLoader.CONTAINERS_CLIENT.clear();
        		amount = inputStream.readByte();
        		for (int i = 0; i < amount; i++)
        			NMQMDataLoader.CONTAINERS_CLIENT.add(packet.readString(inputStream, inputStream.readByte()));	
        	} catch (IOException e) {        		
        		e.printStackTrace();
        	}     	
        } else if (packet.channel.equals(NMQMMain.MODID + "_msg")) {      
        	int msgId, amount;
        	String[] params = null;
        	try {
        		msgId = inputStream.readByte();
        		amount = inputStream.readByte();
        		if (amount != 0) {
        			params = new String[amount];
        			for (int i = 0; i < amount; i++)
        				params[i] = packet.readString(inputStream, inputStream.readByte());	
        		}
        	} catch (IOException e) {        		
        		e.printStackTrace();
        		return;
        	}  
    		EnumNMQMChatMessages.showMessage(msgId, (Object[]) params);
        }
	}	
	
	public static void syncContainers(EntityPlayer player) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(bos);                
        Packet250CustomPayload packet = new Packet250CustomPayload(NMQMMain.MODID + "_list", bos.toByteArray());     
    	byte amount = (byte) NMQMDataLoader.CONTAINERS_SERVER.size();
    	byte[] lengths = new byte[amount];
    	String[] containers = null;
    	if (amount != 0)
    		containers = new String[amount];
		int index = 0;
		for (String s : NMQMDataLoader.CONTAINERS_SERVER) {
			containers[index] = s;
			lengths[index] = (byte) s.length();
			index++;
		}
        try {  
        	outputStream.writeByte(amount);
    		for (int i = 0; i < amount; i++) {
    			outputStream.writeByte(lengths[i]);
    			packet.writeString(containers[i], outputStream);
    		}
        } catch (IOException e) {           	
        	e.printStackTrace();
        	return;
        }
    	PacketDispatcher.sendPacketToPlayer(PacketDispatcher.getPacket(NMQMMain.MODID + "_list", (byte[]) bos.toByteArray()), (Player) player);
	}
	
	public static void sendMessageData(EntityPlayer player, EnumNMQMChatMessages msg, String ... args) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(bos);                
        Packet250CustomPayload packet = new Packet250CustomPayload(NMQMMain.MODID + "_msg", bos.toByteArray());     
    	byte 
    	msgId = (byte) msg.getId(),
    	amount = (byte) args.length;
    	byte[] lengths = null;
    	String[] params = null;
    	if (amount != 0) {
	    	lengths = new byte[amount];
	    	params = new String[amount];
			int index = 0;
			for (String s : args) {
				params[index] = s;
				lengths[index] = (byte) s.length();
				index++;
			}
    	}
        try {  
        	outputStream.writeByte(msgId);
        	outputStream.writeByte(amount);
    		for (int i = 0; i < amount; i++) {
    			outputStream.writeByte(lengths[i]);
    			packet.writeString(params[i], outputStream);
    		}
        } catch (IOException e) {           	
        	e.printStackTrace();
        	return;
        }
    	PacketDispatcher.sendPacketToPlayer(PacketDispatcher.getPacket(NMQMMain.MODID + "_msg", (byte[]) bos.toByteArray()), (Player) player);
	}
}
