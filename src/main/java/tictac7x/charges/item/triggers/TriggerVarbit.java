package tictac7x.charges.item.triggers;

public class TriggerVarbit {
    public final int varbitId;
    public final int varbitValue;

    public boolean activate;
    public boolean deactivate;

    public TriggerVarbit(final int varbitId, final int varbitValue) {
        this.varbitId = varbitId;
        this.varbitValue = varbitValue;
    }

    public TriggerVarbit activate() {
        this.activate = true;
        return this;
    }

    public TriggerVarbit deactivate() {
        this.deactivate = true;
        return this;
    }
}
