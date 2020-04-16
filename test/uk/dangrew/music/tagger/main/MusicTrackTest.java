package uk.dangrew.music.tagger.main;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

public class MusicTrackTest {

    private MusicTrack systemUnderTest;

    @Before
    public void initialiseSystemUnderTest(){
        systemUnderTest = new MusicTrack();
    }

    @Test
    public void shouldTagTrack(){
        systemUnderTest.tag(new MusicTimestamp(30));
        systemUnderTest.tag(new MusicTimestamp(90));

        assertThat(systemUnderTest.getTags(), hasSize(2));
        assertThat(systemUnderTest.getTags().get(0).seconds(), is(30.0));
        assertThat(systemUnderTest.getTags().get(1).seconds(), is(90.0));
    }

}