package uk.dangrew.music.tagger.main;

import javafx.util.Duration;

/**
 * {@link MusicController} provides an intermediate between ui and model for controlling the {@link javafx.scene.media.Media}.
 */
public class MusicController {

    private static final Duration SKIP_DURATION_SECONDS = Duration.seconds(30);
    private static final double RATE_INCREMENT = 0.1;

    private final ChangeableMedia media;

    public MusicController(ChangeableMedia media){
        this.media = media;
    }

    public ReadOnlyMedia getMedia(){
        return media;
    }

    public void play(){
        media.play();
    }

    public void togglePause(){
        media.togglePause();
    }

    public void stop(){
        media.stop();
    }

    public void plus30(){
        media.seek(media.currentTime().add(SKIP_DURATION_SECONDS));
    }

    public void minus30(){
        media.seek(media.currentTime().subtract(SKIP_DURATION_SECONDS));
    }

    public void speedUp(){
        media.setRate(media.rate() + RATE_INCREMENT);
    }

    public void slowDown(){
        media.setRate(media.rate() - RATE_INCREMENT);
    }
}

