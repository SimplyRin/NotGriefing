package net.simplyrin.notgriefing;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import net.simplyrin.notgriefing.commands.NotGriefingCommand;
import net.simplyrin.notgriefing.listeners.BlockListener;
import net.simplyrin.notgriefing.utils.Chat;

/**
 * Created by SimplyRin on 2018/03/24
 *
 * Forked by siloneco on 2019/06/26
 */
public class NotGriefing extends JavaPlugin implements Listener {

	private static NotGriefing plugin;
	@Getter
	private String prefix = Chat.f("&7[&cNotGriefing&7] &r");

	@Override
	public void onEnable() {
		plugin = this;

		getLogger().info(Chat.f("{0}&bSite: https://twitter.com/Sui_pw", prefix));
		getLogger().info(Chat.f("{0}&bAuthor: Sui, SimplyRin", prefix));

		if (!plugin.getDescription().getAuthors().contains("SimplyRin")) {
			plugin.getServer().getPluginManager().disablePlugin(plugin);
			return;
		}

		saveDefaultConfig();
		prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Prefix"));

		getServer().getPluginManager().registerEvents(new BlockListener(this), this);
		Bukkit.getPluginCommand("notgriefing").setExecutor(new NotGriefingCommand(plugin));

		Bukkit.getLogger().info(getName() + " enabled.");
	}

	@Override
	public void onDisable() {
		Bukkit.getLogger().info(getName() + " disabled.");
	}
}
