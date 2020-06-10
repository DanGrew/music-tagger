package uk.dangrew.music.tagger.ui.components.tagging;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import uk.dangrew.kode.friendly.javafx.FriendlyMouseEvent;
import uk.dangrew.kode.launch.TestApplication;
import uk.dangrew.kode.utility.event.TestMouseEvent;
import uk.dangrew.music.tagger.main.MusicTrackState;
import uk.dangrew.music.tagger.model.MusicTimestamp;
import uk.dangrew.music.tagger.model.MusicTrack;
import uk.dangrew.music.tagger.model.Tag;
import uk.dangrew.music.tagger.ui.positioning.CanvasDimensions;
import uk.dangrew.music.tagger.ui.positioning.LinePositioningTester;
import uk.dangrew.music.tagger.ui.positioning.NodePositioningTester;

import java.util.Optional;
import java.util.OptionalDouble;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static uk.dangrew.kode.hamcrest.CommonPrecisionMatcher.isCloseTo;

public class TagWidgetTest {

    private DoubleProperty width;
    private DoubleProperty height;

    private Tag tag;
    private MusicTrack musicTrack;
    private MusicTrackState musicTrackState;

    @Mock private ScalePositionCalculator scalePositionCalculator;
    private TagWidget systemUnderTest;

    @Before
    public void initialiseSystemUnderTest() {
        TestApplication.startPlatform();
        initMocks(this);

        width = new SimpleDoubleProperty();
        height = new SimpleDoubleProperty();

        musicTrack = new MusicTrack();
        tag = musicTrack.tag(new MusicTimestamp(20));
        musicTrackState = new MusicTrackState();
        systemUnderTest = new TagWidget(scalePositionCalculator, tag, musicTrack, musicTrackState, new CanvasDimensions(width, height));
    }

    @Test
    public void shouldAddLineToTrack() {
        assertThat(systemUnderTest.getChildren().contains(systemUnderTest.getTrackLine()), is(true));

        LinePositioningTester tester = new LinePositioningTester(systemUnderTest.getTrackLine(), width, height);
        tester.assertThatFixedWidthIsRespectedWhenDimensionChanges(
                OptionalDouble.of(TagWidget.WIDTH_START_PORTION),
                OptionalDouble.of(TagWidget.WIDTH_END_PORTION)
        );
        tester.assertThatRelativeHeightIsRespectedWhenDimensionChanges(
                Optional.of(systemUnderTest.heightPortionProperty()),
                Optional.of(systemUnderTest.heightPortionProperty()::set),
                Optional.of(systemUnderTest.heightPortionProperty()),
                Optional.of(systemUnderTest.heightPortionProperty()::set)
        );
    }

    @Test
    public void shouldUpdateHeightOfComponents() {
        when(scalePositionCalculator.calculatePositionFor(tag.getMusicTimestamp().seconds(), musicTrackState))
                .thenReturn(OptionalDouble.of(0.2));

        systemUnderTest.updatePosition(0.1);

        assertThat(systemUnderTest.heightPortionProperty().get(), is(0.2));
    }

    @Test
    public void shouldAddTextFieldToEditor() {
        assertTextFieldAndControlsArePresent(true);
    }

    @Test public void shouldBindTagTextWithTextField(){
        assertThat(systemUnderTest.getTextField().getText(), is(tag.getTextProperty().get()));
        systemUnderTest.getTextField().setText("Test");
        assertThat(systemUnderTest.getTextField().getText(), is(tag.getTextProperty().get()));
        tag.getTextProperty().set("Another Test");
        assertThat(systemUnderTest.getTextField().getText(), is(tag.getTextProperty().get()));
    }

    @Test public void shouldPositionTextFieldAndControls(){
        NodePositioningTester tester = new NodePositioningTester(systemUnderTest.textFieldAndControlsWrapper(), width, height);
        tester.assertThatWidthPositionRecalculatesWhenPropertiesChange(
                systemUnderTest.getTextFieldWidthProperty(),
                systemUnderTest.getTextFieldWidthProperty()::set
        );
        tester.assertThatHeightPositionRecalculatesWhenPropertiesChange(
                systemUnderTest.heightPortionProperty(),
                systemUnderTest.heightPortionProperty()::set
        );
    }

