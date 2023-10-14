package tictac7x.charges.item;

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
import net.runelite.client.config.ConfigManager;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBox;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.ChargesImprovedPlugin;
import tictac7x.charges.item.listeners.OnAnimationChanged;
import tictac7x.charges.item.listeners.OnChatMessage;
import tictac7x.charges.item.listeners.OnGraphicChanged;
import tictac7x.charges.item.listeners.OnHitsplatApplied;
import tictac7x.charges.item.listeners.OnItemContainerChanged;
import tictac7x.charges.item.listeners.OnStatChanged;
import tictac7x.charges.item.listeners.OnVarbitChanged;
import tictac7x.charges.item.listeners.OnWidgetLoaded;
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
import java.awt.Color;
import java.util.Optional;

public class ChargedItem extends InfoBox {
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

    @Nullable
    public String config_key;
    @Nullable
    public String[] extra_config_keys;
    public TriggerChatMessage[] triggersChatMessages = new TriggerChatMessage[]{};
    public TriggerAnimation[] triggersAnimations = new TriggerAnimation[]{};
    public TriggerGraphic[] triggersGraphics = new TriggerGraphic[]{};
    public TriggerHitsplat[] triggersHitsplats = new TriggerHitsplat[]{};
    public TriggerItem[] triggersItems = new TriggerItem[]{};
    public TriggerWidget[] triggersWidgets = new TriggerWidget[]{};
    @Nullable protected TriggerReset[] triggers_resets;
    public TriggerItemContainer[] triggersItemContainers = new TriggerItemContainer[]{};
    public TriggerStat[] triggersStat = new TriggerStat[]{};

    public boolean in_equipment = false;
    public boolean in_inventory = false;
    protected boolean needs_to_be_equipped_for_infobox;
    public boolean is_negative;

    public int charges = Charges.UNKNOWN;

    private String tooltip;
    public boolean render = false;
    @Nullable public Integer negative_full_charges;
    public boolean zero_charges_is_positive = false;

    final OnStatChanged onStatChanged;
    final OnChatMessage onChatMessage;
    final OnHitsplatApplied onHitsplatApplied;
    final OnWidgetLoaded onWidgetLoaded;
    final OnVarbitChanged onVarbitChanged;
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
        final Store store,
        final Plugin plugin
    ) {
        super(items.getImage(item_id), plugin);
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
        this.onVarbitChanged = new OnVarbitChanged(this);
        this.onAnimationChanged = new OnAnimationChanged(this, client);
        this.onGraphicChanged = new OnGraphicChanged(this, client);
        this.onItemContainerChanged = new OnItemContainerChanged(this, client);


        client_thread.invokeLater(() -> {
            loadChargesFromConfig();
            updateTooltip();
            onChargesUpdated();
        });
    }

    @Override
    public String getName() {
        return super.getName() + "_" + item_id;
    }

    @Override
    public String getText() {
        return ChargesImprovedPlugin.getChargesMinified(charges);
    }

    @Override
    public String getTooltip() {
        return tooltip;
    }

    @Override
    public Color getTextColor() {
        if (charges == Charges.UNKNOWN) {
            return config.getColorUnknown();
        }

        if (
            charges == 0 && !zero_charges_is_positive ||
            negative_full_charges != null && charges == negative_full_charges ||
            needs_to_be_equipped_for_infobox && !in_equipment ||
            is_negative ||
            isDeactivated()
        ) {
            return config.getColorEmpty();
        }

        return config.getColorDefault();
    }

    public boolean inEquipment() {
        return in_equipment;
    }

    private boolean isAllowed() {
        return !config.getHiddenInfoboxes().contains(infobox_id);
    }

    public boolean isNegative() {
        return is_negative;
    }

    public boolean needsToBeEquipped() {
        return needs_to_be_equipped_for_infobox;
    }

    @Override
    public boolean render() {
        return config.showInfoboxes() && isAllowed() && render && charges != Charges.UNLIMITED;
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
        if (config_key == null) return;
        try {
            charges = Integer.parseInt(configs.getConfiguration(ChargesImprovedConfig.group, config_key));
        } catch (final Exception ignored) {}

    }

    public void setCharges(final int charges) {
        this.charges = negative_full_charges != null ? Math.min(Math.max(0, charges), negative_full_charges) : Math.max(0, charges);
        onChargesUpdated();

        if (config_key != null) {
            setConfiguration(config_key, this.charges);
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

    public void updateInfobox(final int item_id) {
        // Item id.
        this.item_id = item_id;

        // Tooltip.
        updateTooltip();

        // Image.
        setImage(items.getImage(item_id));
        infoboxes.updateInfoBoxImage(this);
    }

    public void updateTooltip() {
        tooltip = items.getItemComposition(item_id).getName() + (needs_to_be_equipped_for_infobox && !in_equipment ? " - Needs to be equipped" : "");
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

    public String getConfigStatusKey() {
        if (config_key == null) return null;

        return config_key + "_status";
    }

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
        onVarbitChanged.trigger(event);
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


