package uk.dangrew.music.tagger.ui;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.junit.Before;
import org.junit.Test;
import uk.dangrew.kode.friendly.javafx.FriendlyMouseEvent;
import uk.dangrew.kode.utility.event.TestMouseEvent;
import uk.dangrew.music.tagger.recorder.MusicTrackConfiguration;

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

    private MusicTrackConfiguration musicTrackConfiguration;
    private MtCurrentPosition systemUnderTest;

    @Before
    public void initialiseSystemUnderTest() {
        musicTrackConfiguration = new MusicTrackConfiguration();
        systemUnderTest = new MtCurrentPosition(
                new CanvasDimensions(width = new SimpleDoubleProperty(), height = new SimpleDoubleProperty()),
                musicTrackConfiguration);
    }

    @Test
    public void shouldProvidePositioning() {
        LinePositioningTester tester = new LinePositioningTester(systemUnderTest, width, height);
        tester.assertThatLineTranslatesWhenWidthDimensionChanges(
                OptionalDouble.of(MtCurrentPosition.WIDTH_START_PORTION),
                OptionalDouble.of(MtCurrentPosition.WIDTH_END_PORTION)
        );
        tester.assertThatPositionRecalculatesWhenHeightPropertiesChange(
                Optional.ofNullable(musicTrackConfiguration.currentPositionProperty()),
                Optional.of(value -> musicTrackConfiguration.currentPositionProperty().set(value)),
                Optional.ofNullable(musicTrackConfiguration.currentPositionProperty()),
                Optional.of(value -> musicTrackConfiguration.currentPositionProperty().set(value))
        );
    }

    @Test
    public void shouldUpdateConfigurationWhenDragged() {
        height.set(1000.0);
        assertThat(systemUnderTest.getOnMouseDragged(), is(notNullValue()));

        FriendlyMouseEvent mouseEvent = mock(FriendlyMouseEvent.class);
        when(mouseEvent.getY()).thenReturn(100.0);

        systemUnderTest.mouseDragged(mouseEvent);
        assertThat(musicTrackConfiguration.currentPositionProperty().get(), is(0.1));
    }

}