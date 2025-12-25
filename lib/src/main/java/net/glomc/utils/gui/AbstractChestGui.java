package net.glomc.utils.gui;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import net.glomc.utils.gui.components.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractChestGui implements InventoryHolder {

  private final Inventory inventory;
  protected final Plugin plugin;
  private final Map<Integer, GuiItem> items = new HashMap<>();
  private final int inventoryRows;
  private final int inventorySize;

  protected AbstractChestGui(Component name, int rows, Plugin plugin) {
    this.inventorySize = Ints.constrainToRange(rows, 1, 6) * 9;
    this.inventoryRows = rows - 1; // minecraft starts at 0
    this.inventory = Bukkit.createInventory(this, inventorySize, name);
    this.plugin = plugin;
  }

  private void render() {
    inventory.clear();
    items.forEach((slot, item) -> inventory.setItem(slot, item.getItemStack()));
  }

  protected abstract void prepareRender();

  public void updateGui() {
    prepareRender();
    render();
  }

  private int getSlotFromRowCol(final int row, final int col) {
    Preconditions.checkArgument(row >= 0 && row <= inventoryRows,
      "Row must be between 0 and %s, but was %s", this.inventoryRows, row);
    Preconditions.checkArgument(col >= 0 && col <= 8,
      "Column must be between 0 and 8, but was %s", col);
    return row * 9 + col;
  }


  public void insertItem(int row, int col, GuiItem item) {
    insertItem(getSlotFromRowCol(row, col), item);
  }

  public void removeItem(int row, int col) {
    removeItem(getSlotFromRowCol(row, col));
  }

  public void insertItem(int slot, GuiItem item) {
    Preconditions.checkArgument(slot >= 0 && slot <= inventorySize,
      "Slot must be between 0 and %s, but was %s", inventorySize, slot);
    this.items.put(slot, item);
  }

  public void removeItem(int slot) {
    Preconditions.checkArgument(slot >= 0 && slot <= inventorySize,
      "Slot must be between 0 and %s, but was %s", inventorySize, slot);
    items.remove(slot);
  }

  public void clearItems() {
    items.clear();
  }

  public GuiItem getGuiItem(int slot) {
    Preconditions.checkArgument(slot >= 0 && slot <= inventorySize,
      "Slot must be between 0 and %s, but was %s", inventorySize, slot);
    return items.get(slot);
  }

  protected void executeItemClick(InventoryClickEvent event) {
    GuiItem item = getGuiItem(event.getSlot());
    if (item != null && item.getFunction() != null) item.getFunction().execute(item, event);
  }

  @Override
  public @NonNull Inventory getInventory() {
    return inventory;
  }

  public InventoryType getType() {
    return this.inventory.getType();
  }

  public void open(Player player) {
    // check if gui is auto updating/refreshiNG
    if (refreshTask == null || refreshTask.isCancelled()) updateGui();
    player.openInventory(this.inventory);
  }

  public void close(Player player) {
    player.getScheduler().runDelayed(plugin, task -> player.closeInventory(), null, 2);
  }

  public void close() {
    plugin.getServer().getGlobalRegionScheduler().execute(plugin, this.inventory::close);
  }


  // default events for working clickable menu
  public void onClick(InventoryClickEvent event) {
    event.setCancelled(true); // prevent any changes
    executeItemClick(event);
  }

  public void onOpen(InventoryOpenEvent event) {

  }

  public void onClose(InventoryCloseEvent event) {
    if (cancelRefreshOnClose) {
      cancelRefresh();
    }
  }

  public void onDrag(InventoryDragEvent event) {
    event.setCancelled(true);
  }

  // refreshable gui
  private ScheduledTask refreshTask;
  private boolean cancelRefreshOnClose = false;

  public void refreshGuiEvery(int ticks) {
    Preconditions.checkArgument(refreshTask == null, "refresh task already exists, CANCEL first");
    refreshTask = plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(plugin, scheduledTask -> {
      onRefresh();
      updateGui();
    }, 1, ticks);
  }

  public void cancelRefresh() {
    if (refreshTask != null) {
      refreshTask.cancel();
      refreshTask = null;
    }
  }

  @SuppressWarnings("all")
  protected void cancelRefreshOnClose(boolean cancel) {
    this.cancelRefreshOnClose = cancel;
  }

  protected void onRefresh() {

  }
}
