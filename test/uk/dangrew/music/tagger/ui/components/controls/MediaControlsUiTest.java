package uk.dangrew.music.tagger.ui.components.controls;

import com.sun.javafx.application.PlatformImpl;
import javafx.beans.property.*;
import javafx.util.Duration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.dangrew.kode.launch.TestApplication;
import uk.dangrew.music.tagger.main.MusicTrackState;
import uk.dangrew.music.tagger.model.ReadOnlyMedia;
import uk.dangrew.music.tagger.ui.components.MusicController;
import uk.dangrew.music.tagger.ui.components.controls.MediaControlsUi;
import uk.dangrew.music.tagger.ui.positioning.CanvasDimensions;
import uk.dangrew.music.tagger.ui.positioning.NodePositioningTester;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class MediaControlsUiTest {

    private DoubleProperty width;
    private DoubleProperty height;

    @Mock private ReadOnlyMedia readOnlyMedia;
    private BooleanProperty playingProperty;
    private ObjectProperty<Duration> currentTimeProperty;
    private DoubleProperty rateProperty;

    private MusicTrackState musicTrackState;
    @Mock private MusicController controller;
    private MediaControlsUi systemUnderTest;

    @Before
    public void initialiseSystemUnderTest() {
        TestApplication.startPlatform();

        initMocks(this);

        playingProperty = new SimpleBooleanProperty(false);
        currentTimeProperty = new SimpleObjectProperty<>(Duration.ZERO);
        rateProperty = new SimpleDoubleProperty(1.0);

        when(readOnlyMedia.playingProperty()).thenReturn(playingProperty);
        when(readOnlyMedia.currentTimeProperty()).thenReturn(currentTimeProperty);
        when(readOnlyMedia.rateProperty()).thenReturn(rateProperty);
        when(controller.getMedia()).thenReturn(readOnlyMedia);

        musicTrackState = new MusicTrackState();
        systemUnderTest = new MediaControlsUi(
                new CanvasDimensions(width = new SimpleDoubleProperty(), height = new SimpleDoubleProperty()),
                controller,
                musicTrackState
        );
    }

    @Test
    public void shouldBePositionedRelativeToScreen() {
        NodePositioningTester tester = new NodePositioningTester(systemUnderTest, width, height);
        tester.assertThatNodeTranslatesWhenWidthDimensionChanges(MediaControlsUi.WIDTH_PORTION);
        tester.assertThatNodeTranslatesWhenHeightDimensionChanges(MediaControlsUi.HEIGHT_PORTION);
    }

    @Test
    public void shouldForwardControlsToController() {
        systemUnderTest.play().fire();
        verify(controller).togglePause();

        systemUnderTest.stop().fire();
        verify(controller).stop();

        systemUnderTest.plus30().fire();
        verify(controller).plus(30);

        systemUnderTest.minus30().fire();
        verify(controller).minus(30);

        systemUnderTest.plus5().fire();
        verify(controller).plus(30);

        systemUnderTest.minus5().fire();
        verify(controller).minus(30);

        systemUnderTest.speedUp().fire();
        verify(controller).speedUp();

        systemUnderTest.slowDown().fire();
        verify(controller).slowDown();
    }

    @Test
    public void shouldRespondToMusicTrackPlaying() {
        assertThat(systemUnderTest.play().getText(), is(MediaControlsUi.PLAY_TEXT));
        playingProperty.set(true);
        assertThat(systemUnderTest.play().getText(), is(MediaControlsUi.PAUSE_TEXT));
        playingProperty.set(false);
        assertThat(systemUnderTest.play().getText(), is(MediaControlsUi.PLAY_TEXT));
    }

    @Test
    public void shouldDisplayCurrentTimeAndRate() {
        currentTimeProperty.set(Duration.seconds(30));
        assertThat(systemUnderTest.currentTime().getText(), is("0:30 at 1.0x"));
        currentTimeProperty.set(Duration.seconds(61.1));
        assertThat(systemUnderTest.currentTime().getText(), is("1:01.1 at 1.0x"));
        currentTimeProperty.set(Duration.seconds(-600));
        assertThat(systemUnderTest.currentTime().getText(), is("-10:00 at 1.0x"));
        rateProperty.set(2.0);
        assertThat(systemUnderTest.currentTime().getText(), is("-10:00 at 2.0x"));
    }

    @Test public void shouldProvideRecordingFunction(){
        assertThat(systemUnderTest.record().getText(), is(MediaControlsUi.RECORD_TEXT));

        systemUnderTest.record().fire();
        verify(controller).toggleRecording();

        systemUnderTest.record().fire();
        verify(controller, times(2)).toggleRecording();
    }

    @Test public void shouldRespondToRecordingFunction(){
        musicTrackState.recordingProperty().set(true);
        assertThat(systemUnderTest.play().isDisable(), is(true));
        assertThat(systemUnderTest.stop().isDisable(), is(true));
        assertThat(systemUnderTest.plus30().isDisable(), is(true));
        assertThat(systemUnderTest.minus30().isDisable(), is(true));
        assertThat(systemUnderTest.speedUp().isDisable(), is(true));
        assertThat(systemUnderTest.slowDown().isDisable(), is(true));
        assertThat(systemUnderTest.record().getText(), is(MediaControlsUi.STOP_RECORDING_TEXT));

        musicTrackState.recordingProperty().set(false);
        assertThat(systemUnderTest.play().isDisable(), is(false));
        assertThat(systemUnderTest.stop().isDisable(), is(false));
        assertThat(systemUnderTest.plus30().isDisable(), is(false));
        assertThat(systemUnderTest.minus30().isDisable(), is(false));
        assertThat(systemUnderTest.speedUp().isDisable(), is(false));
        assertThat(systemUnderTest.slowDown().isDisable(), is(false));
        assertThat(systemUnderTest.record().getText(), is(MediaControlsUi.RECORD_TEXT));
    }
}