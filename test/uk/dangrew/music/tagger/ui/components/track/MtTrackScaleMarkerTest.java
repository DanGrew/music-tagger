package uk.dangrew.music.tagger.ui.components.track;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.junit.Before;
import org.junit.Test;
import uk.dangrew.kode.launch.TestApplication;
import uk.dangrew.music.tagger.ui.positioning.CanvasDimensions;
import uk.dangrew.music.tagger.ui.positioning.LinePositioningTester;
import uk.dangrew.music.tagger.ui.positioning.NodePositioningTester;

import java.util.Optional;
import java.util.OptionalDouble;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class MtTrackScaleMarkerTest {

    private DoubleProperty width;
    private DoubleProperty height;
    private MtTrackScaleMarker systemUnderTest;

    @Before public void initialiseSystemUnderTest(){
        TestApplication.startPlatform();
        width = new SimpleDoubleProperty();
        height = new SimpleDoubleProperty();
        systemUnderTest = new MtTrackScaleMarker(new CanvasDimensions(width, height));
    }

    @Test public void shouldProvideMarkerAndLabel(){
        assertThat(systemUnderTest.getChildren().contains(systemUnderTest.label()), is(true));
        assertThat(systemUnderTest.getChildren().contains(systemUnderTest.marker()), is(true));
    }

    @Test public void shouldUpdatePosition(){
        assertThat(systemUnderTest.heightPortionProperty().get(), is(0.0));
        systemUnderTest.setPosition(0.1);
        assertThat(systemUnderTest.heightPortionProperty().get(), is(0.1));
    }

    @Test public void shouldUpdateLabel(){
        assertThat(systemUnderTest.label().getText(), is("0:00"));
        systemUnderTest.setSeconds(5);
        assertThat(systemUnderTest.label().getText(), is("0:05"));
    }

    @Test public void shouldBePositioned(){
        NodePositioningTester nodePositioningTester = new NodePositioningTester(systemUnderTest.label(), width, height);
        nodePositioningTester.assertThatNodeTranslatesWhenWidthDimensionChanges(MtTrackScaleMarker.LABEL_WIDTH_PORTION);
        nodePositioningTester.assertThatHeightPositionRecalculatesWhenPropertiesChange(systemUnderTest.heightPortionProperty(), value -> systemUnderTest.setPosition(value));

        LinePositioningTester linePositioningTester = new LinePositioningTester(systemUnderTest.marker(), width, height);
        linePositioningTester.assertThatFixedWidthIsRespectedWhenDimensionChanges(
                OptionalDouble.of(MtTrackScaleMarker.MARKER_WIDTH_START_PORTION),
                OptionalDouble.of(MtTrackScaleMarker.MARKER_WIDTH_END_PORTION)
        );
        linePositioningTester.assertThatRelativeHeightIsRespectedWhenDimensionChanges(
                Optional.of(systemUnderTest.heightPortionProperty()), Optional.of(value -> systemUnderTest.setPosition(value)),
                Optional.of(systemUnderTest.heightPortionProperty()), Optional.of(value -> systemUnderTest.setPosition(value))
        );
    }

    @Test public void shouldDetach(){
        systemUnderTest.detach();
        NodePositioningTester nodePositioningTester = new NodePositioningTester(systemUnderTest.label(), width, height);
        nodePositioningTester.assertThatNodeDoesNotTranslateWhenWidthDimensionChanges(MtTrackScaleMarker.LABEL_WIDTH_PORTION);
        nodePositioningTester.assertThatHeightPositionDoesNotRecalculateWhenPropertiesChange(systemUnderTest.heightPortionProperty(), value -> systemUnderTest.setPosition(value));

        LinePositioningTester linePositioningTester = new LinePositioningTester(systemUnderTest.marker(), width, height);
        linePositioningTester.assertThatLineDoesNotTranslateWhenWidthDimensionChanges(
                OptionalDouble.of(MtTrackScaleMarker.MARKER_WIDTH_START_PORTION),
                OptionalDouble.of(MtTrackScaleMarker.MARKER_WIDTH_END_PORTION)
        );
        linePositioningTester.assertThatPositionDoesNotRecalculateWhenHeightPropertiesChange(
                Optional.of(systemUnderTest.heightPortionProperty()), Optional.of(value -> systemUnderTest.setPosition(value)),
                Optional.of(systemUnderTest.heightPortionProperty()), Optional.of(value -> systemUnderTest.setPosition(value))
        );
    }
}