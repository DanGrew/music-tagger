package uk.dangrew.music.tagger.ui;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class AbsolutePositioningTest {

    private AbsolutePositioning systemUnderTest;

    @Before public void initialiseSystemUnderTest(){
        systemUnderTest = new AbsolutePositioning(0.4);
    }

    @Test
    public void shouldProvidePortion(){
        assertThat(systemUnderTest.getPositioning(), is(0.4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAcceptSmallerThanMinimum(){
        new AbsolutePositioning(-0.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAcceptLargerThanMaximum(){
        new AbsolutePositioning(1.1);
    }

}