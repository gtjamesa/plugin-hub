package tictac7x.charges.item.triggers;

public class OnGraphicChanged extends TriggerBase {
    public final int[] graphicId;

    public OnGraphicChanged(final int ...graphicId) {
        this.graphicId = graphicId;
    }
}
