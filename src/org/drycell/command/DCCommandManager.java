package org.drycell.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.drycell.main.DrycellPlugin;

public class DCCommandManager implements CommandExecutor{
	
	private ArrayList<DCCommand> commands = new ArrayList<>();

	private DrycellPlugin plugin;
	public ArrayList<String> bases = new ArrayList<String>();
	
	public DCCommandManager(DrycellPlugin plugin,String... bases){
		this.plugin = plugin;
		for(String base:bases){
			this.bases.add(base);
			plugin.getCommand(base).setExecutor(this);
		}
		registerCommand(new DCHelpCommand(plugin,this,"help","h","?"));
	}
	
	public void unregisterCommand(Class clazz){
		Iterator<DCCommand> it = commands.iterator();
		while(it.hasNext()){
			DCCommand cmd = it.next();
			if(clazz.isInstance(cmd)){
				it.remove();
			}
		}
	}
	
	public void unregisterCommand(String alias){
		Iterator<DCCommand> it = commands.iterator();
		while(it.hasNext()){
			DCCommand cmd = it.next();
			if(cmd.matchCommand(alias)){
				it.remove();
			}
		}
	}
	
	public ArrayList<DCCommand> getCommands(){
		return commands;
	}
	
	public void registerCommand(DCCommand cmd){
		this.commands.add(cmd);
		plugin.getLang().fetchLang("command." + cmd.aliases.get(0) + ".desc",cmd.getDefaultDescription());
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,
			String[] args) {
		if(args.length == 0){
			sender.sendMessage(plugin.getLang().fetchLang("command.unknown"));
			try {
				new DCHelpCommand(plugin, this).execute(sender, new Stack<String>());
			} catch (InvalidArgumentException e) {
				sender.sendMessage(ChatColor.RED + e.getProblem());
			}
			return false;
		}
		for(DCCommand command:commands){
			if(command.matchCommand(args[0].toLowerCase())){
				Stack<String> stack = new Stack<String>();
				//Push arguments from back to front, except the 1st arg
				for(int i = args.length -1; i>=1; i--){
					stack.push(args[i]);
				}
				if(!command.hasPermission(sender)){
					sender.sendMessage(plugin.getLang().fetchLang("permissions.insufficient"));
					return false;
				}
				if(!command.canConsoleExec() && !(sender instanceof Player)){
					sender.sendMessage(plugin.getLang().fetchLang("permissions.console-cannot-exec"));
					return false;
				}
				if(!command.isInAcceptedParamRange(stack)){
					sender.sendMessage(plugin.getLang().fetchLang("command.wrong-arg-length"));
					return false;
				}
				try{
					command.execute(sender, stack);
					return true;
				}catch(InvalidArgumentException e){
					sender.sendMessage(ChatColor.RED + e.getProblem());
				}
			}
		}
		sender.sendMessage(plugin.getLang().fetchLang("command.unknown"));
		return false;
	}
	

}
