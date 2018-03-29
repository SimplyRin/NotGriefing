package net.simplyrin.notgriefing;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;

import lombok.Getter;
import net.simplyrin.notgriefing.command.NotGriefing;

/**
 * Created by SimplyRin on 2018/03/24
 */
public class Main extends JavaPlugin implements Listener {

	private static Main plugin;
	@Getter
	private String prefix = "§7[§cNotGriefing§7] §r";

	@Override
	public void onEnable() {
		plugin = this;
		plugin.getServer().getConsoleSender().sendMessage(plugin.getPrefix() + "§bSite: https://twitter.com/Sui_pw");
		plugin.getServer().getConsoleSender().sendMessage(plugin.getPrefix() + "§bAuthor: Sui, SimplyRin");
		if(!plugin.getDescription().getAuthors().contains("SimplyRin")) {
			plugin.getServer().getPluginManager().disablePlugin(plugin);
			return;
		}

		plugin.saveDefaultConfig();
		plugin.prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Prefix"));

		plugin.getServer().getPluginManager().registerEvents(plugin, plugin);
		plugin.getCommand("notgriefing").setExecutor(new NotGriefing(plugin));
	}

	@Override
	public void onDisable() {
		plugin = null;
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();

		if(!player.hasPermission("not_griefing.setbreak")) {
			if(!plugin.getConfig().getBoolean("Block.Place")) {
				return;
			}

			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();

		if(!player.hasPermission("not_griefing.blockbreak")) {
			if(!plugin.getConfig().getBoolean("Block.Break")) {
				return;
			}

			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		Player sender = event.getPlayer();

		if(!plugin.getConfig().getBoolean("ShowProcessCommands.Toggle")) {
			return;
		}

		for(Player player : plugin.getServer().getOnlinePlayers()) {
			if(player.hasPermission("not_griefing.showcmd")) {
				String style = plugin.getConfig().getString("ShowProcessCommands.Style");

				style = style.replace("%prefix", this.getPrefix());
				style = style.replace("%player", sender.getName());
				style = style.replace("%command", event.getMessage());
				style = ChatColor.translateAlternateColorCodes('&', style);

				player.sendMessage(style);
			}
		}
	}

	@EventHandler
	public void onBlockDispense(BlockDispenseEvent event) {
		ItemStack itemStack = event.getItem();

		if(itemStack.getType().equals(Material.WATER_BUCKET)) {
			if(!plugin.getConfig().getBoolean("BlockDispense.Water")) {
				return;
			}

			event.setCancelled(true);
		}

		if(itemStack.getType().equals(Material.LAVA_BUCKET)) {
			if(!plugin.getConfig().getBoolean("BlockDispense.Lava")) {
				return;
			}

			event.setCancelled(true);
		}

		if(itemStack.getType().equals(Material.POTION)) {
			Potion potion = Potion.fromItemStack(itemStack);

			if(!potion.getType().getEffectType().equals(PotionEffectType.INVISIBILITY)) {
				return;
			}

			if(!plugin.getConfig().getBoolean("BlockDispense.Invisibility")) {
				return;
			}

			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Action action = event.getAction();
		Player player = event.getPlayer();
		ItemStack itemStack = player.getItemInHand();

		if(itemStack == null) {
			return;
		}

		if(action.equals(Action.RIGHT_CLICK_BLOCK)) {
			if(itemStack.getType().equals(Material.WATER_BUCKET)) {
				if(player.hasPermission("not_griefing.water")) {
					return;
				}

				event.setCancelled(true);
				if(plugin.getConfig().getString("Replace.Type").equals("REPLACE")) {
					plugin.replaceItem(player);
				}
			}
			if(itemStack.getType().equals(Material.LAVA_BUCKET)) {
				if(player.hasPermission("not_griefing.lava")) {
					return;
				}

				event.setCancelled(true);
				if(plugin.getConfig().getString("Replace.Type").equals("REPLACE")) {
					plugin.replaceItem(player);
				}
			}
		}

		if(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
			if(itemStack.getType().equals(Material.FLINT_AND_STEEL)) {
				if(player.hasPermission("not_griefing.flint_and_steel")) {
					return;
				}

				event.setCancelled(true);
				if(plugin.getConfig().getString("Replace.Type").equals("REPLACE")) {
					plugin.replaceItem(player);
				}
			}

			if(itemStack.getType().equals(Material.POTION)) {
				if(player.hasPermission("not_griefing.invpot")) {
					return;
				}

				Potion potion = Potion.fromItemStack(itemStack);

				if(potion.getType().getEffectType().equals(PotionEffectType.INVISIBILITY)) {
					event.setCancelled(true);
					if(plugin.getConfig().getString("Replace.Type").equals("REPLACE")) {
						plugin.replaceItem(player);
					}
				}
			}
		}
	}

	public void replaceItem(Player player) {
		String replaceItem = plugin.getConfig().getString("Replace.Item");
		ItemStack itemStack = new ItemStack(Material.matchMaterial(replaceItem));
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Replace.Name")));
		itemStack.setItemMeta(itemMeta);
		player.getPlayer().setItemInHand(itemStack);
		player.updateInventory();
	}

}
