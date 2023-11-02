package tictac7x.charges.item.triggers;

import java.util.Optional;
import java.util.regex.Pattern;

public class OnWidgetLoaded extends TriggerBase {
    public final int groupId;
    public final int childId;
    public final int subChildId;
    public final Pattern text;

    public Optional<Boolean> setDynamically = Optional.empty();

    public OnWidgetLoaded setDynamically() {
        this.setDynamically = Optional.of(true);
        return this;
    }

    public OnWidgetLoaded(final int groupId, final int childId, final int subChildId, final String text) {
        this.groupId = groupId;
        this.childId = childId;
        this.subChildId = subChildId;
        this.text = Pattern.compile(text);
    }
}
