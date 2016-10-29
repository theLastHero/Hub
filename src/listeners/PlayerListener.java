package listeners;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.ackeron.hub.Hub;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class PlayerListener implements Listener {

	public static ItemStack slimeball;
	public static ItemStack magnacream;

	// drop item cancel event
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();

		if (p.isOp() == false) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onInventoryItemMove(InventoryClickEvent e) {
		if(e.getWhoClicked().isOp()==false){
		e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e){
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	  {
	    Player player = event.getPlayer();
	    Location playerLoc = player.getLocation();
	    int ID = playerLoc.getWorld().getBlockAt(playerLoc).getRelative(0, -1, 0).getTypeId();
	    int plate = playerLoc.getWorld().getBlockAt(playerLoc).getTypeId();
	    if (player instanceof Player){
	        if (plate == 70)
	        {
	          player.setVelocity(player.getLocation().getDirection().multiply(3));
	          player.setVelocity(new Vector(player.getVelocity().getX(), 1.0D, player.getVelocity().getZ()));
	          player.playSound(player.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.0F, 1.0F);
	      }
	    }
	  }
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerUse(PlayerInteractEvent e) {
		Player p = e.getPlayer();


		if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_AIR)) {

			// SLIME_BALL
			if (p.getItemInHand().getType() == Material.SLIME_BALL
					|| p.getItemInHand().getType() == Material.MAGMA_CREAM) {

				if (p.getItemInHand().getType() == Material.SLIME_BALL) {
					for (Player ps : Bukkit.getOnlinePlayers()) {
						p.hidePlayer(ps);
					}
					p.setItemInHand(magnacream);
					return;
				}

				if (p.getItemInHand().getType() == Material.MAGMA_CREAM) {
					for (Player ps : Bukkit.getOnlinePlayers()) {
						p.showPlayer(ps);
					}
					p.setItemInHand(slimeball);
					return;
				}

				return;
			}

			// SLOT 1
			if (p.getItemInHand().getType() == Material.ENDER_PEARL) {
				e.setCancelled(true);
				showInv(p);
				return;
			}
		}

	}

	public static void sentToServer(Player p, String server) {

		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(server);
		p.sendPluginMessage(Hub.getPlugin(Hub.class), "BungeeCord", out.toByteArray());

	}

	//
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		p.teleport(new Location(p.getWorld(), 514, 37, 651, (float) -97, (float) 3.5));
		setUpInvSingle(p);

	}

	public static void setUpInvEveryone() {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			setUpInvSingle(p);
		}
	}

	public static void setUpInvSingle(Player p) {

		p.getInventory().clear();

		ItemStack slot0 = buildItemStack(new ItemStack(Material.ENDER_PEARL),
				ChatColor.AQUA.toString() + "Navigator " + ChatColor.GRAY.toString() + "(Right Click)",
				ChatColor.WHITE.toString() + "Right Click to open server selector");
		p.getInventory().setItem(0, slot0);

		slimeball = buildItemStack(
				new ItemStack(Material.SLIME_BALL), ChatColor.AQUA.toString() + "Players: " + ChatColor.GREEN.toString()
						+ "ON " + ChatColor.GRAY.toString() + "(Right Click)",
				ChatColor.WHITE.toString() + "Right click to toggle player visibilty");
		p.getInventory().setItem(8, slimeball);

		magnacream = buildItemStack(new ItemStack(Material.MAGMA_CREAM),
				ChatColor.AQUA.toString() + "Players: " + ChatColor.GREEN.toString() + "OFF "
						+ ChatColor.GRAY.toString() + "(Right Click)",
				ChatColor.WHITE.toString() + "Right click to toggle player visibilty");
		p.getInventory().setItem(8, slimeball);

	}

	public static void runCounts(Player p, String serverName) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("PlayerCount");
		out.writeUTF(serverName);

		p.sendPluginMessage(Hub.getPlugin(Hub.class), "BungeeCord", out.toByteArray());
	}

	public static ItemStack buildItemStack(ItemStack is, String displayName, String lore1) {

		ItemStack toEnchant = is;
		ItemMeta toEnchantMeta = toEnchant.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();

		// add displayName
		if (displayName != null) {
			toEnchantMeta.setDisplayName(displayName);
		}

		lore.add(lore1);
		toEnchantMeta.setLore(lore);
		// save
		toEnchant.setItemMeta(toEnchantMeta);
		return toEnchant;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();

		if (e.getCurrentItem().equals(null) || e.getInventory().equals(null)) {
			return;
		}
		if (e.getCurrentItem().getType() == Material.DIAMOND_SWORD) {
			sentToServer(p, "survival");
		}
	}

	public void showInv(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, ChatColor.DARK_AQUA + "Servers");

		// SERVER!
		ItemStack server1 = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta server1Meta = server1.getItemMeta();
		server1Meta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Survival");
		//
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.WHITE.toString() + "Online: " + ChatColor.GRAY.toString() + "(" + Hub.survivalPlayerCount
				+ "/200)");
		lore.add("");
		lore.add(ChatColor.WHITE.toString() + " Economy Survival Server");
		lore.add(ChatColor.WHITE.toString() + " Over 200 custom biomes and many");
		lore.add(ChatColor.WHITE.toString() + " custom monster designs.");
		lore.add("");
		lore.add(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Click to join!");

		server1Meta.setLore(lore);
		server1Meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		server1.setItemMeta(server1Meta);

		inv.setItem(20, server1);

		// SERVER!
		ItemStack server2 = new ItemStack(Material.GRASS);
		ItemMeta server2Meta = server2.getItemMeta();
		server2Meta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Skyblock");
		//
		ArrayList<String> lore2 = new ArrayList<String>();
		lore2.add(ChatColor.WHITE.toString() + "Online: " + ChatColor.GRAY.toString() + "(" + Hub.skywarPlayerCount
				+ "/180)");
		lore2.add("");
		lore2.add(ChatColor.WHITE.toString() + " Peaceful & Fun Island Survival");
		lore2.add(ChatColor.WHITE.toString() + " Complete challenges, unlock new");
		lore2.add(ChatColor.WHITE.toString() + " ranks, team, cat & level up!");
		lore2.add("");
		lore2.add(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Click to join!");

		server2Meta.setLore(lore2);
		server2Meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		server2.setItemMeta(server2Meta);

		inv.setItem(22, server2);

		// SERVER
		ItemStack server3 = new ItemStack(Material.IRON_HELMET);
		ItemMeta server3Meta = server3.getItemMeta();
		server3Meta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Kit PVP");
		//
		ArrayList<String> lore3 = new ArrayList<String>();
		lore3.add(ChatColor.WHITE.toString() + "Online: " + ChatColor.GRAY.toString() + "(" + Hub.kitpvpPlayerCount
				+ "/180)");
		lore3.add("");
		lore3.add(ChatColor.WHITE.toString() + " Grab a Kit and Fight!");
		lore3.add(ChatColor.WHITE.toString() + " Earn money, level up,");
		lore3.add(ChatColor.WHITE.toString() + " unlock supply crates and more!");
		lore3.add("");
		lore3.add(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Click to join!");

		server3Meta.setLore(lore3);
		server3Meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		server3.setItemMeta(server3Meta);

		inv.setItem(24, server3);

		p.openInventory(inv);
	}

}
