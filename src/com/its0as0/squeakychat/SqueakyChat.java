package com.its0as0.squeakychat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SqueakyChat extends JavaPlugin implements Listener {

	public void onEnable() {
		getConfig().options().copyDefaults(true);
		saveConfig();
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		Bukkit.getServer().getLogger().info("SqueakyChat has been enabled!");
	}

	public void onDisable() {
		Bukkit.getServer().getLogger().info("SqueakyChat has been disabled!");
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		for (String word : e.getMessage().split(" ")) {
			if (getConfig().getStringList("blacklist").contains(word)) {
				String newMessage = e.getMessage().replace(word, getConfig().getString("censor"));
				e.setMessage(newMessage);
				getServer().getLogger().info("SqueakyChat : Player " + e.getPlayer().getName() + " attempted to swear!");
				e.getPlayer().sendMessage(ChatColor.RED + "Usage of profanity is not prohibited on this server!");
			}
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("squeakychat")) {
			if (!sender.hasPermission("squeakychat")) {
				sender.sendMessage(ChatColor.WHITE + "SqueakyChat " + ChatColor.BLUE + "\u00BB " + ChatColor.RED + "You do not have permission to use this command!");
				return true;
			} else {
				if (args.length == 0) {
					sender.sendMessage(ChatColor.WHITE + "SqueakyChat " + ChatColor.BLUE + "\u00BB " + ChatColor.WHITE + " Usage: </SqueakyChat> </scsetbleep>");
					return true;
				}
			}
		}

		if (cmd.getName().equalsIgnoreCase("scsetbleep")) {
			if (!sender.hasPermission("squeakychat.setbleep")) {
				sender.sendMessage(ChatColor.RED + "You do not have permission to run this command!");
				return true;
			}

			if (args.length == 0) {
				sender.sendMessage(ChatColor.WHITE + "SqueakyChat " + ChatColor.BLUE + "\u00BB " + ChatColor.RED + "Please specify a censor!");
				return true;
			}

			String censor = args[0];
			getConfig().set("censor", censor);
			saveConfig();
			sender.sendMessage(ChatColor.WHITE + "SqueakyChat " + ChatColor.BLUE + "\u00BB " + ChatColor.GREEN + "Censor set to: " + censor);
			return true;

		}

		return true;
	}

}
