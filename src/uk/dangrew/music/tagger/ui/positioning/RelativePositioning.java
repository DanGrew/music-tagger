package uk.dangrew.music.tagger.ui.positioning;

import javafx.beans.property.ReadOnlyDoubleProperty;

import javax.swing.event.ChangeListener;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * {@link RelativePositioning} is a {@link PortionProvider} that is linked to a {@link ReadOnlyDoubleProperty} that
 * governs the portion the associated item should be placed at.
 */
public class RelativePositioning implements PortionProvider {

    private final Double minimumClamp;
    private final Double maximumClamp;
    private final ReadOnlyDoubleProperty property;
    private final Set<Consumer<Double>> registrations;

    public RelativePositioning(ReadOnlyDoubleProperty property) {
        this(property, null, null);
    }

    public RelativePositioning(
            ReadOnlyDoubleProperty property,
            Double minimumClamp,
            Double maximumClamp
    ) {
        this.property = property;
        this.registrations = new HashSet<>();
        this.minimumClamp = minimumClamp;
        this.maximumClamp = maximumClamp;

        property.addListener((s, o, n) -> notifyRegistrations(n.doubleValue()));
    }

    @Override
    public double getPositioning() {
        return property.get();
    }

    @Override
    public void registerForUpdates(Consumer<Double> handler) {
        registrations.add(handler);
    }

    private void notifyRegistrations(double value){
        double clampedValue = clamp(value);
        registrations.forEach(consumer -> consumer.accept(clampedValue));
    }

    @Override
    public void unregister(Consumer<Double> handler) {
        registrations.remove(handler);
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
