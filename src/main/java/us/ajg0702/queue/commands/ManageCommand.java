package us.ajg0702.queue.commands;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import us.ajg0702.queue.Main;
import us.ajg0702.queue.Manager;
import us.ajg0702.queue.Server;
import us.ajg0702.queue.utils.BungeeMessages;

public class ManageCommand extends Command {
	
	Main pl;
	BungeeMessages msgs;

	public ManageCommand(Main pl) {
		super("ajqueue", null, "ajq");
		this.pl = pl;
		msgs = BungeeMessages.getInstance();
	}



	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("reload")) {
				if(!sender.hasPermission("ajqueue.reload")) {
					sender.sendMessage(msgs.getBC("noperm"));
					return;
				}
				msgs.reload();
				pl.getConfig().reload();
				pl.timeBetweenPlayers = pl.getConfig().getInt("wait-time");
				Manager.getInstance().reloadIntervals();
				Manager.getInstance().reloadServers();
				pl.checkConfig();
				sender.sendMessage(msgs.getBC("commands.reload"));
				return;
				
			}
			if(args[0].equalsIgnoreCase("list")) {
				int total = 0;
				for(Server server : Manager.getInstance().getServers()) {
					
					String msg = msgs.get("list.format").replaceAll("\\{SERVER\\}", server.getName());
					String playerlist = "";
					List<ProxiedPlayer> players = server.getQueue();
					if(msg.contains("{LIST}")) {
						for(ProxiedPlayer p : players) {
							playerlist += msgs.get("list.playerlist").replaceAll("\\{NAME\\}", p.getDisplayName());
						}
						if(playerlist.equalsIgnoreCase("")) {
							playerlist = msgs.get("list.none")+", ";
						}
						playerlist = playerlist.substring(0, playerlist.length()-2);
						msg = msg.replaceAll("\\{LIST\\}", playerlist);
					}
					total += players.size();
					msg = msg.replaceAll("\\{COUNT\\}", players.size()+"");
					sender.sendMessage(Main.formatMessage(msg));
				}
				sender.sendMessage(Main.formatMessage(msgs.get("list.total").replaceAll("\\{TOTAL\\}", total+"")));
				return;
			}
			if(args[0].equalsIgnoreCase("p")) {
				sender.sendMessage(Main.formatMessage(pl.isp()+""));
				return;
			}
			if(args[0].equalsIgnoreCase("player")) {
				sender.sendMessage(Main.formatMessage("/ajQueue <player> <server>"));
				return;
			}
			if(args[0].equalsIgnoreCase("version")) {
				sender.sendMessage(Main.formatMessage(pl.getDescription().getVersion()));
				return;
			}
		}
		if(args.length == 2) {
			
			if(!sender.hasPermission("ajqueue.send")) {
				sender.sendMessage(msgs.getBC("noperm"));
				return;
			}
			
			List<String> playerNames = getNameList();
			if(playerNames.contains(args[0].toLowerCase())) {
				
				ProxiedPlayer ply = pl.getProxy().getPlayer(args[0]);
				Manager.getInstance().addToQueue(ply, args[1]);
				sender.sendMessage(Main.formatMessage(
						msgs.get("send")
						.replaceAll("\\{PLAYER\\}", ply.getDisplayName())
						.replaceAll("\\{SERVER\\}", args[1]))
					);
				return;
			}
		}
		
		sender.sendMessage(Main.formatMessage("/ajqueue <reload|list|player>"));
	}
	
	private List<String> getNameList() {
		List<String> playerNames = new ArrayList<>();
		for(ProxiedPlayer ply : pl.getProxy().getPlayers()) {
			if(ply == null || !ply.isConnected()) continue;
			playerNames.add(ply.getName().toLowerCase());
		}
		return playerNames;
	}
}
