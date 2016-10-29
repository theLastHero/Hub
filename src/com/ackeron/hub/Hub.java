package com.ackeron.hub;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import listeners.PlayerListener;

public class Hub extends JavaPlugin implements PluginMessageListener {

	public static int survivalPlayerCount = 0;
	public static int kitpvpPlayerCount = 0;
	public static int skywarPlayerCount = 0;
	public String serverNAme = "";
	public static BossBar bossBar;

	@Override
	public void onDisable() {
		bossBar.removeAll();
	}

	@Override
	public void onEnable() {

		// registers
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

		//
		PlayerListener.setUpInvEveryone();

		bossBar = Bukkit.getServer().createBossBar("", BarColor.PINK, BarStyle.SEGMENTED_20, new BarFlag[0]);
		bossBar.setTitle(ChatColor.GREEN.toString() + "YOU ARE PLAYING ON " + ChatColor.AQUA.toString()
				+ ChatColor.BOLD.toString() + "TheLastHero");

		startCountdown();

	}

	public static void setBossBar() {

		bossBar.removeAll();

		for (Player online : Bukkit.getOnlinePlayers()) {
			bossBar.addPlayer(online);
			bossBar.setVisible(true);
		}

	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}

		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();

		if (subchannel.equals("PlayerCount")) {

			serverNAme = in.readUTF();
			if (serverNAme != null) {
				// Bukkit.broadcastMessage(serverNAme);

				if (serverNAme.equalsIgnoreCase("Survival")) {
					survivalPlayerCount = in.readInt();
					// Bukkit.broadcastMessage(survivalPlayerCount+"");
				}

				if (serverNAme.equalsIgnoreCase("Lobby")) {
					skywarPlayerCount = in.readInt();
					// Bukkit.broadcastMessage(skywarPlayerCount+"");
				}

				if (serverNAme.equalsIgnoreCase("Kit")) {
					kitpvpPlayerCount = in.readInt();
					// Bukkit.broadcastMessage(kitpvpPlayerCount+"");
				}
			}
		}

	}

	@SuppressWarnings("deprecation")
	public void startCountdown() {

		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

			@Override
			public void run() {

				setBossBar();

				Bukkit.getWorld("SUBHUB").setTime(2000);
				Bukkit.getWorld("SUBHUB").setStorm(false);
				Bukkit.getWorld("SUBHUB").setThundering(false);

				List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

				if (players.size() > 0) {

					Player p = players.get(0);

					ByteArrayDataOutput out = ByteStreams.newDataOutput();
					out.writeUTF("PlayerCount");
					out.writeUTF("Survival");
					p.sendPluginMessage(Hub.getPlugin(Hub.class), "BungeeCord", out.toByteArray());

					ByteArrayDataOutput out2 = ByteStreams.newDataOutput();
					out2.writeUTF("PlayerCount");
					out2.writeUTF("Lobby");
					p.sendPluginMessage(Hub.getPlugin(Hub.class), "BungeeCord", out2.toByteArray());

					ByteArrayDataOutput out3 = ByteStreams.newDataOutput();
					out3.writeUTF("PlayerCount");
					out3.writeUTF("Kit");
					p.sendPluginMessage(Hub.getPlugin(Hub.class), "BungeeCord", out3.toByteArray());
				}
			}

		}, 20L, 40L);

	}
}
