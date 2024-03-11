package tictac7x.charges.item.triggers;

public class OnAnimationChanged extends TriggerBase {
    public final int[] animationId;

    public OnAnimationChanged(final int ...animationId) {
        this.animationId = animationId;
    }
}
