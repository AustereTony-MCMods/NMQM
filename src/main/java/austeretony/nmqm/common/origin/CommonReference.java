package austeretony.nmqm.common.origin;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.command.ICommand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.FMLInjectionData;

public class CommonReference {

	public static String getGameFolder() {
		return ((File) (FMLInjectionData.data()[6])).getAbsolutePath();
	}
	
	public static void registerEvent(Object eventClazz) {
		MinecraftForge.EVENT_BUS.register(eventClazz);
	}
	
	public static void registerCommand(FMLServerStartingEvent event, ICommand command) {
		event.registerServerCommand(command);
	}
	
    public static boolean isOpped(EntityPlayer player) {
    	return MinecraftServer.getServer().getConfigurationManager().canSendCommands(player.getGameProfile());
    }
    
    public static Set<EntityPlayerMP> getPlayersServer() {
    	Set<EntityPlayerMP> players = new HashSet<EntityPlayerMP>();
		for (Object player : MinecraftServer.getServer().getConfigurationManager().playerEntityList)
			players.add((EntityPlayerMP) player);
    	return players;
    }
}
