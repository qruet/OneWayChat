package com.squeaky.onewaychat.world;

import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class ChainManager {

    private static final LinkedList<WorldChain> CHAIN_LIST = new LinkedList<>();

    public static boolean registerChain(WorldChain chain) {
        if (getWorldChain(chain.getOrigin()) != null)
            return false;
        return CHAIN_LIST.add(chain);
    }

    public static WorldChain getWorldChain(World key) {
        return CHAIN_LIST.stream().filter(w -> w.getOrigin().getName().equals(key.getName())).findAny().orElse(null);
    }

    public static Collection<WorldChain> getLinkedWorldChain(World link) {
        LinkedList<WorldChain> worldChains = new LinkedList<>();
        CHAIN_LIST.forEach(cL -> {
            if (cL.hasLink(link))
                worldChains.add(cL);
        });
        return Collections.unmodifiableCollection(worldChains);
    }

}
