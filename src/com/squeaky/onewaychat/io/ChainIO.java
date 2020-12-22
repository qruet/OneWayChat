package com.squeaky.onewaychat.io;

import com.squeaky.onewaychat.OneWayChat;
import com.squeaky.onewaychat.world.ChainManager;
import com.squeaky.onewaychat.world.WorldChain;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChainIO {

    private static final File configFile = new File(JavaPlugin.getPlugin(OneWayChat.class).getDataFolder(), "linked_worlds.yml");
    private static FileConfiguration config;

    public static void serialize() {
        if (config == null) {
            deserialize();
        }

        for (World world : Bukkit.getWorlds()) {
            WorldChain chain = ChainManager.getWorldChain(world);
            if (chain != null)
                serialize(chain);
        }
    }

    private static void serialize(WorldChain chain) {
        List<String> worlds = new ArrayList<>();
        chain.getLinkedWorlds().forEach(w -> {
            worlds.add(w.getName());
        });

        config.set(chain.getOrigin().getName(), worlds);
        try {
            config.save(configFile);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Please report the following to one of the developers.");
            e.printStackTrace();
        }
    }

    public static void deserialize() {
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch(IOException e) {
                Bukkit.getLogger().severe("Please report the following to one of the developers.");
                e.printStackTrace();
            }
            return;
        }
        config = YamlConfiguration.loadConfiguration(configFile);

        for (String key : config.getKeys(false)) {
            ChainManager.registerChain(deserialize(key));
        }
    }

    private static WorldChain deserialize(String world) {
        World origin = Bukkit.getWorld(world);

        Validate.isTrue(origin != null, world + " is not a valid world name!");
        Validate.isTrue(config.contains(world), world + " is not a registered key chain in config.yml!");

        List<String> worlds = config.getStringList(world);
        LinkedList<World> linked_worlds = new LinkedList<>();

        worlds.forEach(w -> {
            linked_worlds.add(Bukkit.getWorld(w));
        });

        return new WorldChain(Bukkit.getWorld(world), linked_worlds.toArray(new World[0]));
    }

}
