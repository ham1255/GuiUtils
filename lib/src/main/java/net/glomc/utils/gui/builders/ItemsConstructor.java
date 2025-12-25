package net.glomc.utils.gui.builders;

import net.glomc.utils.gui.components.GuiItem;

import java.util.ArrayList;
import java.util.List;

public interface ItemsConstructor<T> {

  default List<GuiItem> getContent() {
    List<GuiItem> items = new ArrayList<>();
    getData().forEach(data -> items.add(builder(data)));
    return items;
  }

  GuiItem builder(T data);

  List<T> getData();

}
