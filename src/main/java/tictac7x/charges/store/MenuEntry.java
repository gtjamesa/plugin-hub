package tictac7x.charges.store;

public class MenuEntry {
    public final String target;
    public final String option;
    public final int impostorId;

    public MenuEntry(final String target, final String option, final int impostorId) {
        this.target = target;
        this.option = option;
        this.impostorId = impostorId;
    }
}
