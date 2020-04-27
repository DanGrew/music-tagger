package uk.dangrew.music.tagger.main;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.util.Duration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.dangrew.kode.friendly.javafx.FriendlyMediaPlayer;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ChangeableMediaTest {

    private ObjectProperty<Duration> currentTimeMp1;
    private DoubleProperty rateMp1;

    private ObjectProperty<Duration> currentTimeMp2;
    private DoubleProperty rateMp2;

    @Mock private FriendlyMediaPlayer mediaPlayer1;
    @Mock private FriendlyMediaPlayer mediaPlayer2;
    private ChangeableMedia systemUnderTest;

    @Before
    public void initialiseSystemUnderTest() {
        initMocks(this);

        currentTimeMp1 = new SimpleObjectProperty<>();
        rateMp1 = new SimpleDoubleProperty();
        currentTimeMp2 = new SimpleObjectProperty<>();
        rateMp2 = new SimpleDoubleProperty();

        when(mediaPlayer1.friendly_currentTimeProperty()).thenReturn(currentTimeMp1);
        when(mediaPlayer1.friendly_rateProperty()).thenReturn(rateMp1);
        when(mediaPlayer2.friendly_currentTimeProperty()).thenReturn(currentTimeMp2);
        when(mediaPlayer2.friendly_rateProperty()).thenReturn(rateMp2);

        systemUnderTest = new ChangeableMedia();
    }

    @Test
    public void shouldExecuteInstructions(){
        systemUnderTest.play();
        verify(mediaPlayer1, never()).friendly_play();
        systemUnderTest.pause();
        verify(mediaPlayer1, never()).friendly_pause();
        systemUnderTest.stop();
        verify(mediaPlayer1, never()).friendly_stop();
        systemUnderTest.seek(Duration.ONE);
        verify(mediaPlayer1, never()).friendly_seek(any());
        systemUnderTest.setRate(4.0);
        verify(mediaPlayer1, never()).friendly_setRate(anyDouble());

        systemUnderTest.play();
        verify(mediaPlayer2, never()).friendly_play();
        systemUnderTest.pause();
        verify(mediaPlayer2, never()).friendly_pause();
        systemUnderTest.stop();
        verify(mediaPlayer2, never()).friendly_stop();
        systemUnderTest.seek(Duration.ONE);
        verify(mediaPlayer2, never()).friendly_seek(any());
        systemUnderTest.setRate(4.0);
        verify(mediaPlayer2, never()).friendly_setRate(anyDouble());

        systemUnderTest.changeMedia(mediaPlayer1);
       
        systemUnderTest.play();
        verify(mediaPlayer1).friendly_play();
        systemUnderTest.pause();
        verify(mediaPlayer1).friendly_pause();
        systemUnderTest.stop();
        verify(mediaPlayer1).friendly_stop();
        systemUnderTest.seek(Duration.ONE);
        verify(mediaPlayer1).friendly_seek(Duration.ONE);
        systemUnderTest.setRate(4.0);
        verify(mediaPlayer1).friendly_setRate(4.0);
        verify(mediaPlayer1, times(2)).friendly_stop();

        systemUnderTest.changeMedia(mediaPlayer2);

        systemUnderTest.play();
        verify(mediaPlayer1).friendly_play();
        verify(mediaPlayer2).friendly_play();
        systemUnderTest.pause();
        verify(mediaPlayer1).friendly_pause();
        verify(mediaPlayer2).friendly_pause();
        systemUnderTest.stop();
        verify(mediaPlayer1, times(2)).friendly_stop();
        verify(mediaPlayer2).friendly_stop();
        systemUnderTest.seek(Duration.ONE);
        verify(mediaPlayer1).friendly_seek(Duration.ONE);
        verify(mediaPlayer2).friendly_seek(Duration.ONE);
        systemUnderTest.setRate(4.0);
        verify(mediaPlayer1).friendly_setRate(4.0);
        verify(mediaPlayer1, times(2)).friendly_stop();
        verify(mediaPlayer2).friendly_setRate(4.0);
        verify(mediaPlayer2, times(2)).friendly_stop();
    }

    @Test public void shouldProvidecurrentTimeProperty(){
        assertThat(systemUnderTest.currentTime(), is(Duration.ZERO));
        assertThat(systemUnderTest.currentTimeProperty(), is(notNullValue()));

        systemUnderTest.changeMedia(mediaPlayer1);

        currentTimeMp1.set(Duration.seconds(10));
        assertThat(systemUnderTest.currentTime(), is(Duration.seconds(10)));
        assertThat(systemUnderTest.currentTimeProperty().get(), is(Duration.seconds(10)));

        currentTimeMp2.set(Duration.seconds(20));
        assertThat(systemUnderTest.currentTime(), is(Duration.seconds(10)));
        assertThat(systemUnderTest.currentTimeProperty().get(), is(Duration.seconds(10)));

        systemUnderTest.changeMedia(null);

        currentTimeMp1.set(Duration.seconds(30));
        assertThat(systemUnderTest.currentTime(), is(Duration.ZERO));
        assertThat(systemUnderTest.currentTimeProperty().get(), is(Duration.ZERO));

        systemUnderTest.changeMedia(mediaPlayer2);
        assertThat(systemUnderTest.currentTime(), is(Duration.seconds(20)));
        assertThat(systemUnderTest.currentTimeProperty().get(), is(Duration.seconds(20)));

        currentTimeMp1.set(Duration.seconds(40));
        assertThat(systemUnderTest.currentTime(), is(Duration.seconds(20)));
        assertThat(systemUnderTest.currentTimeProperty().get(), is(Duration.seconds(20)));
    }

    @Test public void shouldRespondToAudioDuration(){
        ChangeListener<Duration> listener = mock(ChangeListener.class);
        systemUnderTest.currentTimeProperty().addListener(listener);

        systemUnderTest.changeMedia(mediaPlayer1);
        currentTimeMp1.set(Duration.seconds(23));

        verify(listener).changed(any(), any(), eq(Duration.seconds(23)));
    }

    @Test public void shouldProvideRateProperty(){
        assertThat(systemUnderTest.rate(), is(1.0));
        assertThat(systemUnderTest.rateProperty(), is(notNullValue()));

        systemUnderTest.changeMedia(mediaPlayer1);

        rateMp1.set(1.0);
        assertThat(systemUnderTest.rate(), is(1.0));
        assertThat(systemUnderTest.rateProperty().get(), is(1.0));

        rateMp2.set(2.0);
        assertThat(systemUnderTest.rate(), is(1.0));
        assertThat(systemUnderTest.rateProperty().get(), is(1.0));

        systemUnderTest.changeMedia(null);

        assertThat(systemUnderTest.rate(), is(1.0));
        assertThat(systemUnderTest.rateProperty().get(), is(1.0));

        rateMp1.set(3.0);
        assertThat(systemUnderTest.rate(), is(1.0));
        assertThat(systemUnderTest.rateProperty().get(), is(1.0));

        systemUnderTest.changeMedia(mediaPlayer2);
        assertThat(systemUnderTest.rate(), is(2.0));
        assertThat(systemUnderTest.rateProperty().get(), is(2.0));

        rateMp1.set(4.0);
        assertThat(systemUnderTest.rate(), is(2.0));
        assertThat(systemUnderTest.rateProperty().get(), is(2.0));
    }

    @Test public void shouldRespondToRate(){
        ChangeListener<Number> listener = mock(ChangeListener.class);
        systemUnderTest.rateProperty().addListener(listener);

        systemUnderTest.changeMedia(mediaPlayer1);
        rateMp1.set(2.1);

        verify(listener).changed(any(), any(), eq(Double.valueOf(2.1)));
    }

    @Test public void shouldProvidePlayingProperty(){
        assertThat(systemUnderTest.playingProperty().get(), is(false));

        systemUnderTest.changeMedia(mediaPlayer1);

        systemUnderTest.play();
        assertThat(systemUnderTest.playingProperty().get(), is(true));

        systemUnderTest.changeMedia(mediaPlayer2);
        assertThat(systemUnderTest.playingProperty().get(), is(false));

        systemUnderTest.play();
        assertThat(systemUnderTest.playingProperty().get(), is(true));
    }

    @Test public void shouldRespondToPlaying(){
        ChangeListener<Boolean> listener = mock(ChangeListener.class);
        systemUnderTest.playingProperty().addListener(listener);

        systemUnderTest.changeMedia(mediaPlayer1);
        systemUnderTest.play();

        verify(listener).changed(any(), any(), eq(true));
    }

    @Test public void shouldControlPlayingProperty(){
        assertThat(systemUnderTest.playingProperty().get(), is(false));
        systemUnderTest.play();
        assertThat(systemUnderTest.playingProperty().get(), is(true));
        systemUnderTest.stop();
        assertThat(systemUnderTest.playingProperty().get(), is(false));
        systemUnderTest.togglePause();
        assertThat(systemUnderTest.playingProperty().get(), is(true));
        systemUnderTest.togglePause();
        assertThat(systemUnderTest.playingProperty().get(), is(false));
        systemUnderTest.togglePause();
        assertThat(systemUnderTest.playingProperty().get(), is(true));
        systemUnderTest.pause();
        assertThat(systemUnderTest.playingProperty().get(), is(false));
    }

    @Test
    public void shouldRecordProgressAtRegularSpeed() {
        systemUnderTest.changeMedia(mediaPlayer1);

        changeDurationTo(10);
        assertThatCurrentTimeIs(10);
        changeDurationTo(20);
        assertThatCurrentTimeIs(20);
        changeDurationTo(20.01);
        assertThatCurrentTimeIs(20.01);

        changeDurationTo(15);
        assertThatCurrentTimeIs(15);
        changeDurationTo(5);
        assertThatCurrentTimeIs(5);
        changeDurationTo(1.01);
        assertThatCurrentTimeIs(1.01);
    }

    @Test public void shouldStopMediaWhenChangingRate(){
        systemUnderTest.changeMedia(mediaPlayer1);
        systemUnderTest.setRate(2.0);
        verify(mediaPlayer1).friendly_setRate(2.0);
        verify(mediaPlayer1).friendly_stop();
    }

    private void changeDurationTo(double seconds) {
        currentTimeMp1.set(Duration.seconds(seconds));
    }

    private void assertThatCurrentTimeIs(double seconds) {
        assertThat(currentTimeMp1.get(), is(Duration.seconds(seconds)));
    }


}