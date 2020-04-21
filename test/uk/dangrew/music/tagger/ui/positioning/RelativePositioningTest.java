package uk.dangrew.music.tagger.ui.positioning;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.junit.Before;
import org.junit.Test;
import uk.dangrew.music.tagger.ui.positioning.RelativePositioning;

import java.util.function.Consumer;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RelativePositioningTest {

    private DoubleProperty property;
    private RelativePositioning systemUnderTest;

    @Before public void initialiseSystemUnderTest(){
        property = new SimpleDoubleProperty();
        systemUnderTest = new RelativePositioning(property, 10.0, 30.0);
    }

    @Test
    public void shouldProvidePosition(){
        assertThat(systemUnderTest.getPositioning(), is(0.0));
        property.set(16.0);
        assertThat(systemUnderTest.getPositioning(), is(16.0));
    }

    @Test public void shouldClampValue(){
        Consumer<Double> consumer = mock(Consumer.class);
        systemUnderTest.registerForUpdates(consumer);

        property.set(13.0);
        verify(consumer).accept(13.0);

        property.set(0.0);
        verify(consumer).accept(10.0);

        property.set(40.0);
        verify(consumer).accept(30.0);
    }

    @Test public void shouldUnregister(){
        Consumer<Double> consumer = mock(Consumer.class);
        systemUnderTest.registerForUpdates(consumer);

        property.set(13.0);
        verify(consumer).accept(13.0);

        systemUnderTest.unregister(consumer);

        property.set(0.0);
        verify(consumer, times(1)).accept(anyDouble());

        property.set(40.0);
        verify(consumer, times(1)).accept(anyDouble());
    }

}