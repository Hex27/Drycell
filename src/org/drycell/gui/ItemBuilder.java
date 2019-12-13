package org.drycell.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {
	
	ItemStack itemstack;
	
	public ItemBuilder(Material material){
		this.itemstack = new ItemStack(material);
	}
	
	public ItemBuilder(ItemStack itemstack){
		this.itemstack = itemstack.clone();
	}

	public ItemBuilder setAmount(int amt){
		itemstack.setAmount(amt);
		return this;
	}
	
	public ItemBuilder setName(String name){
		name = ChatColor.translateAlternateColorCodes('&', name);
		ItemMeta m = itemstack.getItemMeta();
		m.setDisplayName(name);
		itemstack.setItemMeta(m);
		return this;
	}
	
	public ItemBuilder clone(){
		return new ItemBuilder(itemstack);
	}
	
	public ItemBuilder addLore(String newLore){
		return addLore(newLore,true);
	}
	
	public ItemBuilder addLore(String newLore, boolean overflow){
		newLore = ChatColor.translateAlternateColorCodes('&', newLore);
		ItemMeta m = itemstack.getItemMeta();
		List<String> lore = m.getLore();
		if(lore == null) lore = new ArrayList<String>();
		if(overflow){
			String line = "";
			String previousColor = "";
			//Bukkit.getLogger().info(newLore);
			for(int i = 0; i < newLore.length(); i++){
				char c = newLore.charAt(i);
				//Bukkit.broadcastMessage(ChatColor.COLOR_CHAR+"");
				if(c == ChatColor.COLOR_CHAR){
					//Bukkit.broadcastMessage("Worked.");
					previousColor = c+"";
				}else if(previousColor.equals(ChatColor.COLOR_CHAR+"")){
					previousColor += c;
					//Bukkit.broadcastMessage("Worked: " + previousColor);
				}
				line += c;
				if(line.length() > 20 && c == ' '){
					lore.add(line);
					line = previousColor + "";
				}
			}
			if(line.length()>0){
				lore.add(line);
			}
			
		}else
			lore.add(newLore);
		
		m.setLore(lore);
		itemstack.setItemMeta(m);
		return this;
	}
	
	public ItemStack build(){
		return itemstack;
	}

}
