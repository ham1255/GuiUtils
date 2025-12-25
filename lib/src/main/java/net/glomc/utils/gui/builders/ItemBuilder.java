package net.glomc.utils.gui.builders;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;


public class ItemBuilder {

    private Material material;
    private Component displayName;
    private List<Component> lore;
    private int amount;
    private boolean enchanted;


    public ItemBuilder() {
        material = Material.STONE;
        displayName = Component.text("STONE");
        lore = new ArrayList<>();
        amount = 1;
    }

    public ItemBuilder setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public ItemBuilder setName(Component displayName) {
        this.displayName = displayName;
        return this;
    }

    public ItemBuilder setLore(Component... lore) {
        this.lore = List.of(lore);
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
        itemMeta.lore(this.lore);
        itemMeta.displayName(this.displayName);
        if (enchanted) {
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addEnchant(Enchantment.FIRE_ASPECT, 1, false);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemBuilder newItem() {
      return new ItemBuilder();
    }


}
