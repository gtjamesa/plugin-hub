package tictac7x.charges.item;

import net.runelite.api.Client;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GraphicChanged;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.ItemContainerChanged;
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
import tictac7x.charges.item.listeners.OnAnimationChanged;
import tictac7x.charges.item.listeners.OnChatMessage;
import tictac7x.charges.item.listeners.OnGraphicChanged;
import tictac7x.charges.item.listeners.OnHitsplatApplied;
import tictac7x.charges.item.listeners.OnItemContainerChanged;
import tictac7x.charges.item.listeners.OnMenuEntryAdded;
import tictac7x.charges.item.listeners.OnResetDaily;
import tictac7x.charges.item.listeners.OnStatChanged;
import tictac7x.charges.item.listeners.OnVarbitChanged;
import tictac7x.charges.item.listeners.OnWidgetLoaded;
import tictac7x.charges.item.triggers.TriggerAnimation;
import tictac7x.charges.item.triggers.TriggerChatMessage;
import tictac7x.charges.item.triggers.TriggerGraphic;
import tictac7x.charges.item.triggers.TriggerHitsplat;
import tictac7x.charges.item.triggers.TriggerItem;
import tictac7x.charges.item.triggers.TriggerItemContainer;
import tictac7x.charges.item.triggers.TriggerDailyReset;
import tictac7x.charges.item.triggers.TriggerMenuEntryAdded;
import tictac7x.charges.item.triggers.TriggerStat;
import tictac7x.charges.item.triggers.TriggerVarbit;
import tictac7x.charges.item.triggers.TriggerWidget;
import tictac7x.charges.store.Charges;
import tictac7x.charges.store.ItemActivity;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;

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
    public TriggerDailyReset[] triggersResetsDaily = new TriggerDailyReset[]{};
    public TriggerItemContainer[] triggersItemContainers = new TriggerItemContainer[]{};
    public TriggerStat[] triggersStats = new TriggerStat[]{};
    public TriggerVarbit[] triggersVarbits = new TriggerVarbit[]{};
    public TriggerMenuEntryAdded[] triggersMenusEntriesAdded = new TriggerMenuEntryAdded[]{};

    private boolean inEquipment = false;
    private boolean inInventory = false;

    public int charges = Charges.UNKNOWN;

    final OnStatChanged onStatChanged;
    final OnChatMessage onChatMessage;
    final OnHitsplatApplied onHitsplatApplied;
    final OnWidgetLoaded onWidgetLoaded;
    @Nullable OnVarbitChanged onVarbitChanged;
    final OnAnimationChanged onAnimationChanged;
    final OnGraphicChanged onGraphicChanged;
    final OnItemContainerChanged onItemContainerChanged;
    final OnMenuEntryAdded onMenuEntryAdded;
    final OnResetDaily onResetDaily;

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
        this.onChatMessage = new OnChatMessage(this, configs, notifier);
        this.onHitsplatApplied = new OnHitsplatApplied(this, client);
        this.onWidgetLoaded = new OnWidgetLoaded(this, configs, client, client_thread);
        this.onAnimationChanged = new OnAnimationChanged(this, client);
        this.onGraphicChanged = new OnGraphicChanged(this, client);
        this.onItemContainerChanged = new OnItemContainerChanged(this, client);
        this.onMenuEntryAdded = new OnMenuEntryAdded(this, client);
        this.onResetDaily = new OnResetDaily(this);

        client_thread.invokeLater(this::loadChargesFromConfig);
    }

    public boolean inInventory() {
        return inInventory;
    }

    public boolean isEquipped() {
        return inEquipment;
    }

    public void setInInventory(final boolean inInventory) {
        this.inInventory = inInventory;
    }

    public void setInEquipment(final boolean inEquipment) {
        this.inEquipment = inEquipment;
    }

    public int getCharges() {
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

    public boolean isZeroChargesPositive() {
        if (getCurrentTriggerItem().isPresent()) {
            return getCurrentTriggerItem().get().zeroChargesIsPositive;
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

    public void onMenuEntryAdded(final MenuEntryAdded event) {
        if (config.useCommonMenuEntries()) {
            onMenuEntryAdded.trigger(event);
        }
    }

    public void onResetDaily() {
        onResetDaily.trigger();
    }
}


