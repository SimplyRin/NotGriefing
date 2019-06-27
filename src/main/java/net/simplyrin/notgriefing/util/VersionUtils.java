package net.simplyrin.notgriefing.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class VersionUtils {

	public VersionUtils() {
		getServerVersion();
	}

	private int serverVersion = -1;

	private List<Material> waterBuckets = null;

	public List<Material> getWaterBuckets() {

		if (waterBuckets == null) {

			waterBuckets = new ArrayList<>();
			if (getServerVersion() <= 12) {
				waterBuckets.add(Material.WATER_BUCKET);
			} else {
				waterBuckets.addAll(Arrays.asList(Material.COD_BUCKET, Material.PUFFERFISH_BUCKET,
						Material.SALMON_BUCKET, Material.TROPICAL_FISH_BUCKET, Material.WATER_BUCKET));
			}
		}

		return waterBuckets;
	}

	public ItemStack getItemInMainHand(Player p) {
		if (getServerVersion() <= 8) {

			@SuppressWarnings("deprecation")
			ItemStack main = p.getItemInHand();
			return main;
		} else {
			return p.getInventory().getItemInMainHand();
		}
	}

	@SuppressWarnings("deprecation")
	public void setItemInMainHand(Player p, ItemStack item) {
		if (getServerVersion() <= 8) {
			p.setItemInHand(item);
		} else {
			p.getInventory().setItemInMainHand(item);
		}
	}

	public boolean containsAnyPotionEffectType(ItemStack potion, PotionEffectType... types) {

		if (types.length <= 0) {
			return false;
		}

		PotionMeta meta = (PotionMeta) potion.getItemMeta();
		List<PotionEffect> effects = meta.getCustomEffects();

		List<PotionEffectType> potionTypes = effects.stream()
				.map(PotionEffect::getType)
				.collect(Collectors.toList());

		if (getServerVersion() >= 9) {
			PotionType baseType = meta.getBasePotionData().getType();
			potionTypes.add(baseType.getEffectType());
		}

		for (PotionEffectType targetType : types) {
			if (potionTypes.contains(targetType)) {
				return true;
			}
		}

		return false;
	}

	public int getServerVersion() {
		if (serverVersion < 0) {
			setServerVersion();
		}

		return serverVersion;
	}

	private void setServerVersion() {
		String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";

		String verTemp = version.substring(3);
		serverVersion = Integer.parseInt(verTemp.substring(0, verTemp.indexOf("_")));
	}
}
