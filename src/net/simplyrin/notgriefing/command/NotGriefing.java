package net.simplyrin.notgriefing.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.simplyrin.notgriefing.Main;

/**
 * Created by SimplyRin on 2018/03/25
 */
public class NotGriefing implements CommandExecutor {

	private Main plugin;

	public NotGriefing(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!sender.hasPermission("not_griefing.command")) {
			sender.sendMessage(this.plugin.getPrefix() + "§cYou do not have access to this command");
			sender.sendMessage(this.plugin.getPrefix() + "§cPlease contact to SimplyRin!");
			return true;
		}

		if(args.length > 0) {
			if(args[0].equalsIgnoreCase("reload")) {
				this.plugin.reloadConfig();
				sender.sendMessage(this.plugin.getPrefix() + "§bConfig ファイルをリロードしました。");
				return true;
			}
		}
		sender.sendMessage(this.plugin.getPrefix() + "§cUsage: /" + cmd.getName() + " <reload>");
		return true;
	}

}
