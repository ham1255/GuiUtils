package net.glomc.utils.gui;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import net.glomc.utils.gui.builders.ItemBuilder;
import net.glomc.utils.gui.builders.ItemsConstructor;
import net.glomc.utils.gui.components.GuiItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.util.LinkedList;
import java.util.List;

public abstract class PaginatedChestGui extends AbstractChestGui {

  private final LinkedList<GuiItem> content = new LinkedList<>();

  private final int pageSize;
  private int pageNumber = 1;
  private final int toolBoxRow;

  protected PaginatedChestGui(Component name, int rows, int toolboxRow, Plugin plugin) {
    this(name, rows, toolboxRow, plugin, List.of());
  }

  protected PaginatedChestGui(Component name, int rows, int toolboxRow, Plugin plugin, ItemsConstructor<?> constructor) {
    this(name, rows, toolboxRow, plugin, constructor.getContent());
  }

  protected PaginatedChestGui(Component name, int rows, int toolboxRow, Plugin plugin, List<GuiItem> content) {
    super(name, rows, plugin);
    Preconditions.checkArgument(rows > 1, "rows must be bigger than 1 as a row being used as page control. current rows: %s", rows);
    Preconditions.checkArgument(toolboxRow >= 1 && toolboxRow <= 6,
      "Column must be between 1 and 6, but was %s", toolboxRow);
    this.pageSize = (rows * 9) - 9; // - 9 because we reserve a row for toolbox :)
    this.toolBoxRow = toolboxRow;
    this.content.addAll(content);
  }

  public void resetContent(ItemsConstructor<?> constructor) {
    resetContent(constructor.getContent());
  }

  public void resetContent(List<GuiItem> content) {
    this.content.clear();
    this.content.addAll(content);
    this.pageNumber = 1;
    this.updateGui();
  }


  protected void nextPage() {
    if (canNextPage()) {
      pageNumber++;
      updateGui();
    }
  }

  protected void backPage() {
    if (canBackPage()) {
      pageNumber--;
      updateGui();
    }
  }

  protected boolean canBackPage() {
    return pageNumber > 1;
  }

  protected boolean canNextPage() {
    return pageNumber < getLastPage();
  }

  protected List<GuiItem> getPageContent(int pageNumber) {
    return this.content.subList((pageSize * (pageNumber - 1)), Ints.constrainToRange((pageSize * pageNumber), 0, this.content.size()));
  }

  protected int getLastPage() {
    return (content.size() + pageSize - 1) / pageSize;
  }

  protected void insertToolBoxItem(int slot, GuiItem item) {
    Preconditions.checkArgument(slot >= 0 && slot <= 8, "Slot must be between 0 and 8, but was %s", 8, slot);
    int guiSlot = getToolboxStart() + slot;
    insertItem(guiSlot, item);
  }

  protected void removeToolBoxItem(int slot) {
    Preconditions.checkArgument(slot >= 0 && slot <= 8, "Slot must be between 0 and 8, but was %s", 8, slot);
    int guiSlot = getToolboxStart() + slot;
    removeItem(guiSlot);
  }

  public int getToolboxStart() {
    return (toolBoxRow - 1) * 9;
  }


  protected void renderToolBox() {
    final GuiItem BACK_PAGE_ITEM = new GuiItem(new ItemBuilder().setName(Component.text("<<<<< Previous page").color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)).setMaterial(Material.PAPER).build(), (item, event) -> backPage());
    final GuiItem NEXT_PAGE_ITEM = new GuiItem(new ItemBuilder().setName(Component.text("Next page >>>>>").color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)).setMaterial(Material.PAPER).build(), (item, event) -> nextPage());
    final GuiItem PAGE_NUMBER_ITEM = new GuiItem(new ItemBuilder().setAmount(pageNumber > 64 ? 1 : pageNumber).setName(Component.text(pageNumber)).setMaterial(Material.BOOK).build());

    if (canBackPage()) {
      insertToolBoxItem(3, BACK_PAGE_ITEM);
    } else {
      removeToolBoxItem(3);
    }
    insertToolBoxItem(4, PAGE_NUMBER_ITEM);
    if (canNextPage()) {
      insertToolBoxItem(5, NEXT_PAGE_ITEM);
    } else {
      removeToolBoxItem(5);
    }

  }

  public boolean isToolboxRow(int slot) {
    int start = (toolBoxRow - 1) * 9;
    int end = toolBoxRow * 9 - 1;
    return slot >= start && slot <= end;
  }

  protected void renderPage() {
    int slot = 0;
    for (GuiItem guiItem : getPageContent(pageNumber)) {
      while (isToolboxRow(slot)) {
        slot++;
      }
      insertItem(slot, guiItem);
      slot++;
    }
  }

  @Override
  protected void prepareRender() {
    clearItems();
    renderToolBox();
    renderPage();
  }
}
