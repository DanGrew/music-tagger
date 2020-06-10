package uk.dangrew.music.tagger.ui.components.track;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Duration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.dangrew.kode.hamcrest.CommonPrecisionMatcher;
import uk.dangrew.kode.utility.event.TestMouseEvent;
import uk.dangrew.music.tagger.model.ChangeableMedia;
import uk.dangrew.music.tagger.model.MusicTimestamp;
import uk.dangrew.music.tagger.model.MusicTrack;
import uk.dangrew.music.tagger.model.Tag;
import uk.dangrew.music.tagger.ui.positioning.CanvasDimensions;
import uk.dangrew.music.tagger.ui.positioning.LinePositioningTester;

import java.util.Optional;
import java.util.OptionalDouble;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static uk.dangrew.kode.hamcrest.CommonPrecisionMatcher.isCloseTo;

public class MtctsJumpToWidgetTest {

    private DoubleProperty widthProperty;
    private DoubleProperty heightProperty;

    @Mock private MusicTrack musicTrack;
    @Mock private ChangeableMedia media;
    private ObjectProperty<Duration> duration;

    private Tag tag;
    private MtctsJumpToWidget systemUnderTest;

    @Before public void initialiseSystemUnderTest(){
        initMocks(this);
        tag = new Tag(new MusicTimestamp(20));
        when(musicTrack.mediaPlayer()).thenReturn(media);
        duration = new SimpleObjectProperty<>(Duration.seconds(200.0));
        when(media.durationProperty()).thenReturn(duration);

        widthProperty = new SimpleDoubleProperty(100);
        heightProperty = new SimpleDoubleProperty(200);

        systemUnderTest = new MtctsJumpToWidget(tag, musicTrack, new CanvasDimensions(widthProperty, heightProperty));
    }

    @Test public void shouldPositionRelativeToCanvas(){
        LinePositioningTester tester = new LinePositioningTester(systemUnderTest, widthProperty, heightProperty);
        tester.assertThatRelativeWidthIsRespectedWhenDimensionChanges(
                Optional.of(systemUnderTest.widthPositionProperty()),
                Optional.of(systemUnderTest.widthPositionProperty())
        );
        tester.assertThatFixedHeightIsRespectedWhenDimensionChanges(
                OptionalDouble.of(MtctsJumpToWidget.HOOK_HEIGHT_START_PORTION),
                OptionalDouble.of(MtctsJumpToWidget.HOOK_HEIGHT_END_PORTION)
        );
    }

    @Test public void shouldSeekWhenReleased(){
        systemUnderTest.getOnMouseReleased().handle(new TestMouseEvent());
        verify(media).seek(Duration.seconds(20.0));

        tag.getMusicTimestamp().secondsProperty().set(100.0);
        systemUnderTest.getOnMouseReleased().handle(new TestMouseEvent());
        verify(media).seek(Duration.seconds(100.0));
    }

    @Test public void shouldUpdateWidthPositionWhenTimestampChanged(){
        assertThat(systemUnderTest.widthPositionProperty().get(), isCloseTo(0.18));
        tag.getMusicTimestamp().secondsProperty().set(40.0);
        assertThat(systemUnderTest.widthPositionProperty().get(), isCloseTo(0.26));
    }
}