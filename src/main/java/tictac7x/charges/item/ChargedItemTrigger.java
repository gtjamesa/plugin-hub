package tictac7x.charges.item;

import tictac7x.charges.store.ItemContainerType;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChargedItemTrigger {
    // Trigger types.
    public Optional<Pattern> onChatMessage = Optional.empty();
    public Optional<ItemContainerType> onItemContainerChanged = Optional.empty();
    public Optional<int[]> onItemDespawned = Optional.empty();

    // Checks.
    public Optional<Integer> specificItem = Optional.empty();
    public Optional<String> onMenuOption = Optional.empty();

    // Actions.
    public Optional<Integer> increaseCharges = Optional.empty();
    public Optional<Integer> decreaseCharges = Optional.empty();
    public Optional<Consumer<Matcher>> matcherConsumer = Optional.empty();

    // Storage.
    public Optional<Boolean> emptyStorage = Optional.empty();
    public Optional<Boolean> fillStorageFromInventory = Optional.empty();
    public Optional<Boolean> pickUpToStorage = Optional.empty();

    public ChargedItemTrigger increaseCharges(final int charges) {
        this.increaseCharges = Optional.of(charges);
        return this;
    }

    public ChargedItemTrigger decreaseCharges(final int charges) {
        this.decreaseCharges = Optional.of(charges);
        return this;
    }

    public ChargedItemTrigger onChatMessage(final String message) {
        this.onChatMessage = Optional.of(Pattern.compile(message));
        return this;
    }

    public ChargedItemTrigger emptyStorage() {
        this.emptyStorage = Optional.of(true);
        return this;
    }

    public ChargedItemTrigger fillStorageFromInventory() {
        this.fillStorageFromInventory = Optional.of(true);
        return this;
    }

    public ChargedItemTrigger consumer(final Consumer<Matcher> consumer) {
        this.matcherConsumer = Optional.of(consumer);
        return this;
    }

    public ChargedItemTrigger specificItem(final int itemId) {
        this.specificItem = Optional.of(itemId);
        return this;
    }

    public ChargedItemTrigger onItemContainerChanged(final ItemContainerType itemContainerType) {
        this.onItemContainerChanged = Optional.of(itemContainerType);
        return this;
    }

    public ChargedItemTrigger onMenuOption(final String option) {
        this.onMenuOption = Optional.of(option);
        return this;
    }

    public ChargedItemTrigger onItemDespawned(final int ...itemIds) {
        this.onItemDespawned = Optional.of(itemIds);
        return this;
    }

    public ChargedItemTrigger pickUpToStorage() {
        this.pickUpToStorage = Optional.of(true);
        return this;
    }
}
