package uk.dangrew.music.tagger.ui.components;

import com.sun.javafx.application.PlatformImpl;
import javafx.beans.property.*;
import javafx.util.Duration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.dangrew.music.tagger.main.MusicController;
import uk.dangrew.music.tagger.main.ReadOnlyMedia;
import uk.dangrew.music.tagger.ui.positioning.CanvasDimensions;
import uk.dangrew.music.tagger.ui.positioning.NodePositioningTester;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MediaControlsUiTest {

    private DoubleProperty width;
    private DoubleProperty height;

    @Mock private ReadOnlyMedia readOnlyMedia;
    private BooleanProperty playingProperty;
    private ObjectProperty<Duration> currentTimeProperty;
    @Mock private MusicController controller;
    private MediaControlsUi systemUnderTest;

    @Before
    public void initialiseSystemUnderTest() {
        PlatformImpl.startup(() -> {
        });

        initMocks(this);

        playingProperty = new SimpleBooleanProperty(false);
        currentTimeProperty = new SimpleObjectProperty<>(Duration.ZERO);

        when(readOnlyMedia.playingProperty()).thenReturn(playingProperty);
        when(readOnlyMedia.currentTimeProperty()).thenReturn(currentTimeProperty);
        when(controller.getMedia()).thenReturn(readOnlyMedia);

        systemUnderTest = new MediaControlsUi(
                new CanvasDimensions(width = new SimpleDoubleProperty(), height = new SimpleDoubleProperty()),
                controller
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
        verify(controller).plus30();

        systemUnderTest.minus30().fire();
        verify(controller).minus30();

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
    public void shouldDisplayCurrentTime() {
        assertThat(systemUnderTest.currentTime().getText(), is("0:00"));
        currentTimeProperty.set(Duration.seconds(30));
        assertThat(systemUnderTest.currentTime().getText(), is("0:30"));
        currentTimeProperty.set(Duration.seconds(61.1));
        assertThat(systemUnderTest.currentTime().getText(), is("1:01.1"));
        currentTimeProperty.set(Duration.seconds(-600));
        assertThat(systemUnderTest.currentTime().getText(), is("-10:00"));
    }
}