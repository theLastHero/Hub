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
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;

import com.ackeron.hub.Hub;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class PlayerListener implements Listener {

	public static ItemStack slimeball;
	public static ItemStack magnacream;
	public static ItemStack goldnugget;
	public static ItemStack playerHead;

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
			
			// SLOT 1
			if (p.getItemInHand().getType() == Material.GOLD_NUGGET) {
				e.setCancelled(true);
				showTokenInv(p);
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
				ChatColor.WHITE.toString() + "Right Click to open server selector!");
		p.getInventory().setItem(0, slot0);

		slimeball = buildItemStack(
				new ItemStack(Material.SLIME_BALL), ChatColor.AQUA.toString() + "Players: " + ChatColor.GREEN.toString()
						+ "ON " + ChatColor.GRAY.toString() + "(Right Click)",
				ChatColor.WHITE.toString() + "Right click to toggle player visibilty!");
		p.getInventory().setItem(8, slimeball);

		magnacream = buildItemStack(new ItemStack(Material.MAGMA_CREAM),
				ChatColor.AQUA.toString() + "Players: " + ChatColor.GREEN.toString() + "OFF "
						+ ChatColor.GRAY.toString() + "(Right Click)",
				ChatColor.WHITE.toString() + "Right click to toggle player visibilty!");
		p.getInventory().setItem(8, slimeball);
		
		goldnugget = buildItemStack(new ItemStack(Material.GOLD_NUGGET),
				ChatColor.GOLD.toString() + "Token Shop "
						+ ChatColor.GRAY.toString() + "(Right Click)",
				ChatColor.WHITE.toString() + "Right click to open token shop!");
		p.getInventory().setItem(4, goldnugget);
		
		playerHead = buildItemStack(new ItemStack(Material.SKULL_ITEM, 1, (short) 3),
				ChatColor.GOLD.toString() + "Player Profile "
						+ ChatColor.GRAY.toString() + "(Right Click)",
				ChatColor.WHITE.toString() + "Right click to view your profile!");
		SkullMeta Meta_Skull = (SkullMeta) playerHead.getItemMeta();
		Meta_Skull.setOwner(p.getName());
		
		
		p.getInventory().setItem(1, playerHead);

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
	public void onFoodLevelChange(FoodLevelChangeEvent event)
	{
	event.setFoodLevel(20);
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
		if (e.getCurrentItem().getType() == Material.GRASS) {
			sentToServer(p, "skyblock");
		}
		if (e.getCurrentItem().getType() == Material.IRON_HELMET) {
			sentToServer(p, "kit");
		}
	}
	
	public void showTokenInv(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, ChatColor.DARK_AQUA + "Token Shop");
		
		//PARTICLES!
		ItemStack tokenItem0 = new ItemStack(Material.GOLD_NUGGET);
		ItemMeta tokenItem0Meta = tokenItem0.getItemMeta();
		tokenItem0Meta.setDisplayName(ChatColor.AQUA.toString() + "Your Tokens");
		//
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("");
		lore.add(ChatColor.GRAY.toString() + "You have " + ChatColor.GREEN + "0" +ChatColor.GRAY + " tokens.");
		lore.add("");
		lore.add(ChatColor.YELLOW.toString() + "Tokens drop randomally on every server.");
		lore.add(ChatColor.YELLOW.toString() + "Grab them by mining, killing or just walking around!");
		lore.add("");
		
		tokenItem0Meta.setLore(lore);
		tokenItem0Meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		tokenItem0.setItemMeta(tokenItem0Meta);
		inv.setItem(4, tokenItem0);

		//PARTICLES!
		lore.clear();
		ItemStack tokenItem1 = new ItemStack(Material.FIREBALL);
		ItemMeta tokenItem1Meta = tokenItem1.getItemMeta();
		tokenItem1Meta.setDisplayName(ChatColor.AQUA.toString() + "Particles");
		//
		lore.add("");
		lore.add(ChatColor.GRAY.toString() + "Buy particle effects to");
		lore.add(ChatColor.GRAY.toString() + "show off to your friends!");
		lore.add("");
		lore.add(ChatColor.YELLOW.toString() + "Applys to all servers and lasts forever!");
		lore.add("");
		lore.add(ChatColor.GRAY + "Access with " + ChatColor.AQUA + "/particles");

		tokenItem1Meta.setLore(lore);
		tokenItem1Meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		tokenItem1.setItemMeta(tokenItem1Meta);
		inv.setItem(19, tokenItem1);
		
		//TITLES
		ItemStack tokenItem2 = new ItemStack(Material.NAME_TAG);
		ItemMeta tokenItem2Meta = tokenItem2.getItemMeta();
		tokenItem2Meta.setDisplayName(ChatColor.AQUA.toString() +  "Titles");
		//
		lore.clear();
		lore.add("");
		lore.add(ChatColor.GRAY.toString() + "Unlock prefixes in front");
		lore.add(ChatColor.GRAY.toString() + "of your name!");
		lore.add("");
		lore.add(ChatColor.YELLOW.toString() + "Applys to all servers and lasts forever!");
		lore.add("");
		lore.add(ChatColor.GRAY + "Access with " + ChatColor.AQUA + "/titles");

		tokenItem2Meta.setLore(lore);
		tokenItem2Meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		tokenItem2.setItemMeta(tokenItem2Meta);

		inv.setItem(21, tokenItem2);
		
		//PETS
		ItemStack tokenItem3 = new ItemStack(Material.EGG);
		ItemMeta tokenItem3Meta = tokenItem3.getItemMeta();
		tokenItem3Meta.setDisplayName(ChatColor.AQUA.toString()  + "Pets");
		//
		lore.clear();
		lore.add("");
		lore.add(ChatColor.GRAY.toString() + "Unlock a new sidekick or");
		lore.add(ChatColor.GRAY.toString() + "best friend by your side!");
		lore.add("");
		lore.add(ChatColor.YELLOW.toString() + "Applys to all servers and lasts forever!");
		lore.add("");
		lore.add(ChatColor.GRAY + "Access with " + ChatColor.AQUA + "/pets");

		tokenItem3Meta.setLore(lore);
		tokenItem3Meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		tokenItem3.setItemMeta(tokenItem3Meta);

		inv.setItem(23, tokenItem3);
		
		//PDISGUISES
		ItemStack tokenItem4 = new ItemStack(Material.MONSTER_EGG);
		ItemMeta tokenItem4Meta = tokenItem4.getItemMeta();
		tokenItem4Meta.setDisplayName(ChatColor.AQUA.toString()  + "Disguises");
		//
		lore.clear();
		lore.add("");
		lore.add(ChatColor.GRAY.toString() + "Disguise yourself as a");
		lore.add(ChatColor.GRAY.toString() + "animal, mob or player!");
		lore.add("");
		lore.add(ChatColor.YELLOW.toString() + "Applys to all servers and lasts forever!");
		lore.add("");
		lore.add(ChatColor.GRAY + "Access with " + ChatColor.AQUA + "/disguises");

		tokenItem4Meta.setLore(lore);
		tokenItem4Meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		tokenItem4.setItemMeta(tokenItem4Meta);

		inv.setItem(25, tokenItem4);
		
		p.openInventory(inv);
		
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
