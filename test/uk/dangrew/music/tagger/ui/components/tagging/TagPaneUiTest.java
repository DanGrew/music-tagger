package uk.dangrew.music.tagger.ui.components.tagging;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.junit.Before;
import org.junit.Test;
import uk.dangrew.kode.launch.TestApplication;
import uk.dangrew.music.tagger.main.MusicTrackState;
import uk.dangrew.music.tagger.model.MusicTimestamp;
import uk.dangrew.music.tagger.model.MusicTrack;
import uk.dangrew.music.tagger.ui.components.tagging.TagPaneUi;
import uk.dangrew.music.tagger.ui.positioning.CanvasDimensions;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class TagPaneUiTest {

    private DoubleProperty width;
    private DoubleProperty height;

    private MusicTrackState musicTrackState;
    private MusicTrack musicTrack;

    private TagPaneUi systemUnderTest;

    @Before public void initialiseSystemUnderTest(){
        TestApplication.startPlatform();

        width = new SimpleDoubleProperty();
        height = new SimpleDoubleProperty();
        musicTrack = new MusicTrack();
        musicTrack.tag(new MusicTimestamp(20));
        musicTrackState = new MusicTrackState();
        musicTrackState.currentPositionProperty().set(0.4);
        musicTrackState.currentTimeProperty().set(5.0);
        musicTrackState.scalePositionIntervalProperty().set(0.05);
        musicTrackState.scaleTimeIntervalProperty().set(5);
        systemUnderTest = new TagPaneUi(musicTrack, new CanvasDimensions(width, height), musicTrackState);
    }

    @Test
    public void shouldUpdateTagPositionsWhenCurrentTimeChanges(){
        musicTrack.tag(new MusicTimestamp(13.0));
        musicTrack.tag(new MusicTimestamp(27.0));

        assertThat(systemUnderTest.tagWidgetFor(musicTrack.getTags().get(0)).getTextFieldWidthProperty().get(), is( TagPaneUi.TEXT_POSITIONS.get(0)));
        assertThat(systemUnderTest.tagWidgetFor(musicTrack.getTags().get(1)).getTextFieldWidthProperty().get(), is( TagPaneUi.TEXT_POSITIONS.get(1)));
        assertThat(systemUnderTest.tagWidgetFor(musicTrack.getTags().get(2)).getTextFieldWidthProperty().get(), is( TagPaneUi.TEXT_POSITIONS.get(2)));

        musicTrackState.currentTimeProperty().set(1.0);

        assertThat(systemUnderTest.tagWidgetFor(musicTrack.getTags().get(0)).getTextFieldWidthProperty().get(), is( TagPaneUi.TEXT_POSITIONS.get(0)));
        assertThat(systemUnderTest.tagWidgetFor(musicTrack.getTags().get(1)).getTextFieldWidthProperty().get(), is( TagPaneUi.TEXT_POSITIONS.get(1)));
        assertThat(systemUnderTest.tagWidgetFor(musicTrack.getTags().get(2)).getTextFieldWidthProperty().get(), is( TagPaneUi.TEXT_POSITIONS.get(2)));
    }

    @Test
    public void shouldCreateTagForEachInTrackAndForEachCreated(){
        assertThat(systemUnderTest.tagWidgetFor(musicTrack.getTags().get(0)).getTag(), is(musicTrack.getTags().get(0)));

        musicTrack.tag(new MusicTimestamp(13.0));
        musicTrack.tag(new MusicTimestamp(27.0));

        assertThat(systemUnderTest.tagWidgetFor(musicTrack.getTags().get(1)).getTag(), is(musicTrack.getTags().get(1)));
        assertThat(systemUnderTest.tagWidgetFor(musicTrack.getTags().get(2)).getTag(), is(musicTrack.getTags().get(2)));
    }

}