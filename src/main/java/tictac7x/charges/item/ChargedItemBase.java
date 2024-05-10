package tictac7x.charges.item;

import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.events.*;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.listeners.*;
import tictac7x.charges.item.triggers.TriggerBase;
import tictac7x.charges.item.triggers.TriggerItem;
import tictac7x.charges.store.Charges;
import tictac7x.charges.store.Store;

import java.awt.Color;
import java.util.Optional;

public abstract class ChargedItemBase {
    public final String configKey;
    protected final Client client;
    protected final ClientThread clientThread;
    protected final ItemManager itemManager;
    protected final InfoBoxManager infoBoxManager;
    protected final ConfigManager configManager;
    protected final ChatMessageManager chatMessageManager;
    protected final Notifier notifier;
    protected final ChargesImprovedConfig config;

    public final Store store;

    public int itemId;

    public TriggerItem[] items = new TriggerItem[]{};
    public TriggerBase[] triggers = new TriggerBase[]{};

    private final ListenerOnChatMessage listenerOnChatMessage;
    private final ListenerOnItemContainerChanged listenerOnItemContainerChanged;
    private final ListenerOnItemPickup listenerOnItemPickup;
    private final ListenerOnXpDrop listenerOnXpDrop;
    private final ListenerOnMenuEntryAdded listenerOnMenuEntryAdded;
    private final ListenerOnResetDaily listenerOnResetDaily;
    private final ListenerOnGraphicChanged listenerOnGraphicChanged;
    private final ListenerOnAnimationChanged listenerOnAnimationChanged;
    private final ListenerOnHitsplatApplied listenerOnHitsplatApplied;
    private final ListenerOnWidgetLoaded listenerOnWidgetLoaded;
    private final ListenerOnVarbitChanged listenerOnVarbitChanged;
    private final ListenerOnUserAction listenerOnUserAction;

    private boolean inInventory = false;
    private boolean inEquipment = false;

    public ChargedItemBase(
        final String configKey,
        final int itemId,
        final Client client,
        final ClientThread clientThread,
        final ConfigManager configManager,
        final ItemManager itemManager,
        final InfoBoxManager infoBoxManager,
        final ChatMessageManager chatMessageManager,
        final Notifier notifier,
        final ChargesImprovedConfig config,
        final Store store
    ) {
        this.itemId = itemId;
        this.configKey = configKey;

        this.client = client;
        this.clientThread = clientThread;
        this.configManager = configManager;
        this.itemManager = itemManager;
        this.infoBoxManager = infoBoxManager;
        this.chatMessageManager = chatMessageManager;
        this.notifier = notifier;
        this.config = config;
        this.store = store;

        listenerOnChatMessage = new ListenerOnChatMessage(client, this, notifier, config);
        listenerOnItemContainerChanged = new ListenerOnItemContainerChanged(client, this, notifier, config);
        listenerOnItemPickup = new ListenerOnItemPickup(client, this, notifier, config);
        listenerOnXpDrop = new ListenerOnXpDrop(client, this, notifier, config);
        listenerOnMenuEntryAdded = new ListenerOnMenuEntryAdded(client, this, notifier, config);
        listenerOnResetDaily = new ListenerOnResetDaily(client, this, notifier, config);
        listenerOnGraphicChanged = new ListenerOnGraphicChanged(client, this, notifier, config);
        listenerOnAnimationChanged = new ListenerOnAnimationChanged(client, this, notifier, config);
        listenerOnHitsplatApplied = new ListenerOnHitsplatApplied(client, this, notifier, config);
        listenerOnWidgetLoaded = new ListenerOnWidgetLoaded(client, this, notifier, config);
        listenerOnVarbitChanged = new ListenerOnVarbitChanged(client, this, notifier, config);
        listenerOnUserAction = new ListenerOnUserAction(client, this, notifier, config);
    }

    public abstract String getCharges();

    public boolean inInventory() {
        return inInventory;
    }

    public boolean isEquipped() {
        return inEquipment;
    }

    Optional<TriggerItem> getCurrentItem() {
        for (final TriggerItem triggerItem : items) {
            if (triggerItem.itemId == itemId) {
                return Optional.of(triggerItem);
            }
        }

        return Optional.empty();
    }

