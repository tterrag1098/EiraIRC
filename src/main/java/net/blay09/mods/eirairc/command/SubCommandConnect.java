package net.blay09.mods.eirairc.command;

import java.util.List;

import net.blay09.mods.eirairc.EiraIRC;
import net.blay09.mods.eirairc.config.ServerConfig;
import net.blay09.mods.eirairc.handler.ConfigurationHandler;
import net.blay09.mods.eirairc.irc.IRCTarget;
import net.blay09.mods.eirairc.util.Globals;
import net.blay09.mods.eirairc.util.Utils;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

public class SubCommandConnect implements ISubCommand {

	@Override
	public String getCommandName() {
		return "connect";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return Globals.MOD_ID + ":irc.commands.connect";
	}

	@Override
	public List getCommandAliases() {
		return null;
	}

	@Override
	public boolean processCommand(ICommandSender sender, IRCTarget context, String[] args, boolean serverSide) {
		if(args.length <= 1) {
			throw new WrongUsageException(getCommandUsage(sender));
		}
		String host = args[1];
		if(EiraIRC.instance.isConnectedTo(host)) {
			Utils.sendLocalizedMessage(sender, "irc.general.alreadyConnected", host);
			return true;
		}
		Utils.sendLocalizedMessage(sender, "irc.basic.connecting", host);
		ServerConfig serverConfig = ConfigurationHandler.getServerConfig(host);
		if(args.length > 2) {
			serverConfig.setServerPassword(args[2]);
		}
		if(Utils.connectTo(serverConfig) != null) {
			ConfigurationHandler.addServerConfig(serverConfig);
			ConfigurationHandler.save();
		} else {
			Utils.sendLocalizedMessage(sender, "irc.connect.error", host);
		}
		return true;
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return Utils.isOP(sender);
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int idx) {
		return false;
	}

}