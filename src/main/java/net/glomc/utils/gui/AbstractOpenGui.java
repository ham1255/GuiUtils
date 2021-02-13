package net.glomc.utils.gui;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public abstract class AbstractOpenGui {

    public void openGui(Player player, AbstractGui gui, Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(gui, plugin);
        gui.setup();
        player.openInventory(gui.getInventory());
    }
}
