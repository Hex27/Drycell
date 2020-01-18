package org.drycell.main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.drycell.gui.InteractiveGUIManager;

import com.google.gson.Gson;

public class Drycell extends JavaPlugin {
	
	private static Drycell i;
	private static Gson gson;
	
	public void onEnable(){
		Bukkit.getLogger().info("Drycell has initiated!");
		i = this;
		getGson();
		Bukkit.getPluginManager().registerEvents(new InteractiveGUIManager(), this);
	}
	
	public static Gson getGson(){
		if(gson == null) gson = new Gson();
		return gson;
	}
	
	public static Drycell get(){ return i; } 

}
