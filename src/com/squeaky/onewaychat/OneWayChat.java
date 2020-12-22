package com.squeaky.onewaychat;

import com.squeaky.onewaychat.command.CommandManager;
import com.squeaky.onewaychat.handler.ChatEventListener;
import com.squeaky.onewaychat.io.ChainIO;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class OneWayChat extends JavaPlugin {

    public void onEnable() {
        ChainIO.deserialize();
        Bukkit.getPluginCommand("perchat").setExecutor(new CommandManager());
        Bukkit.getPluginManager().registerEvents(new ChatEventListener(), this);
        getLogger().info("Enabled!");
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
        ChainIO.serialize();
        getLogger().info("Disabled!");
    }

}
