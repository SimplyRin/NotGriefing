package net.simplyrin.notgriefing.listeners;

import org.bukkit.Bukkit;
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
import org.bukkit.potion.PotionEffectType;

import lombok.RequiredArgsConstructor;
import net.simplyrin.notgriefing.NotGriefing;

@RequiredArgsConstructor
public class BlockListener implements Listener {

	private final NotGriefing plugin;

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();

		if (!player.hasPermission("not_griefing.setbreak")) {
			if (!plugin.getSettings().isLimitPlace()) {
				return;
			}

			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();

		if (!player.hasPermission("not_griefing.blockbreak")) {
			if (!plugin.getSettings().isLimitBreak()) {
				return;
			}

			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		Player sender = event.getPlayer();

		if (!plugin.getSettings().isDisplayCommandOP()) {
			return;
		}

		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.hasPermission("not_griefing.showcmd")) {
				String style = plugin.getSettings().getCmdDisplayStyle();

				style = style.replace("%prefix", plugin.getSettings().getPrefix());
				style = style.replace("%player", sender.getName());
				style = style.replace("%command", event.getMessage());
				style = ChatColor.translateAlternateColorCodes('&', style);

				player.sendMessage(style);
			}
		}
	}

	@EventHandler
	public void onBlockDispense(BlockDispenseEvent event) {
		ItemStack item = event.getItem();

		if (plugin.getVersionUtils().getWaterBuckets().contains(item.getType())) {
			if (!plugin.getSettings().isLimitDispenseWater()) {
				return;
			}

			event.setCancelled(true);
			return;
		}

		if (item.getType().equals(Material.LAVA_BUCKET)) {
			if (!plugin.getSettings().isLimitDispenseLava()) {
				return;
			}

			event.setCancelled(true);
			return;
		}

		if (item.getType().toString().endsWith("POTION")) {

			if (!plugin.getVersionUtils().containsAnyPotionEffectType(item, PotionEffectType.INVISIBILITY)) {
				return;
			}

			if (!plugin.getSettings().isLimitInvisiblePotion()) {
				return;
			}

			event.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Action action = event.getAction();
		Player player = event.getPlayer();
		ItemStack item = plugin.getVersionUtils().getItemInMainHand(player);

		if (item == null) {
			return;
		}

		if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
			if (plugin.getVersionUtils().getWaterBuckets().contains(item.getType())) {
				if (player.hasPermission("not_griefing.water")) {
					return;
				}

				event.setCancelled(true);
				if (plugin.getSettings().getReplaceType().equals("REPLACE")) {
					replaceItem(player);
				}
			}
			if (item.getType().equals(Material.LAVA_BUCKET)) {
				if (player.hasPermission("not_griefing.lava")) {
					return;
				}

				event.setCancelled(true);
				if (plugin.getSettings().getReplaceType().equals("REPLACE")) {
					replaceItem(player);
				}
			}
		}

		if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
			if (item.getType().equals(Material.FLINT_AND_STEEL)) {
				if (player.hasPermission("not_griefing.flint_and_steel")) {
					return;
				}

				event.setCancelled(true);
				if (plugin.getSettings().getReplaceType().equals("REPLACE")) {
					replaceItem(player);
				}
			}

			if (item.getType().toString().endsWith("POTION")) {
				if (player.hasPermission("not_griefing.invpot")) {
					return;
				}

				if (!plugin.getVersionUtils().containsAnyPotionEffectType(item, PotionEffectType.INVISIBILITY)) {
					return;
				}

				event.setCancelled(true);

				if (plugin.getSettings().getReplaceType().equals("REPLACE")) {
					replaceItem(player);
				}
			}
		}
	}

	public void replaceItem(Player player) {
		ItemStack itemStack = new ItemStack(plugin.getSettings().getConvertMaterial());
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(
				ChatColor.translateAlternateColorCodes('&', plugin.getSettings().getConvertMaterialMessage()));
		itemStack.setItemMeta(itemMeta);
		plugin.getVersionUtils().setItemInMainHand(player, itemStack);
		player.updateInventory();
	}
}
