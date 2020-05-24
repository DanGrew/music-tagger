package uk.dangrew.music.tagger.ui.components;

import javafx.util.Duration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.dangrew.kode.TestCommon;
import uk.dangrew.music.tagger.main.MusicTrackState;
import uk.dangrew.music.tagger.model.ChangeableMedia;
import uk.dangrew.music.tagger.ui.components.MusicController;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.doubleThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MusicControllerTest {

    private MusicTrackState musicTrackState;
    @Mock private ChangeableMedia musicTrack;
    private MusicController systemUnderTest;

    @Before public void initialiseSystemUnderTest(){
        initMocks(this);
        musicTrackState = new MusicTrackState();
        systemUnderTest = new MusicController(musicTrack, musicTrackState);
    }

    @Test
    public void shouldPlay(){
        systemUnderTest.play();
        verify(musicTrack).play();
    }

    @Test
    public void shouldPause(){
        systemUnderTest.togglePause();
        verify(musicTrack).togglePause();
    }

    @Test
    public void shouldStop(){
        systemUnderTest.stop();
        verify(musicTrack).stop();
    }

    @Test
    public void shouldSkipAhead(){
        when(musicTrack.currentTime()).thenReturn(Duration.seconds(31));

        systemUnderTest.plus(30);
        verify(musicTrack).seek(Duration.seconds(61));
    }

    @Test
    public void shouldSkipBack(){
        when(musicTrack.currentTime()).thenReturn(Duration.seconds(31));

        systemUnderTest.minus(30);
        verify(musicTrack).seek(Duration.seconds(1));
    }

    @Test
    public void shouldSpeedUp(){
        when(musicTrack.rate()).thenReturn(2.2);

        systemUnderTest.speedUp();
        verify(musicTrack).setRate(doubleThat(closeTo(2.3, TestCommon.precision())));
    }

    @Test
    public void shouldSlowDown(){
        when(musicTrack.rate()).thenReturn(2.2);

        systemUnderTest.slowDown();
        verify(musicTrack).setRate(doubleThat(closeTo(2.1, TestCommon.precision())));
    }

    @Test public void shouldToggleRecording(){
        systemUnderTest.toggleRecording();
        assertThat(musicTrackState.recordingProperty().get(), is(true));
        systemUnderTest.toggleRecording();
        assertThat(musicTrackState.recordingProperty().get(), is(false));
    }
}