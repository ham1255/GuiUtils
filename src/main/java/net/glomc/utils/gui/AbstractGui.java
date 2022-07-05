package net.glomc.utils.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public abstract class AbstractGui implements CommandExecutor, Listener {
    protected Inventory inventory;
    private final int rows;
    protected String name;
    protected final Plugin plugin;
    protected boolean unRegisterListenerWhenClosed;

    private boolean enableDragging = false;

    public enum Rows {
        ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6);
        final int rows;

        Rows(int rows) {
            this.rows = rows * 9;
        }

        public int getRows() {
            return rows;
        }
    }

    public abstract void setup();


    protected AbstractGui(String name, Rows rows, Boolean unRegisterListenerWhenClosed, Plugin plugin) {
        this.rows = rows.getRows();
        this.name = translateAlternateColorCodes('&', name);
        this.inventory = Bukkit.createInventory(null, this.rows, this.name);
        this.unRegisterListenerWhenClosed = unRegisterListenerWhenClosed;
        this.plugin = plugin;
    }


    private void fillGUI(Material material) {
        fillGUI(material, null);
    }


    protected void fillGUI(Material material, Integer... excluded) {
        if (excluded == null) {
            excluded = new Integer[]{};
        }
        List<Integer> excludedSlots = Arrays.asList(excluded);
        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(translateAlternateColorCodes('&', "&1."));
        itemStack.setItemMeta(itemMeta);
        itemStack.setAmount(1);
        for (int i = 0; i < rows; i++) {
            if (excludedSlots.contains(i)) {
                continue;
            }
            inventory.setItem(i, itemStack);
        }


    }


    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (event.getClickedInventory() == this.inventory) {
            event.setCancelled(true);
            onClick(event);
        }

    }

    @EventHandler
    public void onInventoryDragEvent(InventoryDragEvent event) {
        if (event.getInventory() == this.inventory) {
            event.setCancelled(!this.enableDragging);
        }
    }

    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent event) {
        if (event.getInventory() == this.inventory) {
            if (unRegisterListenerWhenClosed) {
                InventoryClickEvent.getHandlerList().unregister(this);
                InventoryCloseEvent.getHandlerList().unregister(this);
                InventoryDragEvent.getHandlerList().unregister(this);
            }

        }
    }

    public abstract void onClick(InventoryClickEvent event);


    public void insertItem(int slot, ItemStack itemStack) {
        this.inventory.setItem(slot, itemStack);
    }


    public void removeItem(int slot) {
        this.inventory.clear(slot);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public InventoryType getType() {
        return this.inventory.getType();
    }

    public void openGui(Player player) {
        player.openInventory(this.inventory);
    }

    public void enableDragging(boolean enableDragging) {
        this.enableDragging = enableDragging;
    }
}
