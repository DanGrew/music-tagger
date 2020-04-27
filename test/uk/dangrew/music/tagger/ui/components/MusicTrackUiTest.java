package uk.dangrew.music.tagger.ui.components;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.dangrew.kode.launch.TestApplication;
import uk.dangrew.music.tagger.main.ChangeableMedia;
import uk.dangrew.music.tagger.main.MusicController;
import uk.dangrew.music.tagger.main.MusicTrackState;
import uk.dangrew.music.tagger.ui.positioning.CanvasDimensions;
import uk.dangrew.music.tagger.ui.positioning.LinePositioningTester;

import java.util.OptionalDouble;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MusicTrackUiTest {

    private DoubleProperty width;
    private DoubleProperty height;

    private MusicTrackState configuration;
    @Mock private MusicController controller;
    private MusicTrackUi systemUnderTest;

    @Before
    public void initialiseSystemUnderTest() {
        TestApplication.startPlatform();
        initMocks(this);
        configuration = new MusicTrackState();
        when(controller.getMedia()).thenReturn(new ChangeableMedia());
        systemUnderTest = new MusicTrackUi(
                new CanvasDimensions(width = new SimpleDoubleProperty(), height = new SimpleDoubleProperty()),
                controller,
                configuration
        );
    }

    @Test
    public void shouldBePositioned() {
        LinePositioningTester tester = new LinePositioningTester(systemUnderTest.skeleton(), width, height);
        tester.assertThatLineTranslatesWhenWidthDimensionChanges(
                OptionalDouble.of(MusicTrackUi.WIDTH_PORTION),
                OptionalDouble.of(MusicTrackUi.WIDTH_PORTION)
        );
        tester.assertThatLineTranslatesWhenHeightDimensionChanges(
                OptionalDouble.of(MusicTrackUi.START_HEIGHT_PORTION),
                OptionalDouble.of(MusicTrackUi.END_HEIGHT_PORTION)
        );

        tester = new LinePositioningTester(systemUnderTest.leftHook(), width, height);
        tester.assertThatLineTranslatesWhenWidthDimensionChanges(
                OptionalDouble.of(MusicTrackUi.MARKER_WIDTH_START_PORTION),
                OptionalDouble.of(MusicTrackUi.MARKER_WIDTH_START_PORTION)
        );
        tester.assertThatLineTranslatesWhenHeightDimensionChanges(
                OptionalDouble.of(MusicTrackUi.START_HEIGHT_PORTION),
                OptionalDouble.of(MusicTrackUi.END_HEIGHT_PORTION)
        );

        tester = new LinePositioningTester(systemUnderTest.rightHook(), width, height);
        tester.assertThatLineTranslatesWhenWidthDimensionChanges(
                OptionalDouble.of(MusicTrackUi.MARKER_WIDTH_END_PORTION),
                OptionalDouble.of(MusicTrackUi.MARKER_WIDTH_END_PORTION)
        );
        tester.assertThatLineTranslatesWhenHeightDimensionChanges(
                OptionalDouble.of(MusicTrackUi.START_HEIGHT_PORTION),
                OptionalDouble.of(MusicTrackUi.END_HEIGHT_PORTION)
        );
    }

    @Test public void shouldProvideComponents(){
        assertThat(systemUnderTest.getChildren().contains(systemUnderTest.skeleton()), is(true));
        assertThat(systemUnderTest.getChildren().contains(systemUnderTest.leftHook()), is(true));
        assertThat(systemUnderTest.getChildren().contains(systemUnderTest.rightHook()), is(true));
    }

}