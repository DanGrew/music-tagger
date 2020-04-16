package uk.dangrew.music.tagger.ui;

import com.sun.javafx.application.PlatformImpl;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.dangrew.music.tagger.recorder.MusicController;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class MediaControlsUiTest {

    private DoubleProperty width;
    private DoubleProperty height;

    @Mock private MusicController controller;
    private MediaControlsUi systemUnderTest;

    @Before
    public void initialiseSystemUnderTest() {
        PlatformImpl.startup(() -> {
        });

        initMocks(this);
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
        verify(controller).play();

        systemUnderTest.pause().fire();
        verify(controller).pause();

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
}