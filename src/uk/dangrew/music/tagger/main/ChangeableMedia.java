package uk.dangrew.music.tagger.main;

import javafx.beans.property.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import uk.dangrew.kode.friendly.javafx.FriendlyMediaPlayer;

import java.util.Optional;

/**
 * {@link ChangeableMedia} wraps around a {@link MediaPlayer} and allows the {@link Media} to be changed, for
 * example changing the audio file, while preserving registrations for the associated information.
 */
public class ChangeableMedia implements ReadOnlyMedia {

    private final ObjectProperty<Duration> currentTimeProperty;
    private final DoubleProperty rateProperty;
    private final BooleanProperty playingProperty;

    private Optional<FriendlyMediaPlayer> mediaPlayer;

    public ChangeableMedia() {
        this.currentTimeProperty = new SimpleObjectProperty<>(Duration.seconds(0));
        this.rateProperty = new SimpleDoubleProperty(1.0);
        this.playingProperty = new SimpleBooleanProperty(false);
        this.mediaPlayer = Optional.empty();
    }

    public void useMediaFile(String newMedia) {
        changeMedia(new FriendlyMediaPlayer(new Media(newMedia)));
    }

    public void changeMedia(FriendlyMediaPlayer newMediaPlayer) {
        if (mediaPlayer.isPresent()) {
            currentTimeProperty.unbind();
            rateProperty.unbind();
        }

        this.mediaPlayer = Optional.ofNullable(newMediaPlayer);
        if (!mediaPlayer.isPresent()) {
            currentTimeProperty.set(Duration.seconds(0));
            rateProperty.set(1.0);
            return;
        }

        currentTimeProperty.bind(mediaPlayer.get().friendly_currentTimeProperty());
        rateProperty.bind(mediaPlayer.get().friendly_rateProperty());
        playingProperty.set(false);
    }

    public void play() {
        mediaPlayer.ifPresent(FriendlyMediaPlayer::friendly_play);
        playingProperty.set(true);
    }//End Method

    public void pause() {
        mediaPlayer.ifPresent(FriendlyMediaPlayer::friendly_pause);
        playingProperty.set(false);
    }

    public void togglePause() {
        if (playingProperty.get()) {
            pause();
        } else {
            play();
        }
    }

    public void stop() {
        mediaPlayer.ifPresent(FriendlyMediaPlayer::friendly_stop);
        playingProperty.set(false);
    }

    public void seek(Duration duration) {
        mediaPlayer.ifPresent(player -> player.friendly_seek(duration));
    }

    public void setRate(double value) {
        mediaPlayer.ifPresent(player -> player.friendly_setRate(value));
        stop();
    }

    public double rate() {
        return rateProperty.get();
    }

    @Override
    public ReadOnlyDoubleProperty rateProperty() {
        return rateProperty;
    }

    @Override
    public ReadOnlyBooleanProperty playingProperty() {
        return playingProperty;
    }

    public Duration currentTime() {
        return currentTimeProperty.get();
    }

    @Override
    public ReadOnlyObjectProperty<Duration> currentTimeProperty() {
        return currentTimeProperty;
    }
}
