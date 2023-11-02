package tictac7x.charges.item.storage;

public class StorageItem {
    public final int itemId;
    private int quantity;

    public StorageItem(final int itemId, final int quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getItemId() {
        return itemId;
    }

    void setQuantity(final int quantity) {
        this.quantity = quantity;
    }
}
