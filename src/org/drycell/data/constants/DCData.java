package org.drycell.data.constants;

import java.util.UUID;

import org.drycell.main.Drycell;

public abstract class DCData {
	
	private UUID id;
	private transient long lastAccess = System.currentTimeMillis();
	
	public boolean canUnload(){
		long now = System.currentTimeMillis();
		long cachePeriod = 1000*60*60;
		return now-lastAccess > cachePeriod;
	}
	
	public void resetAccess(){
		this.lastAccess = System.currentTimeMillis();
	}
	
	public DCData(){
		this.id = UUID.randomUUID();
		resetAccess();
	}
	
	public DCData(UUID id){
		this.id = id;
		resetAccess();
	}
	
	public UUID getUUID(){
		return this.id;
	}

}
