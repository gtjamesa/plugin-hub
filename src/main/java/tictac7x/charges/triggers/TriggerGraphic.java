package tictac7x.charges.triggers;

public class TriggerGraphic {
    public final int graphic_id;

    public int charges;
    public boolean decrease_charges;
    public boolean equipped;

    public TriggerGraphic(final int graphic_id) {
        this.graphic_id = graphic_id;
    }

    public TriggerGraphic decreaseCharges(final int decharges) {
        this.charges = decharges;
        this.decrease_charges = true;
        return this;
    }

    public TriggerGraphic equipped() {
        this.equipped = true;
        return this;
    }
}
