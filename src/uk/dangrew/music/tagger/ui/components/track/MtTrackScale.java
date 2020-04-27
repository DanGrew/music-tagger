package uk.dangrew.music.tagger.ui.components.track;

import javafx.scene.layout.Pane;
import uk.dangrew.music.tagger.main.MusicTrackState;
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
    private final MusicTrackState musicTrackState;
    private final MtTrackScaleMarkerCalculator positionCalculator;

    private final List<MtTrackScaleMarker> markers;

    public MtTrackScale(CanvasDimensions canvasDimensions, MusicTrackState musicTrackState) {
        this.canvasDimensions = canvasDimensions;
        this.musicTrackState = musicTrackState;
        this.positionCalculator = new MtTrackScaleMarkerCalculator(musicTrackState);

        this.markers = new ArrayList<>();
        this.plotMarkers();
        this.updateMarkers();

        musicTrackState.currentPositionProperty().addListener((s, o, n) -> {
            updateMarkers();
        });
        musicTrackState.currentTimeProperty().addListener((s, o, n) -> {
            updateMarkers();
        });
    }

    private void plotMarkers() {
        getChildren().clear();
        markers.forEach(MtTrackScaleMarker::detach);
        markers.clear();
        updateMarkerCount();
    }

    private void updateMarkerCount(){
        double numberOfMarkers = (MtCurrentPosition.MAXIMUM_POSITION - MtCurrentPosition.MINIMUM_POSITION) / musicTrackState.scalePositionIntervalProperty().get();
        double heightPortionOffset = positionCalculator.calculateCurrentMarkerPositionOffset();
        numberOfMarkers = Math.floor(numberOfMarkers + heightPortionOffset);
        if ( numberOfMarkers > markers.size() ) {
            for (int markerIndex = markers.size(); markerIndex < numberOfMarkers; markerIndex++) {
                MtTrackScaleMarker marker = new MtTrackScaleMarker(canvasDimensions);
                markers.add(marker);
                getChildren().add(marker);
            }
        } else if (numberOfMarkers < markers.size() ){
            double markersToRemove = markers.size() - numberOfMarkers;
            for (int markerIndex = 0; markerIndex < markersToRemove; markerIndex++){
                MtTrackScaleMarker marker = markers.remove(markers.size() -1 );
                marker.detach();
                getChildren().remove(marker);
            }
        }
    }

    private void updateMarkers(){
        updateMarkerCount();

        double heightPortionOffset = positionCalculator.calculateCurrentMarkerPositionOffset();
        double scaleStartSeconds = positionCalculator.calculateScaleStartSeconds();

        for (int markerIndex = 0; markerIndex < markers().size(); markerIndex++) {
            MtTrackScaleMarker marker = markers.get(markerIndex);

            double heightPortion = MtCurrentPosition.MINIMUM_POSITION + (markerIndex * musicTrackState.scalePositionIntervalProperty().get()) + heightPortionOffset;
            marker.setPosition(heightPortion);

            double markerSeconds = scaleStartSeconds + (markerIndex * musicTrackState.scaleTimeIntervalProperty().get());
            marker.setSeconds(markerSeconds);
        }
    }

    List<MtTrackScaleMarker> markers() {
        return Collections.unmodifiableList(markers);
    }
}
