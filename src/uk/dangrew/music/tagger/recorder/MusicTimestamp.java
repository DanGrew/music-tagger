package uk.dangrew.music.tagger.recorder;

import java.text.DecimalFormat;
import java.util.Objects;

/**
 * {@link MusicTimestamp} represents a point in the music track that can be displayed.
 */
public class MusicTimestamp {

    private static final DecimalFormat SECONDS_FORMAT = new DecimalFormat("00.###");
    private static final int SECONDS_IN_A_MINUTE = 60;

    private final double seconds;
    private final String displayString;

    public MusicTimestamp(double seconds) {
        this.seconds = seconds;
        this.displayString = format(seconds);
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

    public double seconds() {
        return seconds;
    }

    public String displayString() {
        return displayString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MusicTimestamp that = (MusicTimestamp) o;
        return seconds == that.seconds;
    }

    @Override
    public int hashCode() {
        return Objects.hash(seconds);
    }

    @Override
    public String toString() {
        return displayString();
    }
}
