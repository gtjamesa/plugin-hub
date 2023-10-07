package tictac7x.charges.item;

import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Hitsplat;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GraphicChanged;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.StatChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
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
import tictac7x.charges.store.ChargesItem;
import tictac7x.charges.store.Store;
import tictac7x.charges.triggers.TriggerAnimation;
import tictac7x.charges.triggers.TriggerChatMessage;
import tictac7x.charges.triggers.TriggerGraphic;
import tictac7x.charges.triggers.TriggerHitsplat;
import tictac7x.charges.triggers.TriggerItem;
import tictac7x.charges.triggers.TriggerItemContainer;
import tictac7x.charges.triggers.TriggerReset;
import tictac7x.charges.triggers.TriggerWidget;
import tictac7x.charges.triggers.TriggerStat;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChargedItem extends InfoBox {
    public final ChargesItem infobox_id;
    public int item_id;
    protected final Client client;
    protected final ClientThread client_thread;
    protected final ItemManager items;
    protected final InfoBoxManager infoboxes;
    protected final ConfigManager configs;
    protected final ChatMessageManager chat_messages;
    protected final Notifier notifier;
    protected final ChargesImprovedConfig config;
    protected final Store store;

    @Nullable protected String config_key;
    @Nullable protected String[] extra_config_keys;
    @Nullable protected TriggerChatMessage[] triggers_chat_messages;
    @Nullable protected TriggerAnimation[] triggers_animations;
    @Nullable protected TriggerGraphic[] triggers_graphics;
    @Nullable protected TriggerHitsplat[] triggers_hitsplats;
    @Nullable protected TriggerItem[] triggers_items;
    @Nullable protected TriggerWidget[] triggers_widgets;
    @Nullable protected TriggerReset[] triggers_resets;
    @Nullable protected TriggerItemContainer[] triggers_item_containers;
    @Nullable protected TriggerStat[] triggers_stats;

    protected boolean in_equipment = false;
    private boolean in_inventory = false;
    protected boolean needs_to_be_equipped_for_infobox;
    private boolean is_negative;

    protected int charges = ChargesImprovedPlugin.CHARGES_UNKNOWN;

    private String tooltip;
    private boolean render = false;
    @Nullable public Integer negative_full_charges;
    public boolean zero_charges_is_positive = false;

    public ChargedItem(
        final ChargesItem infobox_id,
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
        @Nullable final String config_status = configs.getConfiguration(ChargesImprovedConfig.group, config_key + "_status");

        if (charges == ChargesImprovedPlugin.CHARGES_UNKNOWN) {
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

    public TriggerItem[] getTriggersItems() {
        return triggers_items;
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
        return config.showInfoboxes() && isAllowed() && render && charges != ChargesImprovedPlugin.CHARGES_UNLIMITED;
    }

    public int getCharges() {
        return charges;
    }

    public void onStatChanged(final StatChanged event) {
        if (triggers_stats == null) return;

        for (final TriggerStat triggerStat : triggers_stats) {
            // Skill check.
            if (triggerStat.skill != event.getSkill()) continue;

            // Extra config check.
            if (
                triggerStat.extra_config.isPresent() &&
                !configs.getConfiguration(ChargesImprovedConfig.group, triggerStat.extra_config.get()[0]).equals(triggerStat.extra_config.get()[1])
            ) continue;

            if (triggerStat.discharges.isPresent()) {
                decreaseCharges(triggerStat.discharges.get());
            }
        }
    }

    public void onVarbitChanged(final VarbitChanged event) {
        if (triggers_items == null) return;

        for (final TriggerItem trigger_item : triggers_items) {
            if (
                // Item trigger does not use varbits.
                trigger_item.varbit_id == null ||
                trigger_item.varbit_value == null ||
                // Varbit does not match the trigger item required varbit.
                event.getVarbitId() != trigger_item.varbit_id ||
                event.getValue() != trigger_item.varbit_value
            ) continue;

            // Find out charges for the item.
            if (trigger_item.fixed_charges != null) {
                int charges = 0;
                charges += store.inventory != null ? store.inventory.count(trigger_item.item_id) * trigger_item.fixed_charges : 0;
                charges += store.equipment != null ? store.equipment.count(trigger_item.item_id) * trigger_item.fixed_charges : 0;
                this.charges = charges;

            // Find out charges based on the amount of item.
            } else if (trigger_item.quantity_charges) {
                int charges = 0;
                charges += store.inventory != null ? store.inventory.count(trigger_item.item_id) : 0;
                charges += store.equipment != null ? store.equipment.count(trigger_item.item_id) : 0;
                this.charges = charges;
            }

            this.is_negative = trigger_item.is_negative;
        }
    }

    private int getInventoryItemsDifference(final ItemContainerChanged event) {
        if (event.getItemContainer().getId() != InventoryID.INVENTORY.getId() || store.inventory_items == null) return 0;

        return itemsDifference(store.inventory_items, event.getItemContainer().getItems());
    }

    private int getBankItemsDifference(final ItemContainerChanged event) {
        int difference = 0;

        if (event.getContainerId() == InventoryID.BANK.getId() && store.bank_items != null) {
            Map<Integer, Integer> differences = new HashMap<>();

            // New bank items.
            for (final Item new_item : event.getItemContainer().getItems()) {
                differences.put(new_item.getId(), new_item.getQuantity());
                items.getItemComposition(0).getPlaceholderId();
            }

            // Previous bank items.
            for (final Item old_item : store.bank_items) {
                differences.put(old_item.getId(), differences.getOrDefault(old_item.getId(), 0) - old_item.getQuantity());
            }

            // Find differences between all items.
            for (final Map.Entry<Integer, Integer> item_difference : differences.entrySet()) {
                if (item_difference.getValue() > 0) {
                    difference += item_difference.getValue();
                }
            }
        }

        return difference;
    }

    public void onItemContainersChanged(final ItemContainerChanged event) {
        if (event.getItemContainer() == null) return;

        // Find items difference before items are overridden.
        final int inventory_items_difference = getInventoryItemsDifference(event);
        final int bank_items_difference = getBankItemsDifference(event);

        if (triggers_item_containers != null) {
            for (final TriggerItemContainer trigger_item_container : triggers_item_containers) {
                // Item container is wrong.
                if (trigger_item_container.inventory_id != event.getContainerId()) continue;

                // Menu target check.
                if (trigger_item_container.menu_target != null && !inMenuTargets(trigger_item_container.menu_target)) continue;

                // Menu option check.
                if (trigger_item_container.menu_option != null && !inMenuOptions(trigger_item_container.menu_option)) continue;

                // Fixed charges.
                if (trigger_item_container.fixed_charges != null) {
                    setCharges(trigger_item_container.fixed_charges);
                    break;
                }

                // Increase by difference of amount of items.
                if (trigger_item_container.increase_by_inventory_difference) {
                    increaseCharges(inventory_items_difference);
                    break;
                }

                // Decrease by difference of amount of items.
                if (trigger_item_container.decrease_by_bank_difference) {
                    decreaseCharges(bank_items_difference);
                    break;
                }

                // Charges dynamically based on the items of the item container.
                if (event.getItemContainer() != null) {
                    setCharges(event.getItemContainer().count());
                    break;
                }
            }
        }

        // No trigger items to detect charges.
        if (triggers_items == null) return;

        boolean in_inventory = false;
        boolean in_equipment = false;
        boolean render = false;
        Integer charges = null;

        for (final TriggerItem trigger_item : triggers_items) {
            // Item trigger has varbit check.
            if (
                trigger_item.varbit_id != null &&
                trigger_item.varbit_value != null &&
                client.getVarbitValue(trigger_item.varbit_id) != trigger_item.varbit_value
            ) {
                continue;
            }

            // Negative item check.
            is_negative = trigger_item.is_negative;

            // Find out if item is equipped.
            final boolean in_inventory_item = store.inventory != null && store.inventory.contains(trigger_item.item_id);
            final boolean in_equipment_item = store.equipment != null && store.equipment.contains(trigger_item.item_id);

            // Item not found, don't calculate charges.
            if (!in_equipment_item && !in_inventory_item) continue;

            // Item found.
            render = true;
            if (in_inventory_item) in_inventory = true;
            if (in_equipment_item) in_equipment = true;

            // Update infobox item picture and tooltip dynamically based on the items if use has different variant of it.
            if (trigger_item.item_id != item_id) {
                updateInfobox(trigger_item.item_id);
            }

            // Find out charges for the item.
            if (trigger_item.fixed_charges != null) {
                if (charges == null) charges = 0;
                charges += store.inventory != null ? store.inventory.count(trigger_item.item_id) * trigger_item.fixed_charges : 0;
                charges += store.equipment != null ? store.equipment.count(trigger_item.item_id) * trigger_item.fixed_charges : 0;
            // Find out charges based on the amount of item.
            } else if (trigger_item.quantity_charges) {
                if (charges == null) charges = 0;
                charges += store.inventory != null ? store.inventory.count(trigger_item.item_id) : 0;
                charges += store.equipment != null ? store.equipment.count(trigger_item.item_id) : 0;
            }
        }

        // Update infobox variables for other triggers.
        this.render = render;
        this.in_inventory = in_inventory;
        this.in_equipment = in_equipment;
        if (charges != null) this.charges = charges;
        updateTooltip();
    }

    public void onChatMessage(final ChatMessage event) {
        if (
            // No chat messages triggers.
            triggers_chat_messages == null ||
            // Message type that we are not interested in.
            event.getType() != ChatMessageType.GAMEMESSAGE && event.getType() != ChatMessageType.SPAM && event.getType() != ChatMessageType.MESBOX ||
            // No config to save charges to.
            config_key == null ||
            // Not in inventory nor in equipment.
            (!in_inventory && !in_equipment)
        ) return;

        final String message = event.getMessage().replaceAll("</?col.*?>", "").replaceAll("<br>", " ");

        for (final TriggerChatMessage chat_message : triggers_chat_messages) {
            final Matcher matcher = chat_message.message.matcher(message);

            // Message should be ignored.
            if (chat_message.ignore_message != null) {
                final Matcher ignore_matcher = chat_message.ignore_message.matcher(message);
                if (ignore_matcher.find()) return;
            }

            // Specific item check.
            if (!chat_message.specific_items.isEmpty() && !chat_message.specific_items.contains(item_id)) continue;

            // Message does not match the pattern.
            if (!matcher.find()) continue;

            // Menu target check.
            if (chat_message.menu_target && !inMenuTargets(getItemName())) continue;

            // Item needs to be equipped.
            if (chat_message.equipped && !in_equipment) continue;

            // Notifications.
            if (chat_message.notification) {
                notifier.notify(chat_message.notification_message != null ? chat_message.notification_message : message);
            }

            // Increase charges by fixed amount.
            if (chat_message.increase_charges != null) {
                increaseCharges(chat_message.increase_charges);

            // Decrease charges by fixed amount.
            } else if (chat_message.decrease_charges != null) {
                decreaseCharges(chat_message.decrease_charges);

            // Set charges to fixed amount.
            } else if (chat_message.fixed_charges != null) {
                setCharges(chat_message.fixed_charges);

            // Set charges from multiple amounts.
            } else if (chat_message.multiple_charges) {
                int charges = 0;

                final Matcher matcher_multiple = Pattern.compile(".*?(\\d+)").matcher(message);
                while (matcher_multiple.find()) {
                    charges += Integer.parseInt(matcher_multiple.group(1));
                }

                setCharges(charges);

            // Charges need to be calculated from total - used.
            } else if (chat_message.use_difference) {
                final int used = Integer.parseInt(matcher.group("used"));
                final int total = Integer.parseInt(matcher.group("total"));
                setCharges(total - used);

            // Set charges dynamically from the chat message.
            } else {
                try {
                    final int charges = Integer.parseInt(matcher.group("charges").replaceAll(",", "").replaceAll("\\.", ""));

                    // Increase dynamically.
                    if (chat_message.increase_dynamically) {
                        increaseCharges(charges);
                    // Decrease dynamically.
                    } else if (chat_message.decrease_dynamically) {
                        decreaseCharges(charges);
                    } else {
                        setCharges(charges);
                    }
                } catch (final Exception ignored) {}
            }

            // Extra consumer.
             if (chat_message.extra_consumer != null) {
                 chat_message.extra_consumer.accept(message);
             }

            // Check extra matches groups.
            if (extra_config_keys != null) {
                for (final String extra_group : extra_config_keys) {
                    try {
                        setConfiguration(config_key + "_" + extra_group, matcher.group(extra_group).replaceAll(",", ""));
                    } catch (final Exception ignored) {}
                }
            }

            // Chat message used, no need to check other messages.
            return;
        }
    }

    public void onWidgetLoaded(final WidgetLoaded event) {
        if (triggers_widgets == null || config_key == null) return;

        client_thread.invokeLater(() -> {
            for (final TriggerWidget trigger_widget : triggers_widgets) {
                if (event.getGroupId() != trigger_widget.group_id) continue;

                Widget widget = client.getWidget(trigger_widget.group_id, trigger_widget.child_id);
                if (trigger_widget.sub_child_id != null && widget != null) widget = widget.getChild(trigger_widget.sub_child_id);
                if (widget == null) continue;

                final String message = widget.getText().replaceAll("</?col.*?>", "").replaceAll("<br>", " ");
                final Pattern regex = Pattern.compile(trigger_widget.message);
                final Matcher matcher = regex.matcher(message);
                if (!matcher.find()) continue;

                // Charges amount is fixed.
                if (trigger_widget.charges != null) {
                    setCharges(trigger_widget.charges);
                // Charges amount has custom logic.
                } else if (trigger_widget.consumer != null) {
                    trigger_widget.consumer.accept(message);
                // Charges amount is dynamic.
                } else if (matcher.group("charges") != null) {
                    final int charges = Integer.parseInt(matcher.group("charges").replaceAll(",", ""));

                    // Charges increased dynamically.
                    if (trigger_widget.increase_dynamically) {
                        increaseCharges(charges);
                    } else {
                        setCharges(charges);
                    }
                }

                // Check extra matches groups.
                if (extra_config_keys != null) {
                    for (final String extra_group : extra_config_keys) {
                        final String extra = matcher.group(extra_group);
                        if (extra != null) setConfiguration(config_key + "_" + extra_group, extra);
                    }
                }
            }
        });
    }

    public void onConfigChanged(final ConfigChanged event) {
        if (event.getGroup().equals(ChargesImprovedConfig.group) && event.getKey().equals(config_key)) {
            charges = Integer.parseInt(event.getNewValue());
        }
    }

    public void onAnimationChanged(final AnimationChanged event) {
        // Player check.
        if (event.getActor() != client.getLocalPlayer()) return;

        // No animations to check.
        if (store.inventory == null || store.equipment == null || triggers_animations == null || charges == ChargesImprovedPlugin.CHARGES_UNKNOWN || triggers_items == null) return;

        // Check all animation triggers.
        animationTriggerLooper: for (final TriggerAnimation trigger_animation : triggers_animations) {
            // Valid animation id check.
            if (trigger_animation.animation_id != event.getActor().getAnimation()) continue;

            // Unallowed items check.
            if (trigger_animation.unallowed_items != null) {
                for (final int item_id : trigger_animation.unallowed_items) {
                    if (store.inventory.contains(item_id) || store.equipment.contains(item_id)) {
                        continue animationTriggerLooper;
                    }
                }
            }

            // Equipped check.
            if (trigger_animation.equipped && !in_equipment) continue;

            // Menu target check.
            if (trigger_animation.menu_target != null && !inMenuTargets(trigger_animation.menu_target)) continue;

            // Menu option check.
            if (trigger_animation.menu_option != null && !inMenuOptions(trigger_animation.menu_option)) continue;

            // Valid trigger, modify charges.
            if (trigger_animation.decrease_charges) {
                decreaseCharges(trigger_animation.charges);
            } else {
                increaseCharges(trigger_animation.charges);
            }

            return;
        }
    }

    public void onGraphicChanged(final GraphicChanged event) {
        // Player check.
        if (event.getActor() != client.getLocalPlayer()) return;

        // No animations to check.
        if (store.equipment == null || triggers_graphics == null || charges == ChargesImprovedPlugin.CHARGES_UNKNOWN || triggers_items == null) return;

        // Check all animation triggers.
        for (final TriggerGraphic trigger_graphic : triggers_graphics) {
            // Valid animation id check.
            if (!event.getActor().hasSpotAnim(trigger_graphic.graphic_id)) continue;

            // Equipped check.
            if (trigger_graphic.equipped && !in_equipment) continue;

            // Valid trigger, modify charges.
            if (trigger_graphic.decrease_charges) {
                decreaseCharges(trigger_graphic.charges);
            } else {
                increaseCharges(trigger_graphic.charges);
            }
        }
    }

    public void onHitsplatApplied(final HitsplatApplied event) {
        if (triggers_hitsplats == null || store.equipment == null) return;
        final Hitsplat hitsplat = event.getHitsplat();

        // Check all hitsplat triggers.
        for (final TriggerHitsplat trigger_hitsplat : triggers_hitsplats) {
            // Player check.
            if (trigger_hitsplat.self && event.getActor() != client.getLocalPlayer()) continue;

            // Enemy check.
            if (!trigger_hitsplat.self && (event.getActor() == client.getLocalPlayer() || hitsplat.isOthers())) continue;

            // Hitsplat type check.
            if (trigger_hitsplat.hitsplat_id != hitsplat.getHitsplatType()) continue;

            // Equipped check.
            if (trigger_hitsplat.equipped && !in_equipment) continue;

            // Non zero check.
            if (trigger_hitsplat.non_zero && hitsplat.getAmount() == 0) continue;

            // Valid hitsplat, modify charges.
            decreaseCharges(trigger_hitsplat.discharges);
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
        if (this.negative_full_charges != null && charges > this.negative_full_charges) return;

        this.charges = charges;
        onChargesUpdated();

        if (config_key != null) {
            setConfiguration(config_key, charges);
        }
    }

    private void decreaseCharges(final int charges) {
        if (this.charges - charges < 0) return;
        setCharges(this.charges - charges);
    }

    public void increaseCharges(final int charges) {
        if (this.charges < 0) return;
        setCharges(this.charges + charges);
    }

    private void setConfiguration(final String key, @Nonnull final String value) {
        configs.setConfiguration(ChargesImprovedConfig.group, key, value);
    }

    private void setConfiguration(final String key, final int value) {
        configs.setConfiguration(ChargesImprovedConfig.group, key, value);
    }

    private void updateInfobox(final int item_id) {
        // Item id.
        this.item_id = item_id;

        // Tooltip.
        updateTooltip();

        // Image.
        setImage(items.getImage(item_id));
        infoboxes.updateInfoBoxImage(this);
    }

    private void updateTooltip() {
        tooltip = items.getItemComposition(item_id).getName() + (needs_to_be_equipped_for_infobox && !in_equipment ? " - Needs to be equipped" : "");
    }

    protected void onChargesUpdated() {
//        chat_messages.queue(QueuedMessage.builder()
//            .type(ChatMessageType.CONSOLE)
//            .runeLiteFormattedMessage(getItemName() + " charges changed: " + charges)
//            .build()
//        );
    }

    private int itemsDifference(final Item[] items_before, final Item[] items_after) {
        final int items_before_count = (int) Arrays.stream(items_before).filter(item -> item.getId() != -1).count();
        final int items_after_count = (int) Arrays.stream(items_after).filter(item -> item.getId() != -1).count();

        return Math.abs(items_before_count - items_after_count);
    }

    private boolean inMenuTargets(final String target) {
        return store.menu_entries.stream().anyMatch(entry -> entry[0].contains(target));
    }

    private boolean inMenuOptions(final String option) {
        return store.menu_entries.stream().anyMatch(entry -> entry[1].equals(option));
    }

    private String getItemName() {
        return items.getItemComposition(item_id).getName();
    }

    public boolean isDeactivated() {
        @Nullable final String configStatus = configs.getConfiguration(ChargesImprovedConfig.group, config_key + "_status");

        if (configStatus == null) {
            return false;
        }

        return config_key != null && configStatus.equals(ChargesImprovedConfig.Status.DEACTIVATED.toString());
    }

    public String getConfigStatusKey() {
        if (config_key == null) return null;

        return config_key + "_status";
    }
}


