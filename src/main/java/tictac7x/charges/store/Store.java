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
import tictac7x.charges.item.storage.StoreableItem;

import java.util.ArrayList;
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

    public Optional<ItemContainer> inventory = Optional.empty();
    public Optional<ItemContainer> equipment = Optional.empty();
    public Optional<ItemContainer> bank = Optional.empty();
    
    public List<StoreableItem> currentItems = new ArrayList<>();
    public List<StoreableItem> previousItems = new ArrayList<>();

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

    public int getInventoryPreviouslyEmptySlots() {
        return 28 - previousItems.size();
    }

    public void onStatChanged(final StatChanged event) {
        skillsXp.put(event.getSkill(), event.getXp());
    }

    public void onItemContainerChanged(final ItemContainerChanged event) {
        // Update inventory, save previous items.
        if (event.getContainerId() == InventoryID.INVENTORY.getId()) {
            inventory = Optional.of(event.getItemContainer());
            previousItems = currentItems;
            currentItems = new ArrayList<>();
            for (final Item item : event.getItemContainer().getItems()) {
                if (item.getId() != -1) {
                    currentItems.add(new StoreableItem(
                        item.getId(), 
                        itemManager.getItemComposition(item.getId()).getName(), 
                        item.getQuantity()
                    ));
                }
            }
        } else if (event.getContainerId() == InventoryID.EQUIPMENT.getId()) {
            equipment = Optional.of(event.getItemContainer());
        } else if (event.getContainerId() == InventoryID.BANK.getId()) {
            bank = Optional.of(event.getItemContainer());
        }

        updateStorage(event);
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
        // Automatically load all skill xps.
        if (!getSkillXp(Skill.MAGIC).isPresent()) {
            skillsXp.put(Skill.AGILITY,       client.getSkillExperience(Skill.AGILITY));
            skillsXp.put(Skill.ATTACK,        client.getSkillExperience(Skill.ATTACK));
            skillsXp.put(Skill.CONSTRUCTION,  client.getSkillExperience(Skill.CONSTRUCTION));
            skillsXp.put(Skill.COOKING,       client.getSkillExperience(Skill.COOKING));
            skillsXp.put(Skill.CRAFTING,      client.getSkillExperience(Skill.CRAFTING));
            skillsXp.put(Skill.DEFENCE,       client.getSkillExperience(Skill.DEFENCE));
            skillsXp.put(Skill.FARMING,       client.getSkillExperience(Skill.FARMING));
            skillsXp.put(Skill.FIREMAKING,    client.getSkillExperience(Skill.FIREMAKING));
            skillsXp.put(Skill.FISHING,       client.getSkillExperience(Skill.FISHING));
            skillsXp.put(Skill.FLETCHING,     client.getSkillExperience(Skill.FLETCHING));
            skillsXp.put(Skill.HERBLORE,      client.getSkillExperience(Skill.HERBLORE));
            skillsXp.put(Skill.HITPOINTS,     client.getSkillExperience(Skill.HITPOINTS));
            skillsXp.put(Skill.HUNTER,        client.getSkillExperience(Skill.HUNTER));
            skillsXp.put(Skill.MAGIC,         client.getSkillExperience(Skill.MAGIC));
            skillsXp.put(Skill.MINING,        client.getSkillExperience(Skill.MINING));
            skillsXp.put(Skill.PRAYER,        client.getSkillExperience(Skill.PRAYER));
            skillsXp.put(Skill.RANGED,        client.getSkillExperience(Skill.RANGED));
            skillsXp.put(Skill.RUNECRAFT,     client.getSkillExperience(Skill.RUNECRAFT));
            skillsXp.put(Skill.SLAYER,        client.getSkillExperience(Skill.SLAYER));
            skillsXp.put(Skill.SMITHING,      client.getSkillExperience(Skill.SMITHING));
            skillsXp.put(Skill.STRENGTH,      client.getSkillExperience(Skill.STRENGTH));
            skillsXp.put(Skill.THIEVING,      client.getSkillExperience(Skill.THIEVING));
            skillsXp.put(Skill.WOODCUTTING,   client.getSkillExperience(Skill.WOODCUTTING));
        }
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

    public boolean notInMenuTargets(final StoreableItem ...storeableItems) {
        final int[] storeableItemIds = new int[storeableItems.length];

        for (int i = 0; i < storeableItems.length; i ++) {
            storeableItemIds[i] = storeableItems[i].itemId;
        }

        return notInMenuTargets(storeableItemIds);
    }

    public boolean notInMenuTargets(final int ...itemIds) {
        return !inMenuTargets(itemIds);
    }

    public boolean inMenuOptions(final String ...options) {
        for (final MenuEntry menuEntry : menuOptionsClicked) {
            for (final String option : options) {
                if (menuEntry.option.contains(option)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean notInMenuOptions(final String ...options) {
        return !inMenuOptions(options);
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

    public int getInventoryItemQuantity(final int itemId) {
        int quantity = 0;

        for (final StoreableItem storeableItem : currentItems) {
            if (storeableItem.itemId == itemId) {
                quantity += storeableItem.quantity;
            }
        }

        return quantity;
    }

    public int getPreviousInventoryItemQuantity(final int itemId) {
        int quantity = 0;

        for (final StoreableItem storeableItem : previousItems) {
            if (storeableItem.itemId == itemId) {
                quantity += storeableItem.quantity;
            }
        }

        return quantity;
    }

    public boolean inventoryContainsItem(final int itemId) {
        for (final StoreableItem storeableItem : currentItems) {
            if (storeableItem.itemId == itemId) {
                return true;
            }
        }

        return false;
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
            event.getContainerId() != InventoryID.BANK.getId() &&
            event.getContainerId() != InventoryID.EQUIPMENT.getId()
        ) {
            return;
        }

        // Get all previous items.
        final Set<Integer> items = getAllItems();

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

    private Set<Integer> getAllItems() {
        final Optional<String> storageString = Optional.ofNullable(configManager.getConfiguration(ChargesImprovedConfig.group, ChargesImprovedConfig.storage));
        final Set<Integer> allItems = new HashSet<>();

        if (storageString.isPresent()) {
            for (final String itemString : storageString.get().split(",")) {
                try {
                    allItems.add(Integer.parseInt(itemString));
                } catch (final Exception ignored) {}
            }
        }

        return allItems;
    }

    public boolean itemInPossession(final int itemId) {
        for (final int item : getAllItems()) {
            if (item == itemId) {
                return true;
            }
        }

        return false;
    }

    public int getInventoryEmptySlots() {
        return 28 - getInventoryQuantity();
    }

    public int getInventoryQuantity() {
        return currentItems.size();
    }
}
