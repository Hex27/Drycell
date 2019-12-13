package org.drycell.main;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.drycell.command.DCCommandManager;
import org.drycell.config.DCConfig;
import org.drycell.data.constants.DCDataManager;
import org.drycell.gui.InteractiveGUIManager;
import org.drycell.lang.DCLanguageManager;
import org.drycell.logger.DCLogger;

public class DrycellPlugin extends JavaPlugin {
	
	public static DCLogger logger;
	private HashMap<Class, DCDataManager> managers = new HashMap<>();
	private DCConfig config;
	private DCLanguageManager lang;
	
	public DCDataManager getDataManager(Class c){
		return managers.get(c);
	}
	
	@Override
	public void onDisable(){
		for(DCDataManager man:managers.values()){
			man.onDisable();
		}
	}
	
	public void registerDataManager(Class type, DCDataManager manager){
		this.managers.put(type,manager);
		logger.info(manager.getClass().getName() + " registered");
	}
	
	@Override
	public void onEnable(){
		logger = new DCLogger(this);
		this.config = new DCConfig(this);
		this.lang = new DCLanguageManager(this);
	}
	
	public DCLanguageManager getLang(){
		return lang;
	}
	
	public DCConfig getDCConfig(){
		return this.config;
	}
}
