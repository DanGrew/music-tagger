package uk.dangrew.music.tagger.ui.components;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import uk.dangrew.music.tagger.main.MusicTimestamp;
import uk.dangrew.music.tagger.ui.positioning.*;

/**
 * {@link MtTrackScaleMarker} - Music Track Scale Marker provides a marker in the form of a {@link Line} and a {@link
 * Label}, to indicate a marker on the {@link MtTrackScale} for a portion or segment of the track.
 */
public class MtTrackScaleMarker extends Pane {

    static final double MARKER_WIDTH_START_PORTION = 0.48;
    static final double MARKER_WIDTH_END_PORTION = 0.52;
    static final double LABEL_WIDTH_PORTION = 0.3;

    private final DoubleProperty heightPortionProperty;
    private final Line marker;
    private final Label label;
    private double secondsThroughTrack;

    public MtTrackScaleMarker(
            CanvasDimensions canvasDimensions,
            double initialHeightProportion,
            double secondsThroughTrack
    ) {
        this.marker = new Line(0, 0, 0, 0);
        this.heightPortionProperty = new SimpleDoubleProperty(initialHeightProportion);
        this.label = new Label(MusicTimestamp.format(secondsThroughTrack));
        this.secondsThroughTrack = secondsThroughTrack;

        this.getChildren().add(marker);
        this.getChildren().add(label);

        RelativePositioning heightPosition = new RelativePositioning(heightPortionProperty);
        new CanvasLineRelativePositioning(canvasDimensions).bind(marker, new LinePortions(
                new AbsolutePositioning(MARKER_WIDTH_START_PORTION),
                new AbsolutePositioning(MARKER_WIDTH_END_PORTION),
                heightPosition,
                heightPosition
        ));

        new CanvasNodeRelativePositioning(canvasDimensions).bind(label, new AbsolutePositioning(LABEL_WIDTH_PORTION), new RelativePositioning(heightPortionProperty));
    }

    public ReadOnlyDoubleProperty heightPortionProperty() {
        return heightPortionProperty;
    }

    public void updateMarker(double portionDelta, double secondsThroughTrackDelta) {
        heightPortionProperty.set(heightPortionProperty.get() + portionDelta);
        secondsThroughTrack += secondsThroughTrackDelta;
        label.setText(MusicTimestamp.format(secondsThroughTrack));
    }

    Label label() {
        return label;
    }

    Line marker() {
        return marker;
    }

}
