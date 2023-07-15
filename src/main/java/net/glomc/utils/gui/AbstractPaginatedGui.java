package net.glomc.utils.gui;

import com.google.common.primitives.Ints;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

// THIS GUI is always 6 rows FOR PAGE STUFF ONLY!
public abstract class AbstractPaginatedGui<T> extends AbstractGui {

    protected final static ItemStack BACK_PAGE_ITEM = new ItemBuilder().setName(Component.text("<<<<< Previous page").color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)).setMaterial(Material.PAPER).build();
    protected final static ItemStack NEXT_PAGE_ITEM = new ItemBuilder().setName(Component.text("Next page >>>>>").color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)).setMaterial(Material.PAPER).build();
    protected final static ItemStack BLOCKED_PAGE = new ItemBuilder().setName(Component.text(".")).setMaterial(Material.BARRIER).build();

    protected final List<T> content;

    protected final AtomicBoolean locked = new AtomicBoolean(false);


    protected final AtomicInteger page = new AtomicInteger(1);

    protected AbstractPaginatedGui(Component name, List<T> content, Plugin plugin) {
        super(name, 6, plugin);
        this.content = new ArrayList<>(content);
    }

    protected void nextPage() {
        page.incrementAndGet();
    }

    protected void backPage() {
        page.decrementAndGet();
    }

    protected boolean canBackPage() {
        return this.page.get() > 1;
    }

    private boolean canNextPage() {
        return getPageContent(page.get()).size() == 45 && getPageContent(page.get() + 1).size() > 0;
    }


    protected List<T> getPageContent(int page) {
        return this.content.subList((45 * (page - 1)), Ints.constrainToRange((45 * page), 0, this.content.size()));
    }


    protected void cleanContentSlots() {
        for (int i = 0; i <= 44; i++) {
            removeItem(i);
        }
    }

    private static ItemStack makePageBook(int page) {
        int stack = page;
        if (page > 64) {
            stack = 1;
        }
        return new ItemBuilder().setName(Component.text("PAGE: " + page)).setMaterial(Material.BOOK).setAmount(stack).build();

    }

    private void renderToolBar() {
        if (canBackPage()) {
            insertItem(45, BACK_PAGE_ITEM);
        } else {
            insertItem(45, BLOCKED_PAGE);
        }
        if (canNextPage()) {
            insertItem(53, NEXT_PAGE_ITEM);
        } else {
            insertItem(53, BLOCKED_PAGE);
        }

        insertItem(49, makePageBook(page.get()));

    }


    @Override
    public void render() {
        if (locked.get()) return;
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            locked.set(true);
            cleanContentSlots();
            renderToolBar();
            renderContent();
            locked.set(false);
        });
    }

    protected abstract void renderContent();


    @Override
    public void onClick(InventoryClickEvent inventoryClickEvent) {
        // inventoryClickEvent.getWhoClicked().sendMessage("slot: " + inventoryClickEvent.getSlot());
        if (locked.get()) return;
        switch (inventoryClickEvent.getSlot()) {
            case 45 -> {
                if (canBackPage()) {
                    backPage();
                    render();
                }
            }
            case 53 -> {
                if (canNextPage()) {
                    nextPage();
                    render();
                }
            }
            default -> onContentClick(inventoryClickEvent);
        }
    }

    protected abstract void onContentClick(InventoryClickEvent event);

}
