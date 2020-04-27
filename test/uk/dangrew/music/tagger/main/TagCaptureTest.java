package uk.dangrew.music.tagger.main;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import uk.dangrew.music.tagger.utility.KeyEventCreator;

import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class TagCaptureTest {

    @Mock private KeyBoardCapture keyBoardCapture;
    private MusicTrack musicTrack;
    private MusicTrackState musicTrackState;
    private TagCapture systemUnderTest;

    @Before
    public void initialiseSystemUnderTest() {
        initMocks(this);
        musicTrack = new MusicTrack();
        musicTrackState = new MusicTrackState();
        systemUnderTest = new TagCapture(keyBoardCapture, musicTrack, musicTrackState);
    }

    @Test
    public void shouldTagTrackOnKeyPress() {
        ArgumentCaptor<Consumer> captor = ArgumentCaptor.forClass(Consumer.class);

        musicTrackState.recordingProperty().set(true);
        verify(keyBoardCapture).capture(eq(KeyEvent.KEY_RELEASED), captor.capture());
        verify(keyBoardCapture).capture(eq(KeyEvent.KEY_TYPED), captor.capture());

        KeyEvent typedSpace = new KeyEventCreator().create(KeyEvent.KEY_RELEASED, KeyCode.SPACE);
        musicTrackState.currentTimeProperty().set(61.0);

        captor.getValue().accept(typedSpace);
        assertThat(musicTrack.getTags().get(0).getMusicTimestamp().seconds(), is(61.0));
        assertThat(musicTrack.getTags(), hasSize(1));

        musicTrackState.currentTimeProperty().set(63.0);
        musicTrackState.recordingProperty().set(false);

        verify(keyBoardCapture).stopCapture(eq(KeyEvent.KEY_RELEASED), captor.capture());
        verify(keyBoardCapture).stopCapture(eq(KeyEvent.KEY_TYPED), captor.capture());
    }

}