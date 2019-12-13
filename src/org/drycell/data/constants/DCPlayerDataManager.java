package org.drycell.data.constants;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.drycell.main.DrycellPlugin;

public abstract class DCPlayerDataManager<T extends DCData> extends DCDataManager<T> implements Listener{

	public DCPlayerDataManager(DrycellPlugin plugin) {
		super(plugin);
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	public void loadCurrentPlayers(){
		//Attempt to all players online.
		for(Player p:Bukkit.getOnlinePlayers()){
			UUID id = p.getUniqueId();
			syncGetOrLoad(id);
		}
	}
	
	@EventHandler
	public void loadPlayerDataEvent(AsyncPlayerPreLoginEvent event){
		syncGetOrLoad(event.getUniqueId());
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event){
		UUID id = event.getPlayer().getUniqueId();
		//Save and uncache the player's data
		if(!cache.containsKey(id)){
			plugin.logger.error("Player left server without data loaded!");
			return;
		}
		T data = cache.get(id);
		executor.execute(() -> save(data));
		if(data.canUnload()){
			cache.remove(id);
		}
	}

	

}
