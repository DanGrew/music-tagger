package uk.dangrew.music.tagger.ui.components.track;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.dangrew.kode.friendly.javafx.FriendlyMouseEvent;
import uk.dangrew.kode.launch.TestApplication;
import uk.dangrew.music.tagger.main.MusicTrackState;
import uk.dangrew.music.tagger.model.ChangeableMedia;
import uk.dangrew.music.tagger.model.MusicTimestamp;
import uk.dangrew.music.tagger.model.MusicTrack;
import uk.dangrew.music.tagger.model.Tag;
import uk.dangrew.music.tagger.ui.positioning.CanvasDimensions;
import uk.dangrew.music.tagger.ui.positioning.LinePositioningTester;
import uk.dangrew.music.tagger.ui.positioning.NodePositioningTester;

import java.util.Optional;
import java.util.OptionalDouble;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class MtCurrentTimeSliderTest {

    private DoubleProperty width;
    private DoubleProperty height;

    @Mock private ChangeableMedia changeableMedia;
    private ObjectProperty<Duration> durationProperty;
    private ObservableList<Tag> tags;

    @Mock private MusicTrack musicTrack;
    private MusicTrackState musicTrackState;
    private MtCurrentTimeSlider systemUnderTest;

    @Before public void initialiseSystemUnderTest(){
        TestApplication.startPlatform();
        initMocks(this);

        width = new SimpleDoubleProperty(1000.0);
        height = new SimpleDoubleProperty(500.0);
        durationProperty = new SimpleObjectProperty<>(Duration.seconds(400));

        when(musicTrack.mediaPlayer()).thenReturn(changeableMedia);
        when(changeableMedia.durationProperty()).thenReturn(durationProperty);

        tags = FXCollections.observableArrayList();
        when(musicTrack.getTags()).thenReturn(tags);
        tags.add(new Tag(new MusicTimestamp(100)));

        musicTrackState = new MusicTrackState();

        systemUnderTest = new MtCurrentTimeSlider(
                new CanvasDimensions(width, height),
                musicTrack,
                musicTrackState
        );
    }

    @Test
    public void shouldProvideSkeleton(){
        assertThat(systemUnderTest.getChildren().contains(systemUnderTest.skeleton()), is(true));

        LinePositioningTester tester = new LinePositioningTester(systemUnderTest.skeleton(), width, height);
        tester.assertThatLineTranslatesWhenWidthDimensionChanges(
                OptionalDouble.of(MtCurrentTimeSlider.WIDTH_START_PORTION),
                OptionalDouble.of(MtCurrentTimeSlider.WIDTH_END_PORTION)
        );
        tester.assertThatLineTranslatesWhenHeightDimensionChanges(
                OptionalDouble.of(MtCurrentTimeSlider.SKELETON_HEIGHT_PORTION),
                OptionalDouble.of(MtCurrentTimeSlider.SKELETON_HEIGHT_PORTION)
        );
    }

    @Test
    public void shouldProvideHooks(){
        assertThat(systemUnderTest.getChildren().contains(systemUnderTest.startHook()), is(true));

        LinePositioningTester tester = new LinePositioningTester(systemUnderTest.startHook(), width, height);
        tester.assertThatLineTranslatesWhenWidthDimensionChanges(
                OptionalDouble.of(MtCurrentTimeSlider.WIDTH_START_PORTION),
                OptionalDouble.of(MtCurrentTimeSlider.WIDTH_START_PORTION)
        );
        tester.assertThatLineTranslatesWhenHeightDimensionChanges(
                OptionalDouble.of(MtCurrentTimeSlider.HOOK_HEIGHT_START_PORTION),
                OptionalDouble.of(MtCurrentTimeSlider.HOOK_HEIGHT_END_PORTION)
        );

        assertThat(systemUnderTest.getChildren().contains(systemUnderTest.endHook()), is(true));

        tester = new LinePositioningTester(systemUnderTest.endHook(), width, height);
        tester.assertThatLineTranslatesWhenWidthDimensionChanges(
                OptionalDouble.of(MtCurrentTimeSlider.WIDTH_END_PORTION),
                OptionalDouble.of(MtCurrentTimeSlider.WIDTH_END_PORTION)
        );
        tester.assertThatLineTranslatesWhenHeightDimensionChanges(
                OptionalDouble.of(MtCurrentTimeSlider.HOOK_HEIGHT_START_PORTION),
                OptionalDouble.of(MtCurrentTimeSlider.HOOK_HEIGHT_END_PORTION)
        );
    }

    @Test
    public void shouldProvideLabels(){
        width.set(0.0);
        height.set(0.0);

        assertThat(systemUnderTest.getChildren().contains(systemUnderTest.startLabel()), is(true));

        NodePositioningTester tester = new NodePositioningTester(systemUnderTest.startLabel(), width, height);
        tester.assertThatNodeTranslatesWhenWidthDimensionChanges(MtCurrentTimeSlider.LABEL_WIDTH_START_PORTION);
        tester.assertThatNodeTranslatesWhenHeightDimensionChanges(MtCurrentTimeSlider.SKELETON_HEIGHT_PORTION);

        assertThat(systemUnderTest.getChildren().contains(systemUnderTest.endLabel()), is(true));

        tester = new NodePositioningTester(systemUnderTest.endLabel(), width, height);
        tester.assertThatNodeTranslatesWhenWidthDimensionChanges(MtCurrentTimeSlider.LABEL_WIDTH_END_PORTION);
        tester.assertThatNodeTranslatesWhenHeightDimensionChanges(MtCurrentTimeSlider.SKELETON_HEIGHT_PORTION);
    }

    @Test
    public void shouldProvideCurrentTime(){
        assertThat(systemUnderTest.getChildren().contains(systemUnderTest.currentTimeLine()), is(true));

        LinePositioningTester tester = new LinePositioningTester(systemUnderTest.currentTimeLine(), width, height);
        tester.assertThatPositionRecalculatesWhenWidthPropertiesChange(
                Optional.of(systemUnderTest.currentTimeWidthProperty()),
                Optional.of(systemUnderTest.currentTimeWidthProperty())
        );
        tester.assertThatLineTranslatesWhenHeightDimensionChanges(
                OptionalDouble.of(MtCurrentTimeSlider.HOOK_HEIGHT_START_PORTION),
                OptionalDouble.of(MtCurrentTimeSlider.HOOK_HEIGHT_END_PORTION)
        );
    }

    @Test
    public void shouldUpdateCurrentTimePosition(){
        musicTrackState.currentTimeProperty().set(100);
        assertThat(systemUnderTest.currentTimeLine().getStartX(), is(closeTo(300.0, 0.001)));
        musicTrackState.currentTimeProperty().set(200);
        assertThat(systemUnderTest.currentTimeLine().getStartX(), is(closeTo(500.0, 0.001)));
    }

    @Test
    public void shouldSeekToDraggedPosition(){
        FriendlyMouseEvent mouseEvent = mock(FriendlyMouseEvent.class);
        when(mouseEvent.getX()).thenReturn(400.0);

        musicTrackState.currentTimeProperty().set(100);

        systemUnderTest.currentLineDragged(mouseEvent);
        verify(changeableMedia).seek(Duration.seconds(150));
    }

    @Test
    public void shouldUpdateDurationLabel(){
        assertThat(systemUnderTest.endLabel().getText(), is(MusicTimestamp.format(400)));
        durationProperty.set(Duration.seconds(300));
        assertThat(systemUnderTest.endLabel().getText(), is(MusicTimestamp.format(300)));
    }

    @Test
    public void shouldProvideJumpTos(){
        Line tagLine = systemUnderTest.lineForTag(tags.get(0));
        assertThat(systemUnderTest.getChildren().contains(tagLine), is(true));

        systemUnderTest.tagLineReleased(tags.get(0));
        verify(changeableMedia).seek(Duration.seconds(tags.get(0).getMusicTimestamp().seconds()));

        tags.add(new Tag(new MusicTimestamp(200)));

        tagLine = systemUnderTest.lineForTag(tags.get(1));
        assertThat(systemUnderTest.getChildren().contains(tagLine), is(true));

        systemUnderTest.tagLineReleased(tags.get(1));
        verify(changeableMedia).seek(Duration.seconds(tags.get(1).getMusicTimestamp().seconds()));
    }

    @Test public void shouldRemoveOldTags(){
        Line tagLine = systemUnderTest.lineForTag(tags.get(0));
        assertThat(systemUnderTest.getChildren().contains(tagLine), is(true));

        musicTrack.getTags().clear();
        assertThat(systemUnderTest.getChildren().contains(tagLine), is(false));
        assertThat(systemUnderTest.getChildren().contains(tagLine), is(false));
    }
}