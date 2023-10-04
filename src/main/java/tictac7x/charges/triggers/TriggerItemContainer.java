package tictac7x.charges.triggers;

import net.runelite.api.InventoryID;
import tictac7x.charges.store.InventoryType;

import javax.annotation.Nullable;

public class TriggerItemContainer {
    public final int inventory_id;

    public boolean increase_by_inventory_difference;
    public boolean decrease_by_bank_difference;

    @Nullable public String menu_target;
    @Nullable public String menu_option;
    @Nullable public Integer fixed_charges;

    public TriggerItemContainer(final InventoryType inventory_type) {
        this.inventory_id = inventory_type == InventoryType.INVENTORY ? InventoryID.INVENTORY.getId() : InventoryID.BANK.getId();
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

    public TriggerItemContainer decreaseByBankDifference() {
        this.decrease_by_bank_difference = true;
        return this;
    }
}
