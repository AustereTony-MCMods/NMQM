package austeretony.nmqm.common.reference;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.FMLInjectionData;
import net.minecraft.command.ICommand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IChatComponent;

public class CommonReference {

    public static String getGameFolder() {
        return ((File) (FMLInjectionData.data()[6])).getAbsolutePath();
    }

    public static void registerFMLEvent(Object eventClazz) {
        FMLCommonHandler.instance().bus().register(eventClazz);
    }

    public static void registerCommand(FMLServerStartingEvent event, ICommand command) {
        event.registerServerCommand(command);
    }

    public static boolean isOpped(EntityPlayer player) {
        return MinecraftServer.getServer().getConfigurationManager().func_152596_g(player.getGameProfile());
    }

    public static Set<EntityPlayerMP> getPlayersServer() {
        Set<EntityPlayerMP> players = new HashSet<EntityPlayerMP>();
        for (Object player : MinecraftServer.getServer().getConfigurationManager().playerEntityList)
            players.add((EntityPlayerMP) player);
        return players;
    }

    public static void sendMessage(EntityPlayer player, IChatComponent chatComponent) {
        player.addChatMessage(chatComponent);
    }
}
