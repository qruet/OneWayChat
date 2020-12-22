package com.squeaky.onewaychat.world;

import org.bukkit.World;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class WorldChain {

    private final World origin;
    private final LinkedList<World> linked_worlds;

    public WorldChain(World origin, World... linked_worlds) {
        this.origin = origin;
        this.linked_worlds = new LinkedList<>(Arrays.asList(linked_worlds));
    }

    public World getOrigin() {
        return origin;
    }

    public Collection<World> getLinkedWorlds() {
        return Collections.unmodifiableCollection(linked_worlds);
    }

    public void unlink(World world) {
        linked_worlds.remove(world);
    }

    public void link(World world) {
        linked_worlds.add(world);
    }

    public boolean hasLink(World world) {
        for(World w : linked_worlds) {
            if(w.getName().equals(world.getName()))
                return true;
        }
        return false;
    }

}
