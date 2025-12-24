package net.glomc.utils.gui;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.glomc.utils.gui.builders.ItemBuilder;
import net.glomc.utils.gui.components.GuiItem;
import net.glomc.utils.gui.components.GuiListener;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class TestPlugin extends JavaPlugin {

  @Override
  public void onEnable() {
    TestGuiCommand testCommand = new TestGuiCommand(this);
    registerCommand("testgui", testCommand);
    this.getServer().getPluginManager().registerEvents(new GuiListener(), this);
  }

  @Override
  public void onDisable() {

  }

  public static class TestPageGui extends PaginatedChestGui {

    protected TestPageGui(Plugin plugin) {


      super(Component.text("page gui test"), 6, 6, plugin, getTestItems());
    }

    private static List<GuiItem> getTestItems() {
      List<GuiItem> content = new ArrayList<>();
      for (int i = 1; i <= 360*360; i++) {
        GuiItem item1 = new GuiItem(new ItemBuilder().setName(Component.text(i)).build());
        content.add(item1);
      }
      return content;
    }

    @Override
    protected void renderToolBox() {
      super.renderToolBox();
      insertToolBoxItem(7, new GuiItem(ItemStack.of(Material.BARRIER), (item, event) -> close((Player) event.getWhoClicked())));
    }
  }


  public static class TestChestGui extends AbstractChestGui {

    private int x = 0;

    public TestChestGui(Plugin plugin) {
      super(Component.text("gui test"), 3, plugin);

    }

    public void setOption(int x) {
      this.x = x;
    }

    public void renderOptions() {
      GuiItem item1 = new GuiItem(new ItemBuilder().setEnchanted(x == 0).build(), (item, event) -> {
        setOption(0);
        updateGui();
      });
      GuiItem item2 = new GuiItem(new ItemBuilder().setEnchanted(x == 1).build(), (item, event) -> {
        setOption(1);
        updateGui();
      });
      GuiItem item3 = new GuiItem(new ItemBuilder().setEnchanted(x == 2).build(), (item, event) -> {
        setOption(2);
        updateGui();
      });
      GuiItem item4 = new GuiItem(new ItemBuilder().setEnchanted(x == 3).build(), (item, event) -> {
        setOption(3);
        updateGui();
      });
      insertItem(1, 1, item1);
      insertItem(1, 3, item2);
      insertItem(1, 5, item3);
      insertItem(1, 7, item4);
    }

    @Override
    protected void prepareRender() {
      renderOptions();
    }

  }

  public static class TestGuiCommand implements BasicCommand {

    private final TestPlugin plugin;

    public TestGuiCommand(TestPlugin plugin) {
      this.plugin = plugin;
    }

    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] strings) {
      if (commandSourceStack.getExecutor() instanceof Player player) {
        player.closeInventory();
        new TestPageGui(plugin).open(player);
      }
    }
  }


}
