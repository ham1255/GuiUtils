package net.glomc.utils.gui.components;

import com.google.common.base.Preconditions;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GuiItem {

  private final ItemFunction<InventoryClickEvent> function;
  private ItemStack itemStack;

  public GuiItem(ItemStack itemStack, ItemFunction<InventoryClickEvent> function) {
    Preconditions.checkNotNull(itemStack, "Item stack should not be null for gui");
    this.function = function;
    this.itemStack = itemStack;
  }

  public GuiItem(ItemStack itemStack) {
    this(itemStack,null);
  }

  public ItemFunction<InventoryClickEvent> getFunction() {
    return function;
  }

  public ItemStack getItemStack() {
    return itemStack;
  }

  public void setItemStack(ItemStack itemStack) {
    Preconditions.checkNotNull(itemStack, "Item stack should not be null for gui");
    this.itemStack = itemStack;
  }
}
