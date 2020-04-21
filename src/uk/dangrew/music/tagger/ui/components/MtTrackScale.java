package uk.dangrew.music.tagger.ui.components;

import javafx.scene.layout.Pane;
import uk.dangrew.music.tagger.main.MtTrackScaleMarkerCalculator;
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
    private final MtTrackScaleMarkerCalculator positionCalculator;

    private final List<MtTrackScaleMarker> markers;

    public MtTrackScale(CanvasDimensions canvasDimensions, MusicTrackConfiguration configuration) {
        this.canvasDimensions = canvasDimensions;
        this.configuration = configuration;
        this.positionCalculator = new MtTrackScaleMarkerCalculator(configuration);

        this.markers = new ArrayList<>();
        this.plotMarkers();
        this.updateMarkers();

        configuration.currentPositionProperty().addListener((s, o, n) -> {
            updateMarkers();
        });
        configuration.currentTimeProperty().addListener((s, o, n) -> {
            updateMarkers();
        });
    }

    private void plotMarkers() {
        getChildren().clear();
        markers.forEach(MtTrackScaleMarker::detach);
        markers.clear();

        double numberOfMarkers = (MtCurrentPosition.MAXIMUM_POSITION - MtCurrentPosition.MINIMUM_POSITION) / configuration.scalePositionIntervalProperty().get();

        for (int markerIndex = 0; markerIndex < numberOfMarkers; markerIndex++) {
            MtTrackScaleMarker marker = new MtTrackScaleMarker(canvasDimensions);
            markers.add(marker);
            getChildren().add(marker);
        }
    }

    private void updateMarkers(){
        double heightPortionOffset = positionCalculator.calculateCurrentMarkerPositionOffset();
        double scaleStartSeconds = positionCalculator.calculateScaleStartSeconds();

        for (int markerIndex = 0; markerIndex < markers().size(); markerIndex++) {
            MtTrackScaleMarker marker = markers.get(markerIndex);

            double heightPortion = MtCurrentPosition.MINIMUM_POSITION + (markerIndex * configuration.scalePositionIntervalProperty().get()) + heightPortionOffset;
            marker.setPosition(heightPortion);

            double markerSeconds = scaleStartSeconds + (markerIndex * configuration.scaleTimeIntervalProperty().get());
            marker.setSeconds(markerSeconds);
        }
    }

    List<MtTrackScaleMarker> markers() {
        return Collections.unmodifiableList(markers);
    }
}