    public String getItemName() {
        return itemManager.getItemComposition(itemId).getName();
    }

    public boolean needsToBeEquipped() {
        if (getCurrentItem().isPresent()) {
            return getCurrentItem().get().needsToBeEquipped.isPresent();
        }

        return false;
    }

    public Color getTextColor() {
        if (getCharges().equals("?")) {
            return config.getColorUnknown();
        }

        if (getCharges().equals("0") || needsToBeEquipped() && !isEquipped()) {
            return config.getColorEmpty();
        }

        return config.getColorDefault();
    }

    protected String getChargesMinified(final int charges) {
        // Unlimited.
        if (charges == Charges.UNLIMITED) return "∞";

        // Unknown.
        if (charges == Charges.UNKNOWN) return "?";

        // Show as is.
        if (charges < 1000) return String.valueOf(charges);

        // Minify to use millions (M).
        if (charges >= 1000000) return charges / 1000000 + "M";

        // Minify to use thousands (K).
        final int thousands = charges / 1000;
        final int hundreds = Math.min((charges % 1000 + 50) / 100, 9);
        return thousands + (thousands < 10 && hundreds > 0 ? "." + hundreds : "") + "K";
    }

    public void onChatMessage(final ChatMessage event) {
        if (inInventory() || isEquipped()) {
            listenerOnChatMessage.trigger(event);
        }
    }

    public void onHitsplatApplied(final HitsplatApplied event) {
        if (inInventory() || isEquipped()) {
            listenerOnHitsplatApplied.trigger(event);
        }
    }

    public void onWidgetLoaded(final WidgetLoaded event) {
        if (inInventory() || isEquipped()) {
            clientThread.invokeLater(() -> {
                listenerOnWidgetLoaded.trigger(event);
            });
        }
    }

    public void onVarbitChanged(final VarbitChanged event) {
        if (inInventory() || isEquipped()) {
            listenerOnVarbitChanged.trigger(event);
        }
    }

    public void onStatChanged(final StatChanged event) {
        if (inInventory() || isEquipped()) {
            listenerOnXpDrop.trigger(event);
        }
    }

    public void onGraphicChanged(final GraphicChanged event) {
        if (inInventory() || isEquipped()) {
            listenerOnGraphicChanged.trigger(event);
        }
    }

    public void onAnimationChanged(final AnimationChanged event) {
        if (inInventory() || isEquipped()) {
            listenerOnAnimationChanged.trigger(event);
        }
    }

    public void onItemContainerChanged(final ItemContainerChanged event) {
        // Only check inventory, equipment and bank.
        if (
            event.getItemContainer().getId() != InventoryID.INVENTORY.getId() &&
            event.getItemContainer().getId() != InventoryID.EQUIPMENT.getId() &&
            event.getItemContainer().getId() != InventoryID.BANK.getId()
        ) return;

        // Find the best id for item to use.
        for (final Item item : event.getItemContainer().getItems()) {
            boolean itemFound = false;
            for (final TriggerItem triggerItem : items) {
                if (item.getId() == triggerItem.itemId) {
                    this.itemId = item.getId();
                    itemFound = true;
                }
            }

            if (itemFound) break;
        }

        // Update inventory and equipment statuses.
        inInventory = store.inventoryContainsItem(itemId);
        inEquipment = store.equipmentContainsItem(itemId);

        if (inInventory || inEquipment) {
            listenerOnItemContainerChanged.trigger(event);
        }
    }

    public void onMenuEntryAdded(final MenuEntryAdded event) {
        if (inInventory() || isEquipped()) {
            listenerOnMenuEntryAdded.trigger(event);
        }
    }

    public void onItemDespawned(final ItemDespawned event) {
        if (inInventory() || isEquipped()) {
            listenerOnItemPickup.trigger(event);
        }
    }

    public void onResetDaily() {
        listenerOnResetDaily.trigger();
    }

    public void onUserAction() {
        if (inInventory() || isEquipped()) {
            listenerOnUserAction.trigger();
        }
    }
}
