package net.simplyrin.notgriefing;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import net.simplyrin.notgriefing.commands.NotGriefingCommand;
import net.simplyrin.notgriefing.listeners.BlockListener;
import net.simplyrin.notgriefing.util.VersionUtils;
import net.simplyrin.notgriefing.utils.Chat;

/**
 * Created by SimplyRin on 2018/03/24
 *
 * Forked by siloneco on 2019/06/26
 */
public class NotGriefing extends JavaPlugin implements Listener {

	private static NotGriefing plugin;
	@Getter
	private ConfigContainer settings = null;
	@Getter
	private VersionUtils versionUtils = null;

	@Override
	public void onEnable() {
		plugin = this;

		versionUtils = new VersionUtils();

		if (!plugin.getDescription().getAuthors().contains("SimplyRin")) {
			plugin.getServer().getPluginManager().disablePlugin(plugin);
			return;
		}

		saveDefaultConfig();
		settings = new ConfigContainer(this);

		getServer().getPluginManager().registerEvents(new BlockListener(this), this);
		Bukkit.getPluginCommand("notgriefing").setExecutor(new NotGriefingCommand(plugin));

		Bukkit.getConsoleSender().sendMessage(Chat.f("{0}Site: https://twitter.com/Sui_pw", settings.getPrefix()));
		Bukkit.getConsoleSender().sendMessage(Chat.f("{0}Author: Sui, SimplyRin", settings.getPrefix()));

		Bukkit.getLogger().info(getName() + " enabled.");
	}

	@Override
	public void onDisable() {
		Bukkit.getLogger().info(getName() + " disabled.");
	}
}