    @Test
    public void shouldDisableTextFieldWhenRecording() {
        assertThat(systemUnderTest.getTextField().isDisable(), is(true));
        musicTrackState.recordingProperty().set(true);
        assertThat(systemUnderTest.getTextField().isDisable(), is(true));
        musicTrackState.recordingProperty().set(false);
        assertThat(systemUnderTest.getTextField().isDisable(), is(false));
    }

    @Test
    public void shouldRemoveWidgetsWhenTheyAreOutOfRange() {
        when(scalePositionCalculator.calculatePositionFor(tag.getMusicTimestamp().seconds(), musicTrackState))
                .thenReturn(OptionalDouble.empty());

        systemUnderTest.updatePosition(0.5);
        assertThat(systemUnderTest.getTextFieldWidthProperty().get(), is(0.5));

        assertThat(systemUnderTest.getChildren().contains(systemUnderTest.getTrackLine()), is(false));
        assertTextFieldAndControlsArePresent(false);

        when(scalePositionCalculator.calculatePositionFor(anyDouble(), any())).thenReturn(OptionalDouble.of(0.5));

        systemUnderTest.updatePosition(0.4);

        assertThat(systemUnderTest.getTextFieldWidthProperty().get(), is(0.4));

        assertThat(systemUnderTest.getChildren().contains(systemUnderTest.getTrackLine()), is(true));
        assertTextFieldAndControlsArePresent(true);
    }

    @Test public void shouldUpdatePositionWhenTimestampChanges(){
        when(scalePositionCalculator.calculatePositionFor(20, musicTrackState)).thenReturn(OptionalDouble.of(0.2));
        when(scalePositionCalculator.calculatePositionFor(40, musicTrackState)).thenReturn(OptionalDouble.of(0.3));

        systemUnderTest.updatePosition(0.1);
        assertThat(systemUnderTest.heightPortionProperty().get(), isCloseTo(0.2));

        tag.getMusicTimestamp().secondsProperty().set(40);
        assertThat(systemUnderTest.heightPortionProperty().get(), isCloseTo(0.3));
    }

    @Test public void shouldUpdatePositionWhenCurrentPositionChanges(){
        when(scalePositionCalculator.calculatePositionFor(20, musicTrackState))
                .thenReturn(OptionalDouble.of(0.2))
                .thenReturn(OptionalDouble.of(0.3));

        systemUnderTest.updatePosition(0.1);
        assertThat(systemUnderTest.heightPortionProperty().get(), isCloseTo(0.2));

        musicTrackState.currentPositionProperty().set(0.1);
        assertThat(systemUnderTest.heightPortionProperty().get(), isCloseTo(0.3));
    }

    @Test public void shouldUpdatePositionWhenScalePositionChanges(){
        when(scalePositionCalculator.calculatePositionFor(20, musicTrackState))
                .thenReturn(OptionalDouble.of(0.2))
                .thenReturn(OptionalDouble.of(0.3));

        systemUnderTest.updatePosition(0.1);
        assertThat(systemUnderTest.heightPortionProperty().get(), isCloseTo(0.2));

        musicTrackState.scalePositionIntervalProperty().set(0.8);
        assertThat(systemUnderTest.heightPortionProperty().get(), isCloseTo(0.3));
    }

    @Test public void shouldUpdatePositionWhenScaleTimeIntervalChanges(){
        when(scalePositionCalculator.calculatePositionFor(20, musicTrackState))
                .thenReturn(OptionalDouble.of(0.2))
                .thenReturn(OptionalDouble.of(0.3));

        systemUnderTest.updatePosition(0.1);
        assertThat(systemUnderTest.heightPortionProperty().get(), isCloseTo(0.2));

        musicTrackState.scaleTimeIntervalProperty().set(10);
        assertThat(systemUnderTest.heightPortionProperty().get(), isCloseTo(0.3));
    }

