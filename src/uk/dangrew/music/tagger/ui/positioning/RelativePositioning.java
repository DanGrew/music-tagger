package uk.dangrew.music.tagger.ui.positioning;

import javafx.beans.property.ReadOnlyDoubleProperty;

import java.util.function.Consumer;

/**
 * {@link RelativePositioning} is a {@link PortionProvider} that is linked to a {@link ReadOnlyDoubleProperty} that
 * governs the portion the associated item should be placed at.
 */
public class RelativePositioning implements PortionProvider {

    private final Double minimumClamp;
    private final Double maximumClamp;
    private final ReadOnlyDoubleProperty property;

    public RelativePositioning(ReadOnlyDoubleProperty property) {
        this(property, null, null);
    }

    public RelativePositioning(
            ReadOnlyDoubleProperty property,
            Double minimumClamp,
            Double maximumClamp
    ) {
        this.property = property;
        this.minimumClamp = minimumClamp;
        this.maximumClamp = maximumClamp;
    }

    @Override
    public double getPositioning() {
        return property.get();
    }

    @Override
    public void registerForUpdates(Consumer<Double> handler) {
        property.addListener((s, o, n) -> handler.accept(clamp(n.doubleValue())));
    }

    private double clamp(double value) {
        if (minimumClamp != null) {
            value = Math.max(value, minimumClamp);
        }
        if (maximumClamp != null) {
            value = Math.min(value, maximumClamp);
        }

        return value;
    }
}
