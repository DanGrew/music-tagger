package uk.dangrew.music.tagger.ui;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * {@link PortionProvider} defines a method of providing a portion of a dimension as a portion of the actual value.
 */
public interface PortionProvider {

    public double getPositioning();

    public void registerForUpdates(Consumer<Double> handler);
}
