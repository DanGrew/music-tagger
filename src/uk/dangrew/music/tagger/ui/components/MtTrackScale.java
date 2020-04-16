package uk.dangrew.music.tagger.ui.components;

import javafx.scene.layout.Pane;
import uk.dangrew.music.tagger.main.MusicTrackConfiguration;
import uk.dangrew.music.tagger.ui.positioning.CanvasDimensions;

import java.util.*;

/**
 * {@link MtTrackScale} - Music Track Scale providing the ui components for drawing the scale on the track indicating
 * the portions and segments of the track.
 */
public class MtTrackScale extends Pane {

    static final double MARKER_SEPARATION = 0.05;
    static final double SECONDS_PER_MARKER = 5;

    private final CanvasDimensions canvasDimensions;
    private final MusicTrackConfiguration configuration;

    private final List<MtTrackScaleMarker> markers;

    private MtTrackScaleMarker firstMarker;
    private MtTrackScaleMarker lastMarker;

    public MtTrackScale(CanvasDimensions canvasDimensions, MusicTrackConfiguration configuration) {
        this.canvasDimensions = canvasDimensions;
        this.configuration = configuration;

        this.markers = new ArrayList<>();
        this.plotScaleMarkers();
        this.firstMarker = markers.get(0);
        this.lastMarker = markers.get(markers.size() - 1);

        configuration.currentPositionProperty().addListener((s, o, n) -> {
            updateScaleMarkers(n.doubleValue() - o.doubleValue());
        });
        configuration.currentTimeProperty().addListener((s, o, n) -> {
            updateBasedOnDuration(n.doubleValue() - o.doubleValue());
        });
    }

    private void plotScaleMarkers() {
        getChildren().clear();

        double numberOfMarkers = (MtCurrentPosition.MAXIMUM_POSITION - MtCurrentPosition.MINIMUM_POSITION) / MARKER_SEPARATION;
        double markersBeforeCurrent = (configuration.currentPositionProperty().get() - MtCurrentPosition.MINIMUM_POSITION) / MARKER_SEPARATION;

        for (double markerIndex = 0 - markersBeforeCurrent; markerIndex < -markersBeforeCurrent + numberOfMarkers; markerIndex++) {
            double heightPortion = configuration.currentPositionProperty().get() + (markerIndex * MARKER_SEPARATION);
            double secondsThroughTrack = configuration.currentTimeProperty().get() + (markerIndex * SECONDS_PER_MARKER);

            MtTrackScaleMarker marker = new MtTrackScaleMarker(canvasDimensions, heightPortion, secondsThroughTrack);
            markers.add(marker);
            getChildren().add(marker);
        }
    }

    private void updateScaleMarkers(double delta) {
        for (MtTrackScaleMarker marker : markers) {
            marker.updateMarker(delta, 0);
        }

        if (lastMarker.heightPortionProperty().get() > MtCurrentPosition.MAXIMUM_POSITION) {
            for (MtTrackScaleMarker marker : markers) {
                marker.updateMarker(-MARKER_SEPARATION, -SECONDS_PER_MARKER);
            }
        }

        if (firstMarker.heightPortionProperty().get() < MtCurrentPosition.MINIMUM_POSITION) {
            for (MtTrackScaleMarker marker : markers) {
                marker.updateMarker(+MARKER_SEPARATION, +SECONDS_PER_MARKER);
            }
        }
    }

    private void updateBasedOnDuration(double secondsIncreased) {
        updateScaleMarkers(-secondsIncreased / SECONDS_PER_MARKER * MARKER_SEPARATION);
    }

    List<MtTrackScaleMarker> markers() {
        return Collections.unmodifiableList(markers);
    }
}
