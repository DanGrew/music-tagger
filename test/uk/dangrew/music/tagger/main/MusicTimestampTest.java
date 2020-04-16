package uk.dangrew.music.tagger.main;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class MusicTimestampTest {

    private MusicTimestamp systemUnderTest;

    @Before public void initialiseSystemUnderTest(){
        systemUnderTest = new MusicTimestamp(30.1);
    }

    @Test
    public void shouldProvideTimestampInformation(){
        assertThat(systemUnderTest.seconds(), is(30.1));
    }

    @Test public void shouldProvideDisplayValue(){
        assertThat(systemUnderTest.displayString(), is("0:30.1"));

        assertThat(new MusicTimestamp(121).displayString(), is("2:01"));
    }

    @Test public void shouldFormatTImestamps(){
        assertThat(MusicTimestamp.format(-3), is("-0:03"));
    }

}