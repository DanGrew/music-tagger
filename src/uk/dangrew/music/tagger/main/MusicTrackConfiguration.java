package uk.dangrew.music.tagger.main;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * {@link MusicTrackConfiguration} provides the system configuration that enables the model to communicate with the ui.
 */
public class MusicTrackConfiguration {

    private final DoubleProperty currentTimeProperty;
    private final DoubleProperty currentPositionProperty;
    private final DoubleProperty scalePositionIntervalProperty;
    private final DoubleProperty scaleTimeIntervalProperty;

    public MusicTrackConfiguration() {
        this.currentPositionProperty = new SimpleDoubleProperty(0.2);
        this.currentTimeProperty = new SimpleDoubleProperty(0.0);
        this.scalePositionIntervalProperty = new SimpleDoubleProperty(0.05);
        this.scaleTimeIntervalProperty = new SimpleDoubleProperty(5);
    }

    public DoubleProperty currentPositionProperty() {
        return currentPositionProperty;
    }

    public DoubleProperty currentTimeProperty() {
        return currentTimeProperty;
    }

    public DoubleProperty scalePositionIntervalProperty() {
        return scalePositionIntervalProperty;
    }

    public DoubleProperty scaleTimeIntervalProperty() {
        return scaleTimeIntervalProperty;
    }
}
