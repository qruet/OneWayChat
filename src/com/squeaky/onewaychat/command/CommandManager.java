package com.squeaky.onewaychat.command;

import com.squeaky.onewaychat.utils.chat.T;
import com.squeaky.onewaychat.world.ChainManager;
import com.squeaky.onewaychat.world.WorldChain;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CommandManager implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(T.C("&eMissing arguments, try &6/perchat link [world1] [world2] ..."));
            return true;
        }

        boolean flag = sender instanceof Player && ((Player) sender).hasPermission("onewaychat.admin");

        if (args[0].equalsIgnoreCase("link") && args.length == 3) {
            if (!flag && sender instanceof Player && !((Player) sender).hasPermission("onewaychat.link")) {
                sender.sendMessage(T.C("&cYou do not have permission to do this."));
                return true;
            }
            List<String> arguments = Arrays.asList(args).subList(1, args.length);
            LinkedList<World> worlds = new LinkedList<>();
            for (String arg : arguments) {
                World world = Bukkit.getWorld(arg);
                if (world != null)
                    worlds.add(world);
                else {
                    sender.sendMessage(T.C("&c" + arg + " is not a valid world name, try again."));
                    return true;
                }
            }

            if (worlds.size() != arguments.size())
                return true;

            List<World> linked_worlds = worlds.subList(1, worlds.size());

            WorldChain chain = new WorldChain(worlds.get(0), linked_worlds.toArray(new World[0]));
            if (ChainManager.registerChain(chain)) {
                sender.sendMessage(T.C("&aSuccessfully linked world chats!"));
            } else {
                chain = ChainManager.getWorldChain(worlds.get(0));
                boolean flag1 = false;
                for (World world : linked_worlds) {
                    if (!chain.hasLink(world)) {
                        chain.link(world);
                        flag1 = true;
                    }
                }
                if (flag1) {
                    sender.sendMessage(T.C("&aUpdated linked worlds for " + worlds.get(0).getName() + "!"));
                } else {
                    sender.sendMessage(T.C("&eThat chain configuration has already been set."));
                }
            }

            return true;
        }

        if (args[0].equalsIgnoreCase("unlink") && args.length == 3) {
            if (!flag && sender instanceof Player && !((Player) sender).hasPermission("onewaychat.admin.unlink")) {
                sender.sendMessage(T.C("&cYou do not have permission to do this."));
                return true;
            }
            List<String> arguments = Arrays.asList(args).subList(1, args.length);
            LinkedList<World> worlds = new LinkedList<>();
            for (String arg : arguments) {
                World world = Bukkit.getWorld(arg);
                if (world != null)
                    worlds.add(world);
            }

            if (worlds.size() < 2) {
                sender.sendMessage(T.C("&eNot enough valid world names were provided. Try /perchat unlink [world1] [world2] ..."));
                return true;
            }

            WorldChain chain = ChainManager.getWorldChain(worlds.get(0));
            for (World world : worlds.subList(1, worlds.size())) {
                chain.unlink(world);
            }
            sender.sendMessage(T.C("&aSuccessfully unlinked worlds!"));
            return true;
        }
        sender.sendMessage(T.C("&eCommand not recognized, try &6/perchat link [world1] [world2] ..."));
        return true;
    }
}
