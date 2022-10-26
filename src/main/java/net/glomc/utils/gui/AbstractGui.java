package net.glomc.utils.gui;

import com.google.common.primitives.Ints;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractGui implements Listener {

    private static final LegacyComponentSerializer serializer = LegacyComponentSerializer.legacySection();
    protected Inventory inventory;
    protected final Plugin plugin;
    private boolean unregisterListenerOnClose = false;

    private boolean enableDragging = false;

    protected AbstractGui(Component name, int rows, Plugin plugin) {
        this.inventory = Bukkit.createInventory(null, Ints.constrainToRange(rows, 1, 6) * 9, name);
        this.plugin = plugin;
    }

    protected abstract void render();

    @Deprecated
    protected void fillGUI(Material material) {
        fillGUI(material, serializer.deserialize("-"), (Integer) null);
    }

    protected void fillGUI(Material material, Component itemName, Integer... excluded) {
        if (excluded == null) {
            excluded = new Integer[]{};
        }
        List<Integer> excludedSlots = Arrays.asList(excluded);
        ItemStack itemStack = new ItemStack(material, 1);
        if (itemStack.hasItemMeta()) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.displayName(itemName);
            itemStack.setItemMeta(itemMeta);
            itemStack.setAmount(1);
        }
        // needed to subtract by 1 since it starts at 0 than 1.
        for (int i = 0; i < (this.inventory.getSize() - 1); i++) {
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
            if (unregisterListenerOnClose) {
                unregisterListener();
            }

        }
    }

    public void unregisterListenerOnClose(boolean unregisterListenerOnClose) {
        this.unregisterListenerOnClose = unregisterListenerOnClose;
    }

    public void registerListener() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void unregisterListener() {
        HandlerList.unregisterAll(this);
    }

    public abstract void onClick(InventoryClickEvent event);

    protected void insertItem(int slot, ItemBuilder itemBuilder) {
        insertItem(slot, itemBuilder.build());
    }

    protected void insertItem(int slot, ItemStack itemStack) {
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
