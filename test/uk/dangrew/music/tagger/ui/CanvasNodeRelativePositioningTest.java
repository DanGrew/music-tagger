package uk.dangrew.music.tagger.ui;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import org.junit.Before;
import org.junit.Test;
import uk.dangrew.kode.TestCommon;
import uk.dangrew.kode.launch.TestApplication;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class CanvasNodeRelativePositioningTest {

    private BorderPane node;

    private DoubleProperty width;
    private DoubleProperty height;
    private CanvasNodeRelativePositioning systemUnderTest;

    @Before public void initialiseSystemUnderTest(){
        node = new BorderPane();

        width = new SimpleDoubleProperty(0.0);
        height = new SimpleDoubleProperty(0.0);
        systemUnderTest = new CanvasNodeRelativePositioning(new CanvasDimensions(width, height));
    }

    @Test
    public void shouldTranslateWhenDimensionsChange(){
        systemUnderTest.bind(node, new AbsolutePositioning(0.4), new AbsolutePositioning(0.6));

        assertThat(node.getTranslateX(), is( 0.0));
        assertThat(node.getTranslateY(), is( 0.0));

        width.set(1000.0);
        assertThat(node.getTranslateX(), is( 400.0));
        assertThat(node.getTranslateY(), is( 0.0));

        height.set(500.0);
        assertThat(node.getTranslateX(), is( 400.0));
        assertThat(node.getTranslateY(), is( 300.0));
    }

    @Test
    public void shouldTranslateWhenPropertyChanges(){
        DoubleProperty widthProperty = new SimpleDoubleProperty(0.4);
        DoubleProperty heightProperty = new SimpleDoubleProperty(0.5);
        systemUnderTest.bind(node, new RelativePositioning(widthProperty), new RelativePositioning(heightProperty));

        width.set(1000.0);
        height.set(500.0);
        assertThat(node.getTranslateX(), is( 400.0));
        assertThat(node.getTranslateY(), is( 250.0));

        widthProperty.set(0.8);
        heightProperty.set(0.9);
        assertThat(node.getTranslateX(), is(800.0));
        assertThat(node.getTranslateY(), is(450.0));
    }

    @Test public void shouldAccountForRegionDimensions()  {
        //no easy to test due to final methods
    }

}