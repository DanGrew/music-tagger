package uk.dangrew.music.tagger.ui.components.tagging;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.dangrew.kode.launch.TestApplication;
import uk.dangrew.music.tagger.main.MusicTrackState;
import uk.dangrew.music.tagger.model.MusicTimestamp;
import uk.dangrew.music.tagger.model.Tag;
import uk.dangrew.music.tagger.ui.components.tagging.ScalePositionCalculator;
import uk.dangrew.music.tagger.ui.components.tagging.TagWidget;
import uk.dangrew.music.tagger.ui.positioning.CanvasDimensions;
import uk.dangrew.music.tagger.ui.positioning.LinePositioningTester;
import uk.dangrew.music.tagger.ui.positioning.NodePositioningTester;

import java.util.Optional;
import java.util.OptionalDouble;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class TagWidgetTest {

    private DoubleProperty width;
    private DoubleProperty height;

    private Tag tag;
    private MusicTrackState musicTrackState;

    @Mock private ScalePositionCalculator scalePositionCalculator;
    private TagWidget systemUnderTest;

    @Before
    public void initialiseSystemUnderTest() {
        TestApplication.startPlatform();
        initMocks(this);

        width = new SimpleDoubleProperty();
        height = new SimpleDoubleProperty();

        tag = new Tag(new MusicTimestamp(20));
        musicTrackState = new MusicTrackState();
        systemUnderTest = new TagWidget(scalePositionCalculator, tag, musicTrackState, new CanvasDimensions(width, height));
    }

    @Test
    public void shouldAddLineToTrack() {
        assertThat(systemUnderTest.getChildren().contains(systemUnderTest.getTrackLine()), is(true));

        LinePositioningTester tester = new LinePositioningTester(systemUnderTest.getTrackLine(), width, height);
        tester.assertThatLineTranslatesWhenWidthDimensionChanges(
                OptionalDouble.of(TagWidget.WIDTH_START_PORTION),
                OptionalDouble.of(TagWidget.WIDTH_END_PORTION)
        );
        tester.assertThatPositionRecalculatesWhenHeightPropertiesChange(
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
        assertThat(systemUnderTest.getChildren().contains(systemUnderTest.getTextField()), is(true));
    }

    @Test public void shouldBindTagTextWithTextField(){
        assertThat(systemUnderTest.getTextField().getText(), is(tag.getTextProperty().get()));
        systemUnderTest.getTextField().setText("Test");
        assertThat(systemUnderTest.getTextField().getText(), is(tag.getTextProperty().get()));
        tag.getTextProperty().set("Another Test");
        assertThat(systemUnderTest.getTextField().getText(), is(tag.getTextProperty().get()));
    }

    @Test public void shouldPositionTextField(){
        NodePositioningTester tester = new NodePositioningTester(systemUnderTest.getTextField(), width, height);
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
        assertThat(systemUnderTest.getChildren().contains(systemUnderTest.getTextField()), is(false));

        when(scalePositionCalculator.calculatePositionFor(anyDouble(), any())).thenReturn(OptionalDouble.of(0.5));

        systemUnderTest.updatePosition(0.4);

        assertThat(systemUnderTest.getTextFieldWidthProperty().get(), is(0.4));

        assertThat(systemUnderTest.getChildren().contains(systemUnderTest.getTrackLine()), is(true));
        assertThat(systemUnderTest.getChildren().contains(systemUnderTest.getTextField()), is(true));
    }


}