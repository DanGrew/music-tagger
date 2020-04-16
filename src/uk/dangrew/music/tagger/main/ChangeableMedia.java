package uk.dangrew.music.tagger.main;

import javafx.beans.property.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import uk.dangrew.kode.friendly.javafx.FriendlyMediaPlayer;

import java.util.Optional;

/**
 * {@link ChangeableMedia} wraps around a {@link MediaPlayer} and allows the {@link Media} to be changed, for example
 * changing the audio file, while preserving registrations for the associated information.
 */
public class ChangeableMedia {

    private final ObjectProperty<Duration> currentTimeProperty;
    private final DoubleProperty rateProperty;

    private Optional<FriendlyMediaPlayer> mediaPlayer;

    public ChangeableMedia() {
        this.currentTimeProperty = new SimpleObjectProperty<>();
        this.rateProperty = new SimpleDoubleProperty(1.0);

        this.mediaPlayer = Optional.empty();
    }

    public void changeMedia(FriendlyMediaPlayer newMediaPlayer) {
        if (mediaPlayer.isPresent()) {
            currentTimeProperty.unbind();
            rateProperty.unbind();
        }

        this.mediaPlayer = Optional.ofNullable(newMediaPlayer);
        if (!mediaPlayer.isPresent()) {
            currentTimeProperty.set(null);
            rateProperty.set(1.0);
            return;
        }

        currentTimeProperty.bind(mediaPlayer.get().friendly_currentTimeProperty());
        rateProperty.bind(mediaPlayer.get().friendly_rateProperty());
    }

    public void play() {
        mediaPlayer.ifPresent(FriendlyMediaPlayer::friendly_play);
    }//End Method

    public void pause() {
        mediaPlayer.ifPresent(FriendlyMediaPlayer::friendly_pause);
    }

    public void stop() {
        mediaPlayer.ifPresent(FriendlyMediaPlayer::friendly_stop);
    }

    public void seek(Duration duration) {
        mediaPlayer.ifPresent(player -> player.friendly_seek(duration));
    }

    public void setRate(double value) {
        mediaPlayer.ifPresent(player -> player.friendly_setRate(value));
    }

    public Duration currentTime() {
        return currentTimeProperty.get();
    }

    public ReadOnlyObjectProperty<Duration> currentTimeProperty() {
        return currentTimeProperty;
    }

    public double rate() {
        return rateProperty.get();
    }

    public ReadOnlyDoubleProperty rateProperty() {
        return rateProperty;
    }

}
