package uk.dangrew.music.tagger.ui;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Consumer;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CanvasDimensionsTest {

    private DoubleProperty width;
    private DoubleProperty height;
    private CanvasDimensions systemUnderTest;

    @Before public void initialiseSystemUnderTest(){
        width = new SimpleDoubleProperty(0.0);
        height = new SimpleDoubleProperty(0.0);

        systemUnderTest = new CanvasDimensions(width, height);
    }

    @Test
    public void shouldProvideDimensions(){
        assertThat(systemUnderTest.width(), is(0.0));
        assertThat(systemUnderTest.height(), is(0.0));
        assertThat(systemUnderTest.halfWidth(), is(0.0));
        assertThat(systemUnderTest.halfHeight(), is(0.0));

        width.setValue(1000.0);
        height.setValue(500.0);

        assertThat(systemUnderTest.width(), is(1000.0));
        assertThat(systemUnderTest.height(), is(500.0));
        assertThat(systemUnderTest.halfWidth(), is(500.0));
        assertThat(systemUnderTest.halfHeight(), is(250.0));
    }

    @Test public void shouldProvideRegistrationAndNotification(){
        Consumer<Double> widthHandler1 = mock(Consumer.class);
        Consumer<Double> widthHandler2 = mock(Consumer.class);
        Consumer<Double> heightHandler1 = mock(Consumer.class);
        Consumer<Double> heightHandler2 = mock(Consumer.class);

        systemUnderTest.registerForWidthChange(widthHandler1);
        systemUnderTest.registerForWidthChange(widthHandler2);
        systemUnderTest.registerForHeightChange(heightHandler1);
        systemUnderTest.registerForHeightChange(heightHandler2);

        width.setValue(1000.0);
        verify(widthHandler1).accept(1000.0);
        verify(widthHandler2).accept(1000.0);
        verify(heightHandler1, never()).accept(anyDouble());
        verify(heightHandler2, never()).accept(anyDouble());

        height.setValue(500.0);
        verify(widthHandler1).accept(1000.0);
        verify(widthHandler2).accept(1000.0);
        verify(heightHandler1).accept(500.0);
        verify(heightHandler2).accept(500.0);
    }
}