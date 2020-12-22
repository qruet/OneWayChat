package com.squeaky.onewaychat.handler;

import com.squeaky.onewaychat.world.ChainManager;
import com.squeaky.onewaychat.world.WorldChain;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ChatEventListener implements Listener {

    @EventHandler
    public void onChatEvent(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        World pWorld = player.getWorld();

        // ensure all recipients by default are players only in the same world as sender
        Set<Player> recipients = e.getRecipients();
        recipients.clear();

        recipients.addAll(pWorld.getPlayers());

        Collection<WorldChain> recipient_worlds = ChainManager.getLinkedWorldChain(pWorld);

        for(WorldChain chain : recipient_worlds) {
            recipients.addAll(chain.getOrigin().getPlayers());
        }
    }

}
