package tictac7x.charges.item;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Item;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GraphicChanged;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.ItemDespawned;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
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
import tictac7x.charges.item.listeners.ListenerBase;
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
import tictac7x.charges.item.triggers.OnChatMessage;
import tictac7x.charges.item.triggers.OnItemContainerChanged;
import tictac7x.charges.item.triggers.TriggerBase;
import tictac7x.charges.item.triggers.TriggerItem;
import tictac7x.charges.store.Charges;
import tictac7x.charges.store.ItemActivity;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;

import javax.annotation.Nullable;
import java.awt.Color;
import java.util.Optional;

@Slf4j
public class ChargedItem {
    public final ItemKey infobox_id;
    public int item_id;
    protected final Client client;
    protected final ClientThread client_thread;
    protected final ItemManager items;
    protected final InfoBoxManager infoboxes;
    protected final ConfigManager configs;
    protected final ChatMessageManager chat_messages;
    protected final Notifier notifier;
    protected final ChargesImprovedConfig config;
    public final Store store;

    @Nullable public String config_key;
    @Nullable public String[] extra_config_keys;
    public TriggerItem[] triggersItems = new TriggerItem[]{};

    public int charges = Charges.UNKNOWN;

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

    public ChargedItem(
        final ItemKey infobox_id,
        final int item_id,
        final Client client,
        final ClientThread client_thread,
        final ConfigManager configs,
        final ItemManager items,
        final InfoBoxManager infoboxes,
        final ChatMessageManager chat_messages,
        final Notifier notifier,
        final ChargesImprovedConfig config,
        final Store store
    ) {
        this.infobox_id = infobox_id;
        this.item_id = item_id;
        this.client = client;
        this.client_thread = client_thread;
        this.configs = configs;
        this.items = items;
        this.infoboxes = infoboxes;
        this.chat_messages = chat_messages;
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

        client_thread.invokeLater(this::loadChargesFromConfig);
    }

    public boolean inInventory() {
        return store.inventoryContainsItem(item_id);
    }

    public boolean isEquipped() {
        return store.equipmentContainsItem(item_id);
    }

    public int getCharges() {
        if (getCurrentTriggerItem().isPresent() && getCurrentTriggerItem().get().fixed_charges != null) {
            return getCurrentTriggerItem().get().fixed_charges;
        }

        return charges;
    }

    public void onConfigChanged(final ConfigChanged event) {
        if (event.getGroup().equals(ChargesImprovedConfig.group) && event.getKey().equals(config_key)) {
            charges = Integer.parseInt(event.getNewValue());
        }
    }

    private void loadChargesFromConfig() {
        try {
            charges = Integer.parseInt(configs.getConfiguration(ChargesImprovedConfig.group, config_key));
        } catch (final Exception ignored) {}
    }

    public void setCharges(final int charges) {
        final int newCharges =
            // Unlimited
            charges == Charges.UNLIMITED ? charges :
            // 0 -> max charges
            negativeFullCharges().isPresent() ? Math.min(Math.max(0, charges), negativeFullCharges().get()) :
            // 0 -> charges
            Math.max(0, charges);

        if (this.charges != newCharges) {
            this.charges = newCharges;
            onChargesUpdated();

            if (config_key != null) {
                configs.setConfiguration(ChargesImprovedConfig.group, config_key, this.charges);
            }
        }
    }

    public void decreaseCharges(final int charges) {
        setCharges(Math.max(0, this.charges - charges));
    }

    public void increaseCharges(final int charges) {
        setCharges(Math.max(1, this.charges + charges));
    }

    protected void onChargesUpdated() {
//        chat_messages.queue(QueuedMessage.builder()
//            .type(ChatMessageType.CONSOLE)
//            .runeLiteFormattedMessage(getItemName() + " charges changed: " + charges)
//            .build()
//        );
    }

    public String getItemName() {
        return items.getItemComposition(item_id).getName();
    }

    public boolean isDeactivated() {
        final Optional<String> configStatus = Optional.ofNullable(configs.getConfiguration(ChargesImprovedConfig.group, config_key + "_status"));

        if (!configStatus.isPresent()) {
            return false;
        }

        return configStatus.get().equals(ItemActivity.DEACTIVATED.toString());
    }

    public boolean isActivated() {
        if (!getConfigStatusKey().isPresent()) {
            return false;
        }

        final Optional<String> status = Optional.ofNullable(configs.getConfiguration(ChargesImprovedConfig.group, getConfigStatusKey().get()));

        if (!status.isPresent()) {
            return false;
        }

        return status.get().equals(ItemActivity.ACTIVATED.toString());
    }

    public Optional<String> getConfigStatusKey() {
        if (config_key == null) return Optional.empty();
        return Optional.of(config_key + "_status");
    }

    public String getTooltipExtra() {
        return "";
    }

    private Optional<TriggerItem> getCurrentTriggerItem() {
        for (final TriggerItem triggerItem : triggersItems) {
            if (triggerItem.item_id == item_id) {
                return Optional.of(triggerItem);
            }
        }

        return Optional.empty();
    }

    public boolean needsToBeEquipped() {
        if (getCurrentTriggerItem().isPresent()) {
            return getCurrentTriggerItem().get().needsToBeEquipped;
        }

        return false;
    }

    public Optional<Integer> negativeFullCharges() {
        if (getCurrentTriggerItem().isPresent() && getCurrentTriggerItem().get().maxCharges.isPresent() && getCurrentTriggerItem().get().negativeMaxCharges) {
            return getCurrentTriggerItem().get().maxCharges;
        }

        return Optional.empty();
    }

    public void activityCallback(final ItemActivity ignored) {}

    public Color getTextColor() {
        if (getCharges() == Charges.UNKNOWN) {
            return config.getColorUnknown();
        }

        if (
                getCharges() == 0 ||
                        needsToBeEquipped() && !isEquipped()
        ) {
            return config.getColorEmpty();
        }

        return config.getColorDefault();
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
            client_thread.invokeLater(() -> {
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
            for (final TriggerItem triggerItem : triggersItems) {
                if (triggerItem.item_id == item.getId()) {
                    this.item_id = item.getId();
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
}


