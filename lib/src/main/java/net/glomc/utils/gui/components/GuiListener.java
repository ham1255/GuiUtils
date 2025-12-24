package net.glomc.utils.gui.components;

import net.glomc.utils.gui.AbstractChestGui;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;

public class GuiListener implements Listener {

  @EventHandler
  public void onInventoryClick(final InventoryClickEvent event) {
    if (event.getInventory().getHolder() instanceof AbstractChestGui abstractChestGui && event.getClickedInventory() != null &&
      event.getClickedInventory().getHolder() != null && event.getClickedInventory().getHolder() == abstractChestGui) {
      abstractChestGui.onClick(event);
    }
  }

  @EventHandler
  public void onInventoryDrag(final InventoryDragEvent event) {
    if (event.getInventory().getHolder() instanceof AbstractChestGui abstractChestGui) {
       abstractChestGui.onDrag(event);
    }
  }

  @EventHandler
  public void onInventoryOpen(final InventoryOpenEvent event) {
    if (event.getInventory().getHolder() instanceof AbstractChestGui abstractChestGui) {
      abstractChestGui.onOpen(event);
    }
  }

  @EventHandler
  public void onInventoryClose(final InventoryCloseEvent event) {
    if (event.getInventory().getHolder() instanceof AbstractChestGui abstractChestGui) {
      abstractChestGui.onClose(event);
    }
  }
}
