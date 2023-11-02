package tictac7x.charges.store;

import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.Skill;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.StatChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import tictac7x.charges.ChargesImprovedConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class Store {
    private final Client client;
    private final ItemManager itemManager;
    private final ConfigManager configManager;

    private int gametick = 0;
    private int gametick_before = 0;
    private int inventoryEmptySlots = 0;

    public Optional<ItemContainer> inventory = Optional.empty();
    public Optional<ItemContainer> equipment = Optional.empty();
    public Optional<Item[]> inventory_items = Optional.empty();
    public Optional<Item[]> bank_items = Optional.empty();

    public final List<MenuEntry> menuOptionsClicked = new ArrayList<>();
    private final Map<Skill, Integer> skillsXp = new HashMap<>();

    public Store(final Client client, final ItemManager itemManager, final ConfigManager configManager) {
        this.client = client;
        this.itemManager = itemManager;
        this.configManager = configManager;
    }

    public Optional<Integer> getSkillXp(final Skill skill) {
        if (skillsXp.containsKey(skill)) {
            return Optional.of(skillsXp.get(skill));
        }

        return Optional.empty();
    }

    public int getPreviouslyInventoryEmptySlots() {
        return inventoryEmptySlots;
    }

    public void onStatChanged(final StatChanged event) {
        skillsXp.put(event.getSkill(), event.getXp());
    }

    public void onItemContainerChanged(final ItemContainerChanged event) {
        if (event.getContainerId() == InventoryID.INVENTORY.getId()) {
            inventory = Optional.of(event.getItemContainer());
            inventoryEmptySlots = 28 - event.getItemContainer().count();
        }

        if (event.getContainerId() == InventoryID.EQUIPMENT.getId()) {
            equipment = Optional.of(event.getItemContainer());
        }

        updateStorage(event);
    }

    public void onInventoryItemsChanged(final ItemContainerChanged event) {
        if (event.getContainerId() == InventoryID.INVENTORY.getId()) {
            inventory_items = Optional.of(event.getItemContainer().getItems());
        }
    }

    public void onBankItemsChanged(final ItemContainerChanged event) {
        if (event.getContainerId() == InventoryID.BANK.getId()) {
            bank_items = Optional.of(event.getItemContainer().getItems());
        }
    }

    public void onMenuOptionClicked(final MenuOptionClicked event) {
        final String menuTarget = event.getMenuTarget().replaceAll("</?col.*?>", "");
        final String menuOption = event.getMenuOption();
        int impostorId = -1;

        try {
            impostorId = client.getObjectDefinition(event.getMenuEntry().getIdentifier()).getImpostor().getId();
        } catch (final Exception ignored) {}

        if (
            // Not menu.
            menuTarget.isEmpty() ||
            // Menu option not found.
            menuOption == null || menuOption.isEmpty() ||
            event.getMenuAction().name().equals("RUNELITE")
        ) {
            return;
        }

        // Gametick changed, clear previous menu entries since they are no longer valid.
        if (gametick >= gametick_before + 2) {
            gametick = 0; gametick_before = 0;
            menuOptionsClicked.clear();
        }

        // Save menu option and target for other triggers to use.
        menuOptionsClicked.add(new MenuEntry(menuTarget, menuOption, impostorId));
    }

    public void onGameTick(final GameTick ignored) {
        gametick++;
    }

    public boolean inMenuTargets(final int ...itemIds) {
        final String[] targets = new String[itemIds.length];

        for (int i = 0; i < itemIds.length; i++) {
            targets[i] = itemManager.getItemComposition(itemIds[i]).getName();
        }

        return inMenuTargets(targets);
    }

    public boolean inMenuTargets(final String ...targets) {
        for (final String target : targets) {
            for (final MenuEntry menuEntry : menuOptionsClicked) {
                if (menuEntry.target.contains(target)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean notInMenuTargets(final String ...targets) {
        return !inMenuTargets(targets);
    }

    public boolean notInMenuTargets(final int ...itemIds) {
        return !inMenuTargets(itemIds);
    }

    public boolean inMenuOptions(final String option) {
        for (final MenuEntry menuEntry : menuOptionsClicked) {
            if (menuEntry.option.contains(option)) {
                return true;
            }
        }

        return false;
    }

    public boolean notInMenuOptions(final String option) {
        return !inMenuOptions(option);
    }

    public boolean inMenuImpostors(final int ...impostorIds) {
        for (final MenuEntry menuEntry : menuOptionsClicked) {
            for (final int impostorId : impostorIds) {
                if (menuEntry.impostorId == impostorId) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean notInMenuImpostors(final int ...impostorIds) {
        return !inMenuImpostors(impostorIds);
    }

    public int getInventoryItemsDifference(final ItemContainerChanged event) {
        if (
            event.getItemContainer().getId() != InventoryID.INVENTORY.getId() ||
            !inventory_items.isPresent()
        ) return 0;

        return itemsDifference(inventory_items.get(), event.getItemContainer().getItems());
    }

    public int getBankItemsDifference(final ItemContainerChanged event) {
        if (event.getContainerId() != InventoryID.BANK.getId() || !bank_items.isPresent()) {
            return 0;
        }

        int difference = 0;
        final Map<Integer, Integer> differences = new HashMap<>();

        // New bank items.
        for (final Item new_item : event.getItemContainer().getItems()) {
            differences.put(new_item.getId(), new_item.getQuantity());
            itemManager.getItemComposition(0).getPlaceholderId();
        }

        // Previous bank items.
        for (final Item old_item : bank_items.get()) {
            differences.put(old_item.getId(), differences.getOrDefault(old_item.getId(), 0) - old_item.getQuantity());
        }

        // Find differences between all items.
        for (final Map.Entry<Integer, Integer> item_difference : differences.entrySet()) {
            if (item_difference.getValue() > 0) {
                difference += item_difference.getValue();
            }
        }

        return difference;
    }

    private int itemsDifference(final Item[] items_before, final Item[] items_after) {
        final int items_before_count = (int) Arrays.stream(items_before).filter(item -> item.getId() != -1).count();
        final int items_after_count = (int) Arrays.stream(items_after).filter(item -> item.getId() != -1).count();

        return Math.abs(items_before_count - items_after_count);
    }

    public int getInventoryItemCount(final int itemId) {
        return inventory.map(itemContainer -> itemContainer.count(itemId)).orElse(0);
    }

    public int getPreviousInventoryItemCount(final int itemId) {
        int count = 0;

        if (inventory_items.isPresent()) {
            for (final Item item : inventory_items.get()) {
                if (item.getId() == itemId) {
                    count += item.getQuantity();
                }
            }
        }

        return count;
    }

    public int getEquipmentItemCount(final int itemId) {
        return equipment.map(itemContainer -> itemContainer.count(itemId)).orElse(0);
    }

    public boolean inventoryContainsItem(final int itemId) {
        return inventory.map(itemContainer -> itemContainer.contains(itemId)).orElse(false);
    }

    public boolean equipmentContainsItem(final int ...itemIds) {
        if (!equipment.isPresent()) return false;

        for (final Item equipmentItem : equipment.get().getItems()) {
            for (final int itemId : itemIds) {
                if (equipmentItem.getId() == itemId) {
                    return true;
                }
            }
        }

        return false;
    }

    private void updateStorage(final ItemContainerChanged event) {
        if (
            event.getContainerId() != InventoryID.INVENTORY.getId() &&
            event.getContainerId() == InventoryID.BANK.getId() &&
            event.getContainerId() == InventoryID.EQUIPMENT.getId()
        ) {
            return;
        }

        // Get all previous items.
        final Set<Integer> items = getAllitems();

        // Update items.
        for (final Item item : event.getItemContainer().getItems()) {
            if (item.getId() != -1) {
                items.add(item.getId());
            }
        }

        final StringBuilder storage = new StringBuilder();
        for (final Integer item : items) {
            storage.append(item).append(",");
        }

        // Update config all items.
        configManager.setConfiguration(ChargesImprovedConfig.group, ChargesImprovedConfig.storage, storage.toString().replaceAll(",$", ""));
    }

    private Set<Integer> getAllitems() {
        final String storageString = configManager.getConfiguration(ChargesImprovedConfig.group, ChargesImprovedConfig.storage);
        final Set<Integer> allItems = new HashSet<>();

        for (final String itemString : storageString.split(",")) {
            try {
                allItems.add(Integer.parseInt(itemString));
            } catch (final Exception ignored) {}
        }

        return allItems;
    }

    public boolean possessItem(final int itemId) {
        for (final int item : getAllitems()) {
            if (item == itemId) {
                return true;
            }
        }

        return false;
    }
}
