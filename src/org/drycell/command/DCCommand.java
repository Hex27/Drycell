package org.drycell.command;

import java.util.ArrayList;
import java.util.Stack;

import org.bukkit.command.CommandSender;
import org.drycell.main.DrycellPlugin;

public abstract class DCCommand {
	
	public ArrayList<String> aliases = new ArrayList<>();
	public ArrayList<DCArgument> parameters = new ArrayList<DCArgument>();
	public DrycellPlugin plugin;
	public DCCommand(DrycellPlugin plugin, String... aliases){
		this.plugin = plugin;
		for(String alias:aliases){
			this.aliases.add(alias);
		}
	}
	
	public abstract String getDefaultDescription();
	
	public boolean isInAcceptedParamRange(Stack<String> args){
		if(args.size() > this.parameters.size()) return false;
		if(this.parameters.size() == 0) return true;
		int lowerBound = 0;
		for(DCArgument arg:parameters){
			if(!arg.isOptional()) lowerBound++;
		}
		return args.size() >= lowerBound;
	}
	
	public String getLangPath(){
		return "command." + aliases.get(0) + ".desc";
	}
	public abstract boolean canConsoleExec();
	
	public abstract boolean hasPermission(CommandSender sender);
	
	public abstract void execute(CommandSender sender, Stack<String> args) throws InvalidArgumentException;
	
	/**
	 * Call this method to parse an arraylist of objects parsed by the argument handler
	 * @param sender
	 * @param args
	 * @return
	 * @throws InvalidArgumentException
	 */
	public ArrayList<Object> parseArguments(CommandSender sender, Stack<String> args) throws InvalidArgumentException{
		ArrayList<Object> items = new ArrayList<>(args.size());
		
		int i = 0;
		while(args.size() > 0){
			String arg = args.pop();
			DCArgument parser = parameters.get(i);
			Object parsed = parser.parse(sender, arg);
			if(parsed == null) throw new InvalidArgumentException(parser.validate(sender,arg));
			items.add(i, parsed);
			i++;
		}
		return items;
	}
	
	public String getNextArg(Stack<String> args){
		if(args.empty()) return null;
		return args.pop();
	}
	
	public boolean matchCommand(String command){
		command = command.toLowerCase();
		return aliases.contains(command);
	}

}
