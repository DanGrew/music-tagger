package uk.dangrew.music.tagger.recorder;

import javafx.util.Duration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.dangrew.kode.TestCommon;

import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.*;
import static org.mockito.Matchers.doubleThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MusicControllerTest {

    @Mock private ChangeableMedia musicTrack;
    private MusicController systemUnderTest;

    @Before public void initialiseSystemUnderTest(){
        initMocks(this);
        systemUnderTest = new MusicController(musicTrack);
    }

    @Test
    public void shouldPlay(){
        systemUnderTest.play();
        verify(musicTrack).play();
    }

    @Test
    public void shouldPause(){
        systemUnderTest.pause();
        verify(musicTrack).pause();
    }

    @Test
    public void shouldStop(){
        systemUnderTest.stop();
        verify(musicTrack).stop();
    }

    @Test
    public void shouldSkipAhead(){
        when(musicTrack.currentTime()).thenReturn(Duration.seconds(31));

        systemUnderTest.plus30();
        verify(musicTrack).seek(Duration.seconds(61));
    }

    @Test
    public void shouldSkipBack(){
        when(musicTrack.currentTime()).thenReturn(Duration.seconds(31));

        systemUnderTest.minus30();
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

}