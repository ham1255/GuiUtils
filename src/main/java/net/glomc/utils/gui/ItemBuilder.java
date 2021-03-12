package net.glomc.utils.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class ItemBuilder {

    private Material material;
    private String name;
    private final List<String> lore;
    private int amount;
    private boolean enchanted;


    public ItemBuilder() {
        material = Material.STONE;
        name = "STONE";
        lore = new ArrayList<>();
        amount = 1;
    }

    public ItemBuilder setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public ItemBuilder setName(String name) {
        this.name = ChatColor.translateAlternateColorCodes('&', name);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        this.lore.clear();
        for (String s : lore) {
            this.lore.add(translateAlternateColorCodes('&', s));
        }
        return this;
    }

    public ItemBuilder setLore(ArrayList<String> lore) {
        this.lore.clear();
        lore.forEach((line) -> this.lore.add(ChatColor.translateAlternateColorCodes('&', line)));
        return this;
    }

    public ItemBuilder setEnchanted(boolean enchanted) {
        this.enchanted = enchanted;
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemStack build() {
        ItemStack itemStack = new ItemStack(this.material);
        itemStack.setAmount(this.amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(this.lore);
        itemMeta.setDisplayName(name);
        if (enchanted) {
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addEnchant(Enchantment.FIRE_ASPECT, 1, false);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
