package uk.dangrew.music.tagger.ui.components.tagging;

import java.util.function.Consumer;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import uk.dangrew.kode.javafx.keyboard.KeyBoardCapture;
import uk.dangrew.music.tagger.main.MusicTagger;
import uk.dangrew.music.tagger.main.MusicTrackState;
import uk.dangrew.music.tagger.model.MusicTimestamp;
import uk.dangrew.music.tagger.model.MusicTrack;

public class TagCapture {

    private final MusicTrack musicTrack;
    private final MusicTrackState configuration;
    private final KeyBoardCapture keyBoardCapture;
    private final Consumer<KeyEvent> keyBoardHandler;

    public TagCapture(MusicTrack musicTrack, MusicTrackState musicTrackState) {
        this(MusicTagger.keyBoard(), musicTrack, musicTrackState);
    }

    TagCapture(KeyBoardCapture keyBoardCapture, MusicTrack musicTrack, MusicTrackState musicTrackState){
        this.musicTrack = musicTrack;
        this.configuration = musicTrackState;
        this.keyBoardCapture = keyBoardCapture;
        this.keyBoardHandler = this::handleKeyBoardEvent;

        musicTrackState.recordingProperty().addListener((s, o, n) -> {
            toggleRecording(n);
        });
    }

    private void toggleRecording(boolean isRecording){
        if ( !isRecording ){
            keyBoardCapture.stopCapture(KeyEvent.KEY_RELEASED, keyBoardHandler);
            keyBoardCapture.stopCapture(KeyEvent.KEY_TYPED, keyBoardHandler);
        } else {
            keyBoardCapture.capture(KeyEvent.KEY_RELEASED, keyBoardHandler);
            keyBoardCapture.capture(KeyEvent.KEY_TYPED, keyBoardHandler);
        }
    }

    private void handleKeyBoardEvent(KeyEvent event){
        if ( event.getEventType() == KeyEvent.KEY_PRESSED ) {
            return;
        }

        if ( event.getCode() == KeyCode.SPACE ) {
            musicTrack.tag(new MusicTimestamp(configuration.currentTimeProperty().get()));
        }
    }
}