    @Test public void shouldMoveTagWithinBounds(){
        when(scalePositionCalculator.calculatePositionFor(20, musicTrackState))
                .thenReturn(OptionalDouble.of(0.2));

        systemUnderTest.updatePosition(0.1);
        assertThat(systemUnderTest.heightPortionProperty().get(), isCloseTo(0.2));

        FriendlyMouseEvent event = mock(FriendlyMouseEvent.class);
        when(event.friendly_getSceneY()).thenReturn(500.0);
        height.set(1000.0);

        assertThat(tag.getMusicTimestamp().seconds(), isCloseTo(20.0));
        systemUnderTest.mouseDragged(event);
        assertThat(tag.getMusicTimestamp().seconds(), isCloseTo(23.08));
    }

    @Test public void shouldNotMoveTagBeforeMinimumPosition(){
        when(scalePositionCalculator.calculatePositionFor(20, musicTrackState))
                .thenReturn(OptionalDouble.of(0.2));

        systemUnderTest.updatePosition(0.1);
        assertThat(systemUnderTest.heightPortionProperty().get(), isCloseTo(0.2));

        FriendlyMouseEvent event = mock(FriendlyMouseEvent.class);
        when(event.friendly_getSceneY()).thenReturn(0.0);
        height.set(1000.0);

        assertThat(tag.getMusicTimestamp().seconds(), isCloseTo(20.0));
        systemUnderTest.mouseDragged(event);
        assertThat(tag.getMusicTimestamp().seconds(), isCloseTo(20.0));
    }

    @Test public void shouldNotMoveTagAfterMaximumPosition(){
        when(scalePositionCalculator.calculatePositionFor(20, musicTrackState))
                .thenReturn(OptionalDouble.of(0.2));

        systemUnderTest.updatePosition(0.1);
        assertThat(systemUnderTest.heightPortionProperty().get(), isCloseTo(0.2));

        FriendlyMouseEvent event = mock(FriendlyMouseEvent.class);
        when(event.friendly_getSceneY()).thenReturn(1000.0);
        height.set(1000.0);

        assertThat(tag.getMusicTimestamp().seconds(), isCloseTo(20.0));
        systemUnderTest.mouseDragged(event);
        assertThat(tag.getMusicTimestamp().seconds(), isCloseTo(20.0));
    }

    @Test public void shouldProvideDeleteButton(){
        assertThat(musicTrack.getTags().contains(tag), is(true));
        systemUnderTest.deleteButton().getActionHandler().handle(new TestMouseEvent());
        assertThat(musicTrack.getTags().contains(tag), is(false));
    }

    @Ignore( "needs refactoring on music state to decouple properties")
    @Test public void shouldChangeEnablementBasedOnPlaying(){
        assertThat(systemUnderTest.moveButton().isDisable(), is(false));
        assertThat(systemUnderTest.deleteButton().isDisable(), is(false));

//        musicTrack.mediaPlayer().playingProperty().set(true);
        assertThat(systemUnderTest.moveButton().isDisable(), is(true));
        assertThat(systemUnderTest.deleteButton().isDisable(), is(false));
    }

    @Test public void shouldChangeEnablementBasedOnRecording(){
        assertThat(systemUnderTest.moveButton().isDisable(), is(false));
        assertThat(systemUnderTest.deleteButton().isDisable(), is(false));

        musicTrackState.recordingProperty().set(true);
        assertThat(systemUnderTest.moveButton().isDisable(), is(true));
        assertThat(systemUnderTest.deleteButton().isDisable(), is(true));
    }

    private void assertTextFieldAndControlsArePresent(boolean present){
        assertThat(systemUnderTest.getChildren().contains(systemUnderTest.textFieldAndControlsWrapper()), is(present));
        assertThat(systemUnderTest.textFieldAndControlsWrapper().getChildren().contains(systemUnderTest.getTextField()), is(true));
        assertThat(systemUnderTest.textFieldAndControlsWrapper().getChildren().contains(systemUnderTest.moveButton()), is(true));
        assertThat(systemUnderTest.textFieldAndControlsWrapper().getChildren().contains(systemUnderTest.deleteButton()), is(true));
    }
}