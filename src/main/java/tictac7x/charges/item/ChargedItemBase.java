package tictac7x.charges.item;

import net.runelite.api.Client;
import net.runelite.api.Item;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GraphicChanged;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.ItemDespawned;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.StatChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.listeners.ListenerOnChatMessage;
import tictac7x.charges.item.listeners.ListenerOnGraphicChanged;
import tictac7x.charges.item.listeners.ListenerOnHitsplatApplied;
import tictac7x.charges.item.listeners.ListenerOnItemContainerChanged;
import tictac7x.charges.item.listeners.ListenerOnItemDespawned;
import tictac7x.charges.item.listeners.ListenerOnMenuEntryAdded;
import tictac7x.charges.item.listeners.ListenerOnResetDaily;
import tictac7x.charges.item.listeners.ListenerOnVarbitChanged;
import tictac7x.charges.item.listeners.ListenerOnWidgetLoaded;
import tictac7x.charges.item.listeners.ListenerOnXpDrop;
import tictac7x.charges.item.triggers.TriggerBase;
import tictac7x.charges.item.triggers.TriggerItem;
import tictac7x.charges.store.Charges;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;

import java.awt.Color;
import java.util.Optional;

public abstract class ChargedItemBase {
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
    public final ItemKey itemKey;
    public final Optional<String> configKey;

    public TriggerItem[] items = new TriggerItem[]{};
    public TriggerBase[] triggers = new TriggerBase[]{};

    private final ListenerOnChatMessage listenerOnChatMessage;
    private final ListenerOnItemContainerChanged listenerOnItemContainerChanged;
    private final ListenerOnItemDespawned listenerOnItemDespawned;
    private final ListenerOnXpDrop listenerOnXpDrop;
    private final ListenerOnMenuEntryAdded listenerOnMenuEntryAdded;
    private final ListenerOnResetDaily listenerOnResetDaily;
    private final ListenerOnGraphicChanged listenerOnGraphicChanged;
    private final ListenerOnHitsplatApplied listenerOnHitsplatApplied;
    private final ListenerOnWidgetLoaded listenerOnWidgetLoaded;
    private final ListenerOnVarbitChanged listenerOnVarbitChanged;

    public ChargedItemBase(final String configKey, final ItemKey itemKey, final int itemId, final Client client, final ClientThread clientThread, final ConfigManager configManager, final ItemManager itemManager, final InfoBoxManager infoBoxManager, final ChatMessageManager chatMessageManager, final Notifier notifier, final ChargesImprovedConfig config, final Store store) {
        this(Optional.of(configKey), itemKey, itemId, client, clientThread, configManager, itemManager, infoBoxManager, chatMessageManager, notifier, config, store);
    }

    public ChargedItemBase(final ItemKey itemKey, final int itemId, final Client client, final ClientThread clientThread, final ConfigManager configManager, final ItemManager itemManager, final InfoBoxManager infoBoxManager, final ChatMessageManager chatMessageManager, final Notifier notifier, final ChargesImprovedConfig config, final Store store) {
        this(Optional.empty(), itemKey, itemId, client, clientThread, configManager, itemManager, infoBoxManager, chatMessageManager, notifier, config, store);
    }

    public ChargedItemBase(
            final Optional<String> configKey,
            final ItemKey itemKey,
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
        this.itemKey = itemKey;
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
        listenerOnItemDespawned = new ListenerOnItemDespawned(client, this, notifier, config);
        listenerOnXpDrop = new ListenerOnXpDrop(client, this, notifier, config);
        listenerOnMenuEntryAdded = new ListenerOnMenuEntryAdded(client, this, notifier, config);
        listenerOnResetDaily = new ListenerOnResetDaily(client, this, notifier, config);
        listenerOnGraphicChanged = new ListenerOnGraphicChanged(client, this, notifier, config);
        listenerOnHitsplatApplied = new ListenerOnHitsplatApplied(client, this, notifier, config);
        listenerOnWidgetLoaded = new ListenerOnWidgetLoaded(client, this, notifier, config);
        listenerOnVarbitChanged = new ListenerOnVarbitChanged(client, this, notifier, config);

        clientThread.invokeLater(() -> {
            loadCharges();
        });
    }


    public String getCharges() {
        final Optional<TriggerItem> currentItem = getCurrentItem();
        if (currentItem.isPresent() && currentItem.get().fixed_charges != null) {
            return String.valueOf(currentItem.get().fixed_charges);
        }

        return "?";
    }

    public boolean inInventory() {
        return store.inventoryContainsItem(itemId);
    }

    public boolean isEquipped() {
        return store.equipmentContainsItem(itemId);
    }

    public abstract void loadCharges();

    Optional<TriggerItem> getCurrentItem() {
        for (final TriggerItem triggerItem : items) {
            if (triggerItem.item_id == itemId) {
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
            return getCurrentItem().get().needsToBeEquipped;
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

    public void onItemContainerChanged(final ItemContainerChanged event) {
        chargedItemIdChecker: for (final Item item : event.getItemContainer().getItems()) {
            for (final TriggerItem triggerItem : items) {
                if (triggerItem.item_id == item.getId()) {
                    this.itemId = item.getId();
                    break chargedItemIdChecker;
                }
            }
        }

        if (inInventory() || isEquipped()) {
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
            listenerOnItemDespawned.trigger(event);
        }
    }

    public void onResetDaily() {
        listenerOnResetDaily.trigger();
    }

    public void onConfigChanged(final ConfigChanged event) {}
}
