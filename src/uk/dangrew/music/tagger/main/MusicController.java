package uk.dangrew.music.tagger.main;

import javafx.util.Duration;

/**
 * {@link MusicController} provides an intermediate between ui and model for controlling the {@link javafx.scene.media.Media}.
 */
public class MusicController {

    private static final Duration SKIP_DURATION_SECONDS = Duration.seconds(30);
    private static final double RATE_INCREMENT = 0.1;

    private final ChangeableMedia musicTrack;

    public MusicController(ChangeableMedia musicTrack){
        this.musicTrack = musicTrack;
    }

    public void play(){
        musicTrack.play();
    }

    public void pause(){
        musicTrack.pause();
    }

    public void stop(){
        musicTrack.stop();
    }

    public void plus30(){
        musicTrack.seek(musicTrack.currentTime().add(SKIP_DURATION_SECONDS));
    }

    public void minus30(){
        musicTrack.seek(musicTrack.currentTime().subtract(SKIP_DURATION_SECONDS));
    }

    public void speedUp(){
        musicTrack.setRate(musicTrack.rate() + RATE_INCREMENT);
    }

    public void slowDown(){
        musicTrack.setRate(musicTrack.rate() - RATE_INCREMENT);
    }
}

