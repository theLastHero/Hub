package com.ackeron.hub;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import listeners.PlayerListener;

public class Hub extends JavaPlugin{

	@Override
	public void onDisable() {
	}

	@Override
	public void onEnable() {
		

		Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		
	}

}
