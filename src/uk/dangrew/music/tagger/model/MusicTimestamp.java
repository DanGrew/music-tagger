package uk.dangrew.music.tagger.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.util.Duration;

import java.text.DecimalFormat;
import java.util.Objects;

/**
 * {@link MusicTimestamp} represents a point in the music track that can be displayed.
 */
public class MusicTimestamp {

    private static final DecimalFormat SECONDS_FORMAT = new DecimalFormat("00.###");
    private static final int SECONDS_IN_A_MINUTE = 60;

    private final DoubleProperty seconds;

    public MusicTimestamp(double seconds) {
        this.seconds = new SimpleDoubleProperty(seconds);
    }

    public static String format(double seconds){
        int minutes = (int)(seconds / SECONDS_IN_A_MINUTE);
        double remainderSeconds = seconds - (minutes * SECONDS_IN_A_MINUTE);

        int roundedSeconds = ((int)remainderSeconds * 10);
        if ( roundedSeconds >= 0 ) {
            return minutes + ":" + SECONDS_FORMAT.format(Math.abs(remainderSeconds));
        } else {
            return "-" + minutes + ":" + SECONDS_FORMAT.format(Math.abs(remainderSeconds));
        }
    }

    public static String format(Duration duration){
        if ( duration == null ) {
            return format(0.0);
        } else {
            return format(duration.toSeconds());
        }
    }

    public double seconds(){
        return secondsProperty().get();
    }

    public DoubleProperty secondsProperty() {
        return seconds;
    }

    public String displayString() {
        return format(seconds.get());
    }

    @Override
    public String toString() {
        return displayString();
    }

}
