package org.drycell.command;

import org.bukkit.command.CommandSender;

public abstract class DCArgument<T> {
	
	private String name;

	private boolean isOptional;
	
	public DCArgument(String name, boolean isOptional) {
		super();
		this.name = name;
		this.isOptional = isOptional;
	}

	public abstract T parse(CommandSender sender,String value);
	
	public abstract String validate(CommandSender sender, String value);

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the isOptional
	 */
	public boolean isOptional() {
		return isOptional;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param isOptional the isOptional to set
	 */
	public void setOptional(boolean isOptional) {
		this.isOptional = isOptional;
	}

}
