package uk.dangrew.music.tagger.ui.positioning;

import java.util.function.Consumer;

/**
 * {@link PortionProvider} defines a method of providing a portion of a dimension as a portion of the actual value.
 */
public interface PortionProvider {

    public double getPositioning();

    public void registerForUpdates(Consumer<Double> handler);

    public void unregister(Consumer<Double> handler);
}
