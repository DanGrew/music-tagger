package uk.dangrew.music.tagger.model;

import org.junit.Before;
import org.junit.Test;
import uk.dangrew.music.tagger.model.MusicTimestamp;
import uk.dangrew.music.tagger.model.MusicTrack;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

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
        assertThat(systemUnderTest.getTags().get(0).getMusicTimestamp().seconds(), is(30.0));
        assertThat(systemUnderTest.getTags().get(1).getMusicTimestamp().seconds(), is(90.0));
    }

    @Test public void shouldRemoveTags(){
        Tag tag1 = systemUnderTest.tag(new MusicTimestamp(30));
        Tag tag2 = systemUnderTest.tag(new MusicTimestamp(90));

        assertThat(systemUnderTest.getTags(), hasSize(2));

        systemUnderTest.removeTag(tag1);
        assertThat(systemUnderTest.getTags(), hasSize(1));
        assertThat(systemUnderTest.getTags().get(0), is(tag2));

        systemUnderTest.removeTag(mock(Tag.class));
        assertThat(systemUnderTest.getTags(), hasSize(1));
    }

    @Test public void shouldProvideSortedTimestamps(){
        Tag tag1 = systemUnderTest.tag(new MusicTimestamp(30));
        Tag tag2 = systemUnderTest.tag(new MusicTimestamp(15));

        assertThat(systemUnderTest.getTags(), hasSize(2));
        assertThat(systemUnderTest.getTags().get(0), is(tag1));
        assertThat(systemUnderTest.getTags().get(1), is(tag2));

        assertThat(systemUnderTest.getSortedTags().get(0), is(tag2));
        assertThat(systemUnderTest.getSortedTags().get(1), is(tag1));
    }

}