package com.toyz.MyTokens.BaseCommand.Handler;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class IssueCommands {
	private String[] args;
	private String commandLabel;
	private CommandSender sender;
	private Command cmd;

	public IssueCommands(CommandSender sender, String commandLabel,
			String[] args, Command cmd) {
		this.args = args;
		this.commandLabel = commandLabel;
		this.sender = sender;
		this.cmd = cmd;
	}

	public boolean isPlayer() {
		return this.sender instanceof Player;
	}

	public boolean isConsole() {
		return this.sender instanceof ConsoleCommandSender;
	}

	public CommandSender getSender() {
		return this.sender;
	}

	public Command getCMD() {
		return this.cmd;
	}

	public Player getPlayer() {
		return (Player) this.sender;
	}

	public ConsoleCommandSender getConsole() {
		return (ConsoleCommandSender) this.sender;
	}

	public String[] getArgs() {
		return this.args;
	}

	public int getLength() {
		return this.args.length;
	}

	public boolean argExist(int arg) {
		return arg < this.args.length;
	}

	public String getArg(int arg) {
		return this.args[arg];
	}

	public boolean isAlphanumeric(int arg) {
		return StringUtils.isAlphanumeric(this.args[arg]);
	}

	public boolean isAlpha(int arg) {
		return StringUtils.isAlpha(this.args[arg]);
	}

	public boolean isNumeric(int arg) {
		return StringUtils.isNumeric(this.args[arg]);
	}

	public boolean isCommand(String name) {
		return this.commandLabel.equalsIgnoreCase(name);
	}

	public String getCommand() {
		return this.commandLabel;
	}
}
