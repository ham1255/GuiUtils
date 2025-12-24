package net.glomc.utils.gui.components;

import org.bukkit.event.inventory.InventoryEvent;

import javax.annotation.Nullable;

@FunctionalInterface
public interface ItemFunction<E extends InventoryEvent> {

  // Function of an GuiItem
  void execute(@Nullable GuiItem item, final E event);

}
