package com.its0as0.squeakychat;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class SettingsManager {

	private Plugin p;
	private FileConfiguration config;
	private File cFile;

	private SettingsManager() {
	}

	private static SettingsManager instance = new SettingsManager();

	public static SettingsManager getInstance() {
		return instance;
	}

	public FileConfiguration getConfig() {
		return config;
	}

	public void reloadConfig() {
		config = YamlConfiguration.loadConfiguration(cFile);
	}

	public void saveConfig() {
		try {
			config.save(cFile);
		} catch (IOException e) {
			Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save config.yml!");
		}
	}

	public void setup(Plugin p) {
		config = p.getConfig();
		config.options().copyDefaults(true);
		cFile = new File(p.getDataFolder(), "config.yml");
		saveConfig();
	}

	public PluginDescriptionFile getDesc() {
		return p.getDescription();
	}

}
