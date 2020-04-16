package uk.dangrew.music.tagger.ui;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.junit.Before;
import org.junit.Test;
import uk.dangrew.kode.launch.TestApplication;

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
        systemUnderTest = new MtTrackScaleMarker(new CanvasDimensions(width, height), 0.3, 16);
    }

    @Test public void shouldProvideMarkerAndLabel(){
        assertThat(systemUnderTest.getChildren().contains(systemUnderTest.label()), is(true));
        assertThat(systemUnderTest.getChildren().contains(systemUnderTest.marker()), is(true));
    }

    @Test public void shouldUpdatePosition(){
        assertThat(systemUnderTest.heightPortionProperty().get(), is(0.3));
        systemUnderTest.updateMarker(0.1, 1);
        assertThat(systemUnderTest.heightPortionProperty().get(), is(0.4));
    }

    @Test public void shouldUpdateLabel(){
        assertThat(systemUnderTest.label().getText(), is("0:16"));
        systemUnderTest.updateMarker(0.1, 5);
        assertThat(systemUnderTest.label().getText(), is("0:21"));
    }

    @Test public void shouldBePositioned(){
        NodePositioningTester nodePositioningTester = new NodePositioningTester(systemUnderTest.label(), width, height);
        nodePositioningTester.assertThatNodeTranslatesWhenWidthDimensionChanges(MtTrackScaleMarker.LABEL_WIDTH_PORTION);
        nodePositioningTester.assertThatHeightPositionRecalculatesWhenPropertiesChange(systemUnderTest.heightPortionProperty(), value -> systemUnderTest.updateMarker(value, 1));

        LinePositioningTester linePositioningTester = new LinePositioningTester(systemUnderTest.marker(), width, height);
        linePositioningTester.assertThatLineTranslatesWhenWidthDimensionChanges(
                OptionalDouble.of(MtTrackScaleMarker.MARKER_WIDTH_START_PORTION),
                OptionalDouble.of(MtTrackScaleMarker.MARKER_WIDTH_END_PORTION)
        );
        linePositioningTester.assertThatPositionRecalculatesWhenHeightPropertiesChange(
                Optional.of(systemUnderTest.heightPortionProperty()), Optional.of(value -> systemUnderTest.updateMarker(value, 1)),
                Optional.of(systemUnderTest.heightPortionProperty()), Optional.of(value -> systemUnderTest.updateMarker(value, 1))
        );
    }
}