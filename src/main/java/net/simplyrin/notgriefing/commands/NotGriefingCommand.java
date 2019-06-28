package net.simplyrin.notgriefing.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import lombok.RequiredArgsConstructor;
import net.simplyrin.notgriefing.NotGriefing;
import net.simplyrin.notgriefing.utils.Chat;

/**
 * Created by SimplyRin on 2018/03/25
 */
@RequiredArgsConstructor
public class NotGriefingCommand implements CommandExecutor {

	private final NotGriefing plugin;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		String prefix = plugin.getSettings().getPrefix();

		if (!sender.hasPermission("not_griefing.command")) {
			sender.sendMessage(Chat.f("{0}&cYou don't have permission!", prefix));
			//			sender.sendMessage(plugin.getSettings().getPrefix() + "§cPlease contact to SimplyRin!");
			return true;
		}

		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("reload")) {
				plugin.getSettings().reloadConfig(plugin);
				sender.sendMessage(Chat.f("{0}&bConfig ファイルをロードしました。", prefix));
				return true;
			}
		}
		sender.sendMessage(Chat.f("{0}&cUsage: /{1} <reload>", prefix, label));
		return true;
	}

}
