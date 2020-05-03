package uk.dangrew.music.tagger.model;

import javafx.beans.property.*;
import javafx.util.Duration;

public interface ReadOnlyMedia {

    public ReadOnlyObjectProperty<Duration> currentTimeProperty();

    public ReadOnlyObjectProperty<Duration> durationProperty();

    public ReadOnlyDoubleProperty rateProperty();

    public ReadOnlyBooleanProperty playingProperty();
}
