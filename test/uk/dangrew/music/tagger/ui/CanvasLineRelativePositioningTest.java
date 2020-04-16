package uk.dangrew.music.tagger.ui;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.shape.Line;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class CanvasLineRelativePositioningTest {

    private Line line;

    private DoubleProperty width;
    private DoubleProperty height;
    private CanvasLineRelativePositioning systemUnderTest;

    @Before
    public void initialiseSystemUnderTest() {
        line = new Line(0, 0, 0, 0);

        width = new SimpleDoubleProperty(0.0);
        height = new SimpleDoubleProperty(0.0);
        systemUnderTest = new CanvasLineRelativePositioning(new CanvasDimensions(width, height));
    }

    @Test
    public void shouldChangeLinePositionsWhenDimensionsChange() {
        systemUnderTest.bind(line, new LinePortions(
                new AbsolutePositioning(0.2),
                new AbsolutePositioning(0.3),
                new AbsolutePositioning(0.4),
                new AbsolutePositioning(0.5)));

        width.setValue(1000.0);

        assertThat(line.getStartX(), is(200.0));
        assertThat(line.getEndX(), is(300.0));
        assertThat(line.getStartY(), is(0.0));
        assertThat(line.getEndY(), is(0.0));

        height.setValue(500.0);

        assertThat(line.getStartX(), is(200.0));
        assertThat(line.getEndX(), is(300.0));
        assertThat(line.getStartY(), is(200.0));
        assertThat(line.getEndY(), is(250.0));
    }

    @Test
    public void shouldRealculateWhenLinePropertyChanges() {
        DoubleProperty widthStart = new SimpleDoubleProperty(0.2);
        DoubleProperty widthEnd = new SimpleDoubleProperty(0.3);
        DoubleProperty heightStart = new SimpleDoubleProperty(0.4);
        DoubleProperty heightEnd = new SimpleDoubleProperty(0.5);

        systemUnderTest.bind(line, new LinePortions(
                new RelativePositioning(widthStart),
                new RelativePositioning(widthEnd),
                new RelativePositioning(heightStart),
                new RelativePositioning(heightEnd)
        ));

        width.setValue(1000.0);
        height.setValue(500.0);

        assertThat(line.getStartX(), is(200.0));
        assertThat(line.getEndX(), is(300.0));
        assertThat(line.getStartY(), is(200.0));
        assertThat(line.getEndY(), is(250.0));

        widthStart.setValue(0.4);
        widthEnd.setValue(0.5);
        heightStart.setValue(0.6);
        heightEnd.setValue(0.7);

        assertThat(line.getStartX(), is(400.0));
        assertThat(line.getEndX(), is(500.0));
        assertThat(line.getStartY(), is(300.0));
        assertThat(line.getEndY(), is(350.0));
    }

}