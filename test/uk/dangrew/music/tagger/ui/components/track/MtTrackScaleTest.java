package uk.dangrew.music.tagger.ui.components.track;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.junit.Before;
import org.junit.Test;
import uk.dangrew.kode.TestCommon;
import uk.dangrew.kode.launch.TestApplication;
import uk.dangrew.music.tagger.model.MusicTimestamp;
import uk.dangrew.music.tagger.main.MusicTrackState;
import uk.dangrew.music.tagger.ui.positioning.CanvasDimensions;

import java.util.List;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class MtTrackScaleTest {

    private DoubleProperty width;
    private DoubleProperty height;

    private MusicTrackState musicTrackState;
    private MtTrackScale systemUnderTest;

    @Before
    public void initialiseSystemUnderTest() {
        TestApplication.startPlatform();
        initMocks(this);
        musicTrackState = new MusicTrackState();
        musicTrackState.currentPositionProperty().set(0.4);
        musicTrackState.currentTimeProperty().set(5.0);
        musicTrackState.scalePositionIntervalProperty().set(0.05);
        musicTrackState.scaleTimeIntervalProperty().set(5);
        systemUnderTest = new MtTrackScale(
                new CanvasDimensions(width = new SimpleDoubleProperty(), height = new SimpleDoubleProperty()),
                musicTrackState
        );
    }

    @Test
    public void shouldAdjustMarkersBasedOnCurrentPositionChange() {
        musicTrackState.currentPositionProperty().set(0.5);

        assertMarkersAreFocussedAround(0.10, -35);
    }

    @Test
    public void shouldAdjustMarkersWhenHitMaximum() {
        List<MtTrackScaleMarker> markers = systemUnderTest.markers();
        assertThat(markers, hasSize(16));

        musicTrackState.currentTimeProperty().set(3.0);
        assertMarkersAreFocussedAround(0.12, -25);

        musicTrackState.currentTimeProperty().set(2.0);
        assertMarkersAreFocussedAround(0.13, -25);

        musicTrackState.currentTimeProperty().set(-1.0);
        assertMarkersAreFocussedAround(0.11, -35);
    }

    @Test
    public void shouldAdjustMarkersWhenHitMinimum() {
        List<MtTrackScaleMarker> markers = systemUnderTest.markers();
        assertThat(markers, hasSize(16));

        musicTrackState.currentTimeProperty().set(8.0);
        assertMarkersAreFocussedAround(0.12, -20);

        musicTrackState.currentTimeProperty().set(9.0);
        assertMarkersAreFocussedAround(0.11, -20);

        musicTrackState.currentTimeProperty().set(11.0);
        assertMarkersAreFocussedAround(0.14, -15);
    }

    @Test
    public void shouldProvideMarkersBeforeCurrentAndAfterCurrentPositionAndTime() {
        assertMarkersAreFocussedAround(0.1, -25);
    }

    private void assertMarkersAreFocussedAround(double startPortion, double seconds) {
        List<MtTrackScaleMarker> markers = systemUnderTest.markers();
        assertThat(markers, hasSize(16));

        for (int i = 0; i < 16; i++) {
            assertMarkerIsCorrect(
                    markers.get(i),
                    startPortion + (i * MtTrackScale.MARKER_SEPARATION),
                    MusicTimestamp.format(seconds + (i * MtTrackScale.SECONDS_PER_MARKER)
                    ));
        }
    }

    private void assertMarkerIsCorrect(MtTrackScaleMarker marker, double portion, String time) {
        assertThat(marker.heightPortionProperty().get(), is(closeTo(portion, TestCommon.precision())));
        assertThat(marker.label().getText(), is(time));
    }

}