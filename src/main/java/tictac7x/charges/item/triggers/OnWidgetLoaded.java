package tictac7x.charges.item.triggers;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OnWidgetLoaded extends TriggerBase {
    public final int groupId;
    public final int childId;
    public final int subChildId;
    public final Pattern text;
    public Optional<Consumer<Matcher>> matcherConsumer = Optional.empty();

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

    public OnWidgetLoaded matcherConsumer(final Consumer<Matcher> consumer) {
        this.matcherConsumer = Optional.of(consumer);
        return this;
    }
}
