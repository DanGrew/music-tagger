package uk.dangrew.music.tagger.main;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * {@link MusicTrackState} provides the system configuration that enables the model to communicate with the ui.
 */
public class MusicTrackState {

    private final DoubleProperty currentTimeProperty;
    private final DoubleProperty currentPositionProperty;
    private final DoubleProperty scalePositionIntervalProperty;
    private final DoubleProperty scaleTimeIntervalProperty;
    private final BooleanProperty recordingProperty;

    public MusicTrackState() {
        this.currentPositionProperty = new SimpleDoubleProperty(0.4);
        this.currentTimeProperty = new SimpleDoubleProperty(0.0);
        this.scalePositionIntervalProperty = new SimpleDoubleProperty(0.1);
        this.scaleTimeIntervalProperty = new SimpleDoubleProperty(1);
        this.recordingProperty = new SimpleBooleanProperty(false);
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

    public BooleanProperty recordingProperty() {
        return recordingProperty;
    }
}
