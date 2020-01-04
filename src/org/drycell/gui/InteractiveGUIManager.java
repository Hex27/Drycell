
package org.drycell.gui;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;


public class InteractiveGUIManager implements Listener {

	protected static HashMap<UUID, InteractiveGUI> guis = new HashMap<UUID, InteractiveGUI>();
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onInventoryClick(InventoryClickEvent event){
		Player p = (Player) event.getWhoClicked();
		if(event.getCurrentItem() == null) return;
		if(!guis.containsKey(p.getUniqueId())) return;
		event.setCancelled(true);
		if(event.getClick() != ClickType.LEFT) return;
		if(guis.get(p.getUniqueId()).getAction(event.getSlot()) == null) return;
		if(event.getClickedInventory().getType() != InventoryType.CHEST) return;
		guis.get(p.getUniqueId()).getAction(event.getSlot()).run();
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event){
		guis.remove(event.getPlayer().getUniqueId());
		PagedGUI.guis.remove(event.getPlayer().getUniqueId());
	}
}
