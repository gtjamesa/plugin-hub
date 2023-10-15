package tictac7x.charges.item;

import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GraphicChanged;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.StatChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.listeners.OnAnimationChanged;
import tictac7x.charges.item.listeners.OnChatMessage;
import tictac7x.charges.item.listeners.OnGraphicChanged;
import tictac7x.charges.item.listeners.OnHitsplatApplied;
import tictac7x.charges.item.listeners.OnItemContainerChanged;
import tictac7x.charges.item.listeners.OnStatChanged;
import tictac7x.charges.item.listeners.OnVarbitChanged;
import tictac7x.charges.item.listeners.OnWidgetLoaded;
import tictac7x.charges.item.triggers.TriggerVarbit;
import tictac7x.charges.store.Charges;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.ItemActivity;
import tictac7x.charges.store.Store;
import tictac7x.charges.item.triggers.TriggerAnimation;
import tictac7x.charges.item.triggers.TriggerChatMessage;
import tictac7x.charges.item.triggers.TriggerGraphic;
import tictac7x.charges.item.triggers.TriggerHitsplat;
import tictac7x.charges.item.triggers.TriggerItem;
import tictac7x.charges.item.triggers.TriggerItemContainer;
import tictac7x.charges.item.triggers.TriggerReset;
import tictac7x.charges.item.triggers.TriggerWidget;
import tictac7x.charges.item.triggers.TriggerStat;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

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
    public TriggerChatMessage[] triggersChatMessages = new TriggerChatMessage[]{};
    public TriggerAnimation[] triggersAnimations = new TriggerAnimation[]{};
    public TriggerGraphic[] triggersGraphics = new TriggerGraphic[]{};
    public TriggerHitsplat[] triggersHitsplats = new TriggerHitsplat[]{};
    public TriggerItem[] triggersItems = new TriggerItem[]{};
    public TriggerWidget[] triggersWidgets = new TriggerWidget[]{};
    public TriggerReset[] triggers_resets = new TriggerReset[]{};
    public TriggerItemContainer[] triggersItemContainers = new TriggerItemContainer[]{};
    public TriggerStat[] triggersStat = new TriggerStat[]{};
    public TriggerVarbit[] triggersVarbits = new TriggerVarbit[]{};

    public boolean in_equipment = false;
    public boolean in_inventory = false;
    protected boolean needs_to_be_equipped_for_infobox;

    public int charges = Charges.UNKNOWN;

    @Nullable public Integer negative_full_charges;
    public boolean zero_charges_is_positive = false;

    final OnStatChanged onStatChanged;
    final OnChatMessage onChatMessage;
    final OnHitsplatApplied onHitsplatApplied;
    final OnWidgetLoaded onWidgetLoaded;
    @Nullable OnVarbitChanged onVarbitChanged;
    final OnAnimationChanged onAnimationChanged;
    final OnGraphicChanged onGraphicChanged;
    final OnItemContainerChanged onItemContainerChanged;

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

        this.onStatChanged = new OnStatChanged(this, configs);
        this.onChatMessage = new OnChatMessage(this, notifier);
        this.onHitsplatApplied = new OnHitsplatApplied(this, client);
        this.onWidgetLoaded = new OnWidgetLoaded(this, client, client_thread);
        this.onAnimationChanged = new OnAnimationChanged(this, client);
        this.onGraphicChanged = new OnGraphicChanged(this, client);
        this.onItemContainerChanged = new OnItemContainerChanged(this, client);



        client_thread.invokeLater(this::loadChargesFromConfig);
    }

    public boolean inEquipment() {
        return in_equipment;
    }

    public boolean needsToBeEquipped() {
        return needs_to_be_equipped_for_infobox;
    }

    public int getCharges() {
        return charges;
    }

    public void onConfigChanged(final ConfigChanged event) {
        if (event.getGroup().equals(ChargesImprovedConfig.group) && event.getKey().equals(config_key)) {
            charges = Integer.parseInt(event.getNewValue());
        }
    }

    public void resetCharges() {
        if (triggers_resets == null) return;

        // Check for item resets.
        for (final TriggerReset trigger_reset : triggers_resets) {
            // Same item variations have different amount of charges.
            if (trigger_reset.item_id != null) {
                if (item_id == trigger_reset.item_id) {
                    setCharges(trigger_reset.charges);
                }

            // All variants of the item reset to the same amount of charges.
            } else {
                setCharges(trigger_reset.charges);
            }
        }
    }

    private void loadChargesFromConfig() {
        try {
            charges = Integer.parseInt(configs.getConfiguration(ChargesImprovedConfig.group, config_key));
        } catch (final Exception ignored) {}
    }

    public void setCharges(final int charges) {
        final int newCharges = negative_full_charges != null ? Math.min(Math.max(0, charges), negative_full_charges) : Math.max(0, charges);

        if (newCharges != this.charges) {
            this.charges = negative_full_charges != null ? Math.min(Math.max(0, charges), negative_full_charges) : Math.max(0, charges);
            onChargesUpdated();

            if (config_key != null) {
                setConfiguration(config_key, this.charges);
            }
        }
    }

    public void decreaseCharges(final int charges) {
        setCharges(Math.max(0, this.charges - charges));
    }

    public void increaseCharges(final int charges) {
        setCharges(Math.max(1, this.charges + charges));
    }

    public void setConfiguration(final String key, @Nonnull final String value) {
        configs.setConfiguration(ChargesImprovedConfig.group, key, value);
    }

    private void setConfiguration(final String key, final int value) {
        configs.setConfiguration(ChargesImprovedConfig.group, key, value);
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

    public void activityCallback(final ItemActivity ignored) {}

    public void onChatMessage(final ChatMessage event) {
        onChatMessage.trigger(event);
    }

    public void onHitsplatApplied(final HitsplatApplied event) {
        onHitsplatApplied.trigger(event);
    }

    public void onWidgetLoaded(final WidgetLoaded event) {
        onWidgetLoaded.trigger(event);
    }

    public void onVarbitChanged(final VarbitChanged event) {
        if (onVarbitChanged != null) {
            onVarbitChanged.trigger(event);
        }
    }

    public void onAnimationChanged(final AnimationChanged event) {
        onAnimationChanged.trigger(event);
    }

    public void onStatChanged(final StatChanged event) {
        onStatChanged.trigger(event);
    }

    public void onGraphicChanged(final GraphicChanged event) {
        onGraphicChanged.trigger(event);
    }

    public void onItemContainerChanged(final ItemContainerChanged event) {
        onItemContainerChanged.trigger(event);
    }
}


