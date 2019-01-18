package austeretony.nmqm.common.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;

public class PlayerLoggedInEvent extends Event {

	/**
	 * PlayerFallEvent fires when player logs in.<br>
	 * Fires via {@link ServerConfigurationManager#initializeConnectionToPlayer()}<br>
	 * <br>
	 * {@link #player} logged player.<br>
	 * <br>
	 * This event is not cancelable. <br>
	 * <br>
	 * This event has no result. {@link HasResult}<br>
	 * <br>
	 * Fires through {@link MinecraftForge#EVENT_BUS}.<br>
	 **/
	
	public final EntityPlayer player;
	
	public PlayerLoggedInEvent(EntityPlayer player) {
		this.player = player;
	}
	
	public EntityPlayer getPlayer() {
		return this.player;
	}
	
	@Override
    public boolean isCancelable() {
        return false;
    }
	
	@Override
    public boolean hasResult() {
        return false;
    }
}
