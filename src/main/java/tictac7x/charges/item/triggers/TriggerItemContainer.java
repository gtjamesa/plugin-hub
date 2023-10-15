package tictac7x.charges.item.triggers;

import net.runelite.api.InventoryID;
import tictac7x.charges.store.ItemContainerType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TriggerItemContainer {
    public final int inventory_id;

    public boolean increase_by_inventory_difference;
    public boolean decrease_by_inventory_difference;
    public boolean decrease_by_bank_difference;
    public List<Integer> specificItems = new ArrayList<>();
    public Optional<Integer> decreaseCharges = Optional.empty();

    @Nullable public String menu_target;
    @Nullable public String menu_option;
    @Nullable public Integer fixed_charges;

    public TriggerItemContainer(final ItemContainerType inventory_type) {
        this.inventory_id = inventory_type == ItemContainerType.INVENTORY ? InventoryID.INVENTORY.getId() : InventoryID.BANK.getId();
    }

    public TriggerItemContainer menuTarget(final String menu_target) {
        this.menu_target = menu_target;
        return this;
    }

    public TriggerItemContainer menuOption(final String menu_option) {
        this.menu_option = menu_option;
        return this;
    }

    public TriggerItemContainer fixedCharges(final int fixed_charges) {
        this.fixed_charges = fixed_charges;
        return this;
    }

    public TriggerItemContainer increaseByInventoryDifference() {
        this.increase_by_inventory_difference = true;
        return this;
    }

    public TriggerItemContainer decreaseByInventoryDifference() {
        this.decrease_by_inventory_difference = true;
        return this;
    }

    public TriggerItemContainer decreaseByBankDifference() {
        this.decrease_by_bank_difference = true;
        return this;
    }

    public TriggerItemContainer specificItem(final int ...itemIds) {
        for (final int itemId : itemIds) {
            this.specificItems.add(itemId);
        }
        return this;
    }

    public TriggerItemContainer decreaseCharges(final int charges) {
        this.decreaseCharges = Optional.of(charges);
        return this;
    }
}
