package uk.dangrew.music.tagger.model;

import javafx.beans.property.*;
import javafx.util.Duration;

public interface ReadOnlyMedia {

    public ReadOnlyDoubleProperty rateProperty();

    public ReadOnlyBooleanProperty playingProperty();

    public ReadOnlyObjectProperty<Duration> currentTimeProperty();
}
