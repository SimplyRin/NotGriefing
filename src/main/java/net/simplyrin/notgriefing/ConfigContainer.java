package net.simplyrin.notgriefing;

import java.io.File;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import lombok.Getter;
import net.simplyrin.notgriefing.utils.Chat;

@Getter
public class ConfigContainer {

	private String prefix;
	private boolean limitBreak, limitPlace;
	private boolean limitDispenseWater, limitDispenseLava, limitInvisiblePotion;
	private String replaceType;
	private Material convertMaterial;
	private String convertMaterialMessage;
	private boolean displayCommandOP;
	private String cmdDisplayStyle;

	public ConfigContainer(NotGriefing plugin) {
		FileConfiguration conf = plugin.getConfig();

		prefix = Chat.f(conf.getString("Prefix", "&7[&cNotGriefing&7] &r"));

		limitPlace = conf.getBoolean("Block.Place", true);
		limitBreak = conf.getBoolean("Block.Break", true);

		limitDispenseWater = conf.getBoolean("BlockDispense.Water", true);
		limitDispenseLava = conf.getBoolean("BlockDispense.Lava", true);
		limitInvisiblePotion = conf.getBoolean("BlockDispense.Invisibility", true);

		replaceType = conf.getString("Replace.Type", "CANCEL");

		String materialStr = conf.getString("Replace.Item", null);
		try {
			convertMaterial = Material.valueOf(materialStr.toUpperCase());
		} catch (Exception e) {
			plugin.getLogger().warning("Failed to load value in config \"Replace.Item\"");
			convertMaterial = null;
		}

		convertMaterialMessage = Chat.f(conf.getString("Replace.Name", ""));
		if (convertMaterialMessage.equals("")) {
			convertMaterialMessage = null;
		}

		displayCommandOP = conf.getBoolean("ShowProcessCommands.Toggle", true);
		cmdDisplayStyle = conf.getString("ShowProcessCommands.Style", "%prefix&7%player: %command");
	}

	public void reloadConfig(NotGriefing plugin) {

		YamlConfiguration conf = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));

		prefix = Chat.f(conf.getString("Prefix", "&7[&cNotGriefing&7] &r"));

		limitPlace = conf.getBoolean("Block.Place", true);
		limitBreak = conf.getBoolean("Block.Break", true);

		limitDispenseWater = conf.getBoolean("BlockDispense.Water", true);
		limitDispenseLava = conf.getBoolean("BlockDispense.Lava", true);
		limitInvisiblePotion = conf.getBoolean("BlockDispense.Invisibility", true);

		replaceType = conf.getString("Replace.Type", "CANCEL");

		String materialStr = conf.getString("Replace.Item", null);
		try {
			convertMaterial = Material.valueOf(materialStr.toUpperCase());
		} catch (Exception e) {
			plugin.getLogger().warning("Failed to load value in config \"Replace.Item\"");
			convertMaterial = null;
		}

		convertMaterialMessage = Chat.f(conf.getString("Replace.Name", ""));
		if (convertMaterialMessage.equals("")) {
			convertMaterialMessage = null;
		}

		displayCommandOP = conf.getBoolean("ShowProcessCommands.Toggle", true);
		cmdDisplayStyle = conf.getString("ShowProcessCommands.Style", "%prefix&7%player: %command");
	}
}
