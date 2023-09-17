package tictac7x.charges.store;

import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.MenuOptionClicked;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Store {
    private int gametick = 0;
    private int gametick_before = 0;

    @Nullable
    public ItemContainer inventory = null;

    @Nullable
    public ItemContainer equipment = null;

    @Nullable
    public Item[] inventory_items = null;

    public final List<String[]> menu_entries = new ArrayList<>();

    public void onItemContainerChanged(final ItemContainerChanged event) {
        if (event.getContainerId() == InventoryID.INVENTORY.getId()) {
            inventory = event.getItemContainer();
        } else if (event.getContainerId() == InventoryID.EQUIPMENT.getId()) {
            equipment = event.getItemContainer();
        }
    }

    public void onInventoryItemsChanged(final ItemContainerChanged event) {
        if (event.getContainerId() == InventoryID.INVENTORY.getId()) {
            inventory_items = event.getItemContainer().getItems();
        }
    }

    public void onMenuOptionClicked(final MenuOptionClicked event) {
        final String menu_target = event.getMenuTarget().replaceAll("</?col.*?>", "");
        final String menu_option = event.getMenuOption();

        if (
            // Not menu.
            menu_target.isEmpty() ||
            // Menu option not found.
            menu_option == null || menu_option.isEmpty()
        ) {
            return;
        }

        // Gametick changed, clear previous menu entries since they are no longer valid.
        if (gametick >= gametick_before + 2) {
            gametick = 0; gametick_before = 0;
            menu_entries.clear();
        }

        // Save menu option and target for other triggers to use.
        menu_entries.add(new String[]{menu_target, menu_option});
    }

    public void onGameTick(final GameTick ignored) {
        gametick++;
    }
}
