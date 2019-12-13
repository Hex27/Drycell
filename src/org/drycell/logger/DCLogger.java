package org.drycell.logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class DCLogger {
	
	private JavaPlugin plugin;
	private String tag;
	private String error;
	private String debug;
	
	public DCLogger(JavaPlugin plugin){
		this.plugin = plugin;
		this.tag = "[" + plugin.getName() + "] ";
		this.error = "[" + plugin.getName() + "][!] ";
		this.debug = "[" + plugin.getName() + "][i] ";
	}
	
	public DCLogger(JavaPlugin plugin, String tag, String error, String debug){
		this.plugin = plugin;
		this.tag = tag + " ";
		this.error = error + " ";
		this.debug = debug + " ";
	}
	
	public void info(String message){
		Bukkit.getConsoleSender().sendMessage(tag + message);
	}
	
	public void error(String message){
		Bukkit.getConsoleSender().sendMessage(error + message);
	}

	public void debug(String message){
		Bukkit.getConsoleSender().sendMessage(debug + message);
	}

	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * @return the error
	 */
	public String getError() {
		return error;
	}

	/**
	 * @return the debug
	 */
	public String getDebug() {
		return debug;
	}

	/**
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * @param debug the debug to set
	 */
	public void setDebug(String debug) {
		this.debug = debug;
	}
}
