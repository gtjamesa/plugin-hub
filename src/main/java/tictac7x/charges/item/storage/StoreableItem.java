package tictac7x.charges.item.storage;

public class StoreableItem {
    public final int itemId;
    public final String name;
    public final int quantity;

    public StoreableItem(final int itemId, final String name) {
        this.itemId = itemId;
        this.name = name;
        this.quantity = 1;
    }

    public StoreableItem(final int itemId, final String name, final int quantity) {
        this.itemId = itemId;
        this.name = name;
        this.quantity = quantity;
    }
}
