package org.drycell.main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.drycell.gui.InteractiveGUIManager;

import com.google.gson.Gson;

public class Drycell extends JavaPlugin {
	
	private static Drycell i;
	private Gson gson;
	
	public void onEnable(){
		i = this;
		this.gson = new Gson();
		Bukkit.getPluginManager().registerEvents(new InteractiveGUIManager(), this);
	}
	
	public Gson getGson(){
		return gson;
	}
	
	public static Drycell get(){ return i; } 

}
