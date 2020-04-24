package org.drycell.data.constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.drycell.main.Drycell;
import org.drycell.main.DrycellPlugin;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;

public abstract class DCDataManager<T extends DCData> {
	
	DrycellPlugin plugin;
	protected HashMap<UUID,T> cache = new HashMap<>();
	private AutoSaveTask ast;
	private boolean saving = false;
	ExecutorService executor = Executors.newFixedThreadPool(1);
	public Class<T> type;
	
	public DCDataManager(DrycellPlugin plugin){
		this.plugin = plugin;
		
		//Create directories
		File saveDir = new File(getSaveFolderPath());
		if(!saveDir.exists()) saveDir.mkdir();
		
		//Begin autosave thread
		this.ast = new AutoSaveTask();
		long delay = 20*60*15; //15 minutes
		ast.runTaskTimerAsynchronously(plugin, delay, delay);
	}
	
	public void onDisable(){
		ast.cancel(); //Stop autosaving and save one last time.
		Future<Boolean> future = executor.submit(() -> saveAll());
		try {
			future.get(60*3, TimeUnit.SECONDS);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		//Wait for saving for 3 minutes. Terminate if it doesn't work
	}
	
	public boolean saveAll(){
		return saveAll(true);
	}
	
	/**
	 * To be run asynchronously. If another save is ongoing, this
	 * function will do nothing.
	 * @return whether or not a save was done.
	 */
	public boolean saveAll(boolean force){
		try{
			if(this.saving){
				return false;
			}
			this.saving = true;
			HashMap<UUID,T> items = (HashMap<UUID, T>) cache.clone();
			for(T item:items.values()){
				save(item);
			}
			this.saving = false;
			return true;
		}catch(Throwable e){
			e.printStackTrace();
		}
		return false;
	}
	
	
	private class AutoSaveTask extends BukkitRunnable{

		@Override
		public void run() {
			plugin.logger.info("Autosaved initiated.");
			Future<Boolean> future = executor.submit(() -> saveAll());
			try {
				if(future.get()){
					plugin.logger.info("Autosave Successful.");
				}else{
					plugin.logger.error("Autosave Failed.");
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public T getData(UUID id){
		T data = cache.get(id);
		if(data!= null) data.resetAccess();
		return data;
	}
	
	public HashMap<UUID,T> fetchAllData(){
		File[] files = new File(getSaveFolderPath()).listFiles();
		HashMap<UUID, T> allData = new HashMap<>();
		
		for(File file:files){
			if(file.getName().endsWith(".json")){
				try{
					UUID id = UUID.fromString(file.getName().replaceAll(".json", ""));
					allData.put(id,loadOrCreate(id));
				}catch(Throwable e){
					e.printStackTrace();
					plugin.logger.error("Failed to load " + file.getName() + "!");
				}
			}
		}
		return allData;
	}

	public T syncGetOrLoad(UUID id){
		if(cache.containsKey(id)){
			cache.get(id).resetAccess();
			return cache.get(id);
		}
		//Bukkit.getLogger().info("Sync get or load called, cache empty.");
		T data = loadOrCreate(id);
		//Bukkit.getLogger().info("loadOrCreate finished.");
		if(data != null)
			cache.put(id,data);
		return data;
	}
	
	/**
	 * Can be used asynchronously.
	 * @param id
	 */
	public void loadAndCacheOrCreate(UUID id){
		if(cache.containsKey(id)) return;
		T data = loadOrCreate(id);
		if(data != null)
			cache.put(id,data);
	}
	
	public void uncacheAndSave(UUID id){
		if(!cache.containsKey(id)) return;
		
		T item = cache.remove(id);
		
		save(item);
	}
	
	public T loadOrCreate(UUID id){
		try
        {
			if(!new File(getSaveFolderPath() + id + ".json").exists()){
				return createNewData(id);
			}
			//FileReader fr = new FileReader(getSaveFolderPath() + id + ".json");
			//BufferedReader reader =new BufferedReader(fr);
            //Read JSON file
			//Bukkit.getLogger().info("Attempting file read...");
			String json = new String(Files.readAllBytes(Paths.get(getSaveFolderPath() + id + ".json"))); ;
//			int i;
//			while ((i=reader.read()) != -1){
//				json += (char) i;
//			}
//    		reader.close();
			//Bukkit.getLogger().info("Beginning deserialisation");
            T item = deserialise(json);
    		//Bukkit.getLogger().info("Done!");
            return item;
        } catch (FileNotFoundException e) {
        	//User does not exist.
    		return createNewData(id);
        } catch (Throwable e) {
            e.printStackTrace();
        }
		return null;
	}
	
	public abstract T createNewData(UUID id);
	
	public void save(T item){
		try{
			if(item == null) return;
			String json = serialise(item);
			try (FileWriter file = new FileWriter(getSaveFolderPath() + item.getUUID() + ".json")) {
	            file.write(json);
	            file.flush();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		} catch (Throwable e){
			e.printStackTrace();
		}
	}
	
	public void delete(UUID item){
		try{
			cache.remove(item);
			if(item == null) return;
			File file = new File(getSaveFolderPath() + item.toString() + ".json");
			if(file.exists())
				file.delete();
		} catch (Throwable e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return the relative path from the server directory
	 * e.g. plugins/Swamp/stuff/
	 */
	public abstract String getSaveFolderPath();
	
	public String serialise(T data){
		return Drycell.getGson().toJson(data);
	}
	
	public T deserialise(String json){
		if(json == null) return null;
		return Drycell.getGson().fromJson(json, type);
	}

}
