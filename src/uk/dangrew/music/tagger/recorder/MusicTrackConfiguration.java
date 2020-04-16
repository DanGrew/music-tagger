package uk.dangrew.music.tagger.recorder;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * {@link MusicTrackConfiguration} provides the system configuration that enables the model to communicate with the ui.
 */
public class MusicTrackConfiguration {

    private final DoubleProperty currentTimeProperty;
    private final DoubleProperty currentPositionProperty;

    public MusicTrackConfiguration() {
        this.currentPositionProperty = new SimpleDoubleProperty(0.2);
        this.currentTimeProperty = new SimpleDoubleProperty(0.0);
    }

    public DoubleProperty currentPositionProperty() {
        return currentPositionProperty;
    }

    public DoubleProperty currentTimeProperty() {
        return currentTimeProperty;
    }
}
