package net.glomc.utils.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventoryDoubleChest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;


import java.util.Arrays;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public abstract class AbstractGui implements CommandExecutor, Listener {
    protected Inventory inventory;
    private final int rows;
    protected String name;
    protected final Plugin plugin;
    protected boolean unRegisterListenerWhenClosed;

    public enum Rows {
        ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6);
        int rows;

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
        inventory = Bukkit.createInventory(null, this.rows, this.name);
        this.unRegisterListenerWhenClosed = unRegisterListenerWhenClosed;
        this.plugin = plugin;
    }


    private void fillGUI(Material material) {
        fillGUI(material, null);
    }


    protected void fillGUI(Material material, Integer... excluded) {
        int slot = -1;
        ItemStack item = new ItemStack(material, 1);
        ItemMeta item_meta = item.getItemMeta();
        assert item_meta != null;
        item_meta.setDisplayName(translateAlternateColorCodes('&', "&1."));
        item.setItemMeta(item_meta);
        item.setAmount(1);
        try {
            while (slot <= (this.rows - 2)) {
                slot++;
                int finalSlot = slot;
                if (excluded != null && Arrays.stream(excluded).noneMatch(i -> i == finalSlot)) {
                    this.inventory.setItem(slot, item);
                }
            }
        } catch (Exception ignored) {

        }


    }


    @Deprecated
    protected void enchantItem(int slot) {
        ItemStack itemStack = inventory.getItem(slot);
        if (itemStack == null) {
            return;
        }
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return;
        }
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addEnchant(Enchantment.FIRE_ASPECT, 1, false);
        itemStack.setItemMeta(meta);
        inventory.setItem(slot, itemStack);
    }

    @Deprecated
    protected void removeEnchantFromItem(int slot) {
        ItemStack itemStack = inventory.getItem(slot);
        if (itemStack == null) {
            return;
        }
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return;
        }
        meta.removeEnchant(Enchantment.FIRE_ASPECT);
        itemStack.setItemMeta(meta);
        inventory.setItem(slot, itemStack);
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (isEqual(event.getView())) {
            event.setCancelled(true);
            onClick(event);
        }

    }

    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent event) {
        if (isEqual(event.getView())) {
            if (unRegisterListenerWhenClosed) {
                InventoryClickEvent.getHandlerList().unregister(this);
                InventoryCloseEvent.getHandlerList().unregister(this);
            }

        }
    }


    private boolean isEqual(InventoryView view) {
        return view.getTopInventory().equals(this.inventory) || view.getTitle().equals(ChatColor.translateAlternateColorCodes('&', name));
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

}
