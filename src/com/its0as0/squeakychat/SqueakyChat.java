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

	private SettingsManager settings = SettingsManager.getInstance();

	public void onEnable() {
		settings.setup(this);

		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		Bukkit.getServer().getLogger().info("SqueakyChat has been enabled!");
	}

	public void onDisable() {
		Bukkit.getServer().getLogger().info("SqueakyChat has been disabled!");
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		for (String word : e.getMessage().split(" ")) {
			if (settings.getConfig().getStringList("blacklist").contains(word)) {
				String newMessage = e.getMessage().replace(word, getConfig().getString("censor"));
				e.setMessage(newMessage);

				if (settings.getConfig().getConfigurationSection("alert-console").getBoolean("enabled")) {
					getServer().getLogger().info("SqueakyChat : Player " + e.getPlayer().getName() + " attempted to swear!");
				}

				if (settings.getConfig().getConfigurationSection("warning").getBoolean("enabled")) {
					e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', settings.getConfig().getConfigurationSection("warning").getString("message")));
				}
			}
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("squeakychat")) {
			if (!sender.hasPermission("squeakychat")) {
				sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
				return true;
			} else {
				if (args.length == 0) {
					sender.sendMessage("Commands: /squeakychat, /scsetbleep, /screload");
					return true;
				}
			}
		}

		if (cmd.getName().equalsIgnoreCase("scsetbleep")) {
			if (!sender.hasPermission("squeakychat.setbleep")) {
				sender.sendMessage(ChatColor.RED + "You do not have permission to run this command!");
				return true;
			} else {
				if (args.length == 0) {
					sender.sendMessage("Please specify a censor!");
					return true;
				}

				String censor = args[0];
				settings.getConfig().set("censor", censor);
				saveConfig();
				sender.sendMessage(ChatColor.WHITE + "SqueakyChat " + ChatColor.BLUE + "\u00BB " + ChatColor.GREEN + "Censor set to: " + censor);
				return true;
			}

		}

		if (cmd.getName().equalsIgnoreCase("screload")) {
			if (!sender.hasPermission("screload")) {
				sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
				return true;
			} else {
				if (args.length == 0) {
					settings.reloadConfig();
					sender.sendMessage(ChatColor.WHITE + "SqueakyChat " + ChatColor.BLUE + "\u00BB " + ChatColor.GREEN + "Reloaded config!");
				} else {
					return true;
				}
			}
		}

		return true;
	}

}
