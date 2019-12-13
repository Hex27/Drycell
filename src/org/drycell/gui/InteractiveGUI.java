package org.drycell.gui;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InteractiveGUI{
	
	private Inventory inventory;
	private HashMap<Integer, Runnable> actions = new HashMap<Integer, Runnable>();
	
	public InteractiveGUI(String name, int size){
		inventory = Bukkit.createInventory(null, size,
				name);
	}
	
	public Inventory getInventory(){
		return inventory;
	}
	
	public void setAction(int slot, Runnable runnable){
		actions.put(slot, runnable);
	}
	
	public void openInventory(Player p){
		p.openInventory(inventory);
		InteractiveGUIManager.guis.put(p.getUniqueId(), this);
	}
	
	public Runnable getAction(int slot){
		if(!actions.containsKey(slot)) return null;
		return actions.get(slot);
	}

}
