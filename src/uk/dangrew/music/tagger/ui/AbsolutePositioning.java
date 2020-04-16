package uk.dangrew.music.tagger.ui;

import java.util.function.Consumer;

/**
 * Provides a positioning relative to the window it is placed in as a portion of a dimension.
 */
public class AbsolutePositioning implements PortionProvider {

    private final double portion;

    public AbsolutePositioning(double portion){
        if ( portion < 0.0 || portion > 1.0 ) {
            throw new IllegalArgumentException("Invalid portion.");
        }
        this.portion = portion;
    }

    @Override
    public double getPositioning() {
        return portion;
    }

    @Override
    public void registerForUpdates(Consumer<Double> handler) {
        //do nothing.
    }
}
