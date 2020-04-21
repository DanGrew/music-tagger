package uk.dangrew.music.tagger.main;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.util.Duration;

public interface ReadOnlyMedia {

    public ReadOnlyObjectProperty<Duration> currentTimeProperty();

    public ReadOnlyDoubleProperty rateProperty();

    public ReadOnlyBooleanProperty playingProperty();
}
