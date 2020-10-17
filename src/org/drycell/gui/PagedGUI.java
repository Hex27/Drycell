package org.drycell.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.drycell.utils.XMaterial;

public class PagedGUI {

	private int page = 0;
	private int maxPage = 0;
	private ArrayList<InteractiveGUI> pages = new ArrayList<>();
	public static HashMap<UUID, PagedGUI> guis = new HashMap<>();
	private String name;
	private static final ItemStack nextPage = new ItemBuilder(
			XMaterial.PURPLE_STAINED_GLASS_PANE.parseItem())
	.setName(ChatColor.LIGHT_PURPLE + ">>")
	.build();
	

	private static final ItemStack prevPage = new ItemBuilder(
			XMaterial.GREEN_STAINED_GLASS_PANE.parseItem())
	.setName(ChatColor.GREEN + "<<")
	.build();
	
	public PagedGUI(String name) {
		super();
		this.name = name;
	}
	
	public void registerItemAndAction(ItemStack icon, Runnable runnable, Player p){
		if(pages.size() == 0){
			pages.add(new InteractiveGUI(this.name,54));
		}
		
		//get latest page
		InteractiveGUI gui = pages.get(pages.size()-1);
		
		//Check if full
		if(gui.getInventory().firstEmpty() >= 45){
			maxPage++;
				
			gui.getInventory().setItem(54,nextPage);
			gui.setAction(54, new Runnable(){
				public void run(){
					pages.get(maxPage).openInventory(p);
				}
			});

			pages.add(new InteractiveGUI(this.name,54));
			gui = pages.get(pages.size()-1);
			gui.getInventory().setItem(45,prevPage);	
			gui.setAction(45, new Runnable(){
				public void run(){
					pages.get(maxPage-1).openInventory(p);
				}
			});
		}

		gui.setAction(gui.getInventory().firstEmpty(), runnable);
		gui.getInventory().addItem(icon);
		
	}

	public void openInventory(Player p){
		pages.get(0).openInventory(p);
		guis.put(p.getUniqueId(),this);
	}
	
	public class PagedGUIState{
		
		private PagedGUI ui;
		private int page = 0;
		
		
	}

}
