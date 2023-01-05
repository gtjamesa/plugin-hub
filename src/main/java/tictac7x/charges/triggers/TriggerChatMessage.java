package tictac7x.charges.triggers;

public class TriggerChatMessage {
    public final String message;
    public final int group_or_charges;

    public TriggerChatMessage(final String message, final int group_or_charges) {
        this.message = message;
        this.group_or_charges = group_or_charges;
    }
}
