package net.simplyrin.notgriefing.listeners;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import lombok.RequiredArgsConstructor;
import net.simplyrin.notgriefing.NotGriefing;

@RequiredArgsConstructor
public class BlockListener implements Listener {

	private final NotGriefing plugin;

	private List<Material> waterBuckets = Arrays.asList(Material.COD_BUCKET, Material.PUFFERFISH_BUCKET,
			Material.SALMON_BUCKET, Material.TROPICAL_FISH_BUCKET, Material.WATER_BUCKET);

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

		if (waterBuckets.contains(item.getType())) {
			if (!plugin.getSettings().isLimitDispenseWater()) {
				return;
			}

			event.setCancelled(true);
			return;
		}

		if (itemStack.getType().equals(Material.LAVA_BUCKET)) {
			if (!plugin.getSettings().isLimitDispenseLava()) {
				return;
			}

			event.setCancelled(true);
		}

		if (item.getType().toString().endsWith("POTION")) {

			if (!containsAnyPotionEffectType(item, PotionEffectType.INVISIBILITY)) {
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
		@SuppressWarnings("deprecation")
		ItemStack itemStack = player.getItemInHand();

		if (itemStack == null) {
			return;
		}

		if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
			if (waterBuckets.contains(item.getType())) {
				if (player.hasPermission("not_griefing.water")) {
					return;
				}

				event.setCancelled(true);
				if (plugin.getSettings().getReplaceType().equals("REPLACE")) {
					replaceItem(player);
				}
			}
			if (itemStack.getType().equals(Material.LAVA_BUCKET)) {
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
			if (itemStack.getType().equals(Material.FLINT_AND_STEEL)) {
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

				if (!containsAnyPotionEffectType(item, PotionEffectType.INVISIBILITY)) {
					return;
				}

				event.setCancelled(true);

				if (plugin.getSettings().getReplaceType().equals("REPLACE")) {
					replaceItem(player);
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void replaceItem(Player player) {
		ItemStack itemStack = new ItemStack(plugin.getSettings().getConvertMaterial());
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(
				ChatColor.translateAlternateColorCodes('&', plugin.getSettings().getConvertMaterialMessage()));
		itemStack.setItemMeta(itemMeta);
		player.getInventory().setItemInHand(itemStack);
		player.updateInventory();
	}

	private boolean containsAnyPotionEffectType(ItemStack potion, PotionEffectType... types) {

		if (types.length <= 0) {
			return false;
		}

		PotionMeta meta = (PotionMeta) potion.getItemMeta();
		List<PotionEffect> effects = meta.getCustomEffects();

		List<PotionEffectType> potionTypes = effects.stream()
				.map(PotionEffect::getType)
				.collect(Collectors.toList());

		PotionType baseType = meta.getBasePotionData().getType();
		potionTypes.add(baseType.getEffectType());

		for (PotionEffectType targetType : types) {
			if (potionTypes.contains(targetType)) {
				return true;
			}
		}

		return false;
	}
}
