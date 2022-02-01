package org.drycell.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.drycell.main.DrycellPlugin;

public class DCCommandManager implements TabExecutor{
	
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
	
	public void unregisterCommand(Class<?> clazz){
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
					return false;
				}
			}
		}
		sender.sendMessage(plugin.getLang().fetchLang("command.unknown"));
		return false;
	}
	
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String alias, String[] args) {
	    List<String> options = new ArrayList<>();
        if (args.length == 0) {
            for (DCCommand dcCommand : commands) {
                if (dcCommand.hasPermission(commandSender))
                    options.add(dcCommand.aliases.get(0));
            }
        }else if(args.length == 1) {
            for (DCCommand dcCommand : commands) {
                if (dcCommand.hasPermission(commandSender))
                	for(String a:dcCommand.aliases) {
                		if(a.startsWith(args[0].toLowerCase())) {
                			options.add(dcCommand.aliases.get(0));
                			break;
                		}
                	}
            }
        }else {
            for (DCCommand dcCommand : commands) {
                if (dcCommand.matchCommand(args[0].toLowerCase())) {
                    for (DCArgument<?> arg : dcCommand.parameters) {
                        options.addAll(arg.getTabOptions(args));
                    }
                    break;
                }
            }
        }

        return options;
    }
	

}
