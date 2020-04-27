package uk.dangrew.music.tagger.ui.components;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.junit.Before;
import org.junit.Test;
import uk.dangrew.kode.friendly.javafx.FriendlyMouseEvent;
import uk.dangrew.music.tagger.main.MusicTrackState;
import uk.dangrew.music.tagger.ui.positioning.CanvasDimensions;
import uk.dangrew.music.tagger.ui.positioning.LinePositioningTester;

import java.util.Optional;
import java.util.OptionalDouble;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MtCurrentPositionTest {

    private DoubleProperty width;
    private DoubleProperty height;

    private MusicTrackState musicTrackState;
    private MtCurrentPosition systemUnderTest;

    @Before
    public void initialiseSystemUnderTest() {
        musicTrackState = new MusicTrackState();
        systemUnderTest = new MtCurrentPosition(
                new CanvasDimensions(width = new SimpleDoubleProperty(), height = new SimpleDoubleProperty()),
                musicTrackState);
    }

    @Test
    public void shouldProvidePositioning() {
        LinePositioningTester tester = new LinePositioningTester(systemUnderTest, width, height);
        tester.assertThatLineTranslatesWhenWidthDimensionChanges(
                OptionalDouble.of(MtCurrentPosition.WIDTH_START_PORTION),
                OptionalDouble.of(MtCurrentPosition.WIDTH_END_PORTION)
        );
        tester.assertThatPositionRecalculatesWhenHeightPropertiesChange(
                Optional.ofNullable(musicTrackState.currentPositionProperty()),
                Optional.of(value -> musicTrackState.currentPositionProperty().set(value)),
                Optional.ofNullable(musicTrackState.currentPositionProperty()),
                Optional.of(value -> musicTrackState.currentPositionProperty().set(value))
        );
    }

    @Test
    public void shouldUpdateConfigurationWhenDragged() {
        height.set(1000.0);
        assertThat(systemUnderTest.getOnMouseDragged(), is(notNullValue()));

        FriendlyMouseEvent mouseEvent = mock(FriendlyMouseEvent.class);
        when(mouseEvent.getY()).thenReturn(100.0);

        systemUnderTest.mouseDragged(mouseEvent);
        assertThat(musicTrackState.currentPositionProperty().get(), is(0.1));
    }

}