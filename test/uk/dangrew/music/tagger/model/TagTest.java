package uk.dangrew.music.tagger.model;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class TagTest {

    private Tag systemUnderTest;

    @Before public void initialiseSystemUnderTest(){
        systemUnderTest = new Tag(new MusicTimestamp(20.0));
    }

    @Test
    public void shouldCompare(){
        assertThat(systemUnderTest.compareTo(new Tag(new MusicTimestamp(20.0))), is(0));
        assertThat(systemUnderTest.compareTo(new Tag(new MusicTimestamp(10.0))), is(greaterThan(0)));
        assertThat(systemUnderTest.compareTo(new Tag(new MusicTimestamp(30.0))), is(lessThan(0)));

        assertThat(systemUnderTest.compareTo(new Tag(new MusicTimestamp(20.0), "anything")), is(lessThan(0)));

        systemUnderTest.getTextProperty().set("anything");
        assertThat(systemUnderTest.compareTo(new Tag(new MusicTimestamp(20.0), "anything")), is(0));
        assertThat(systemUnderTest.compareTo(new Tag(new MusicTimestamp(10.0), "aaaa")), is(greaterThan(0)));
        assertThat(systemUnderTest.compareTo(new Tag(new MusicTimestamp(30.0), "greater")), is(lessThan(0)));

        assertThat(systemUnderTest.compareTo(new Tag(new MusicTimestamp(30.0))), is(lessThan(0)));
    }

}