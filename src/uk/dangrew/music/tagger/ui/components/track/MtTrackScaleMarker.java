package uk.dangrew.music.tagger.ui.components.track;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import uk.dangrew.music.tagger.model.MusicTimestamp;
import uk.dangrew.music.tagger.ui.positioning.*;

/**
 * {@link MtTrackScaleMarker} - Music Track Scale Marker provides a marker in the form of a {@link Line} and a {@link
 * Label}, to indicate a marker on the {@link MtTrackScale} for a portion or segment of the track.
 */
public class MtTrackScaleMarker extends Pane {

    static final double MARKER_WIDTH_START_PORTION = 0.48;
    static final double MARKER_WIDTH_END_PORTION = 0.52;
    static final double LABEL_WIDTH_PORTION = 0.3;

    private final CanvasLineRelativePositioning canvasLineRelativePositioning;
    private final CanvasRegionRelativePositioning canvasRegionRelativePositioning;
    private final DoubleProperty heightPortionProperty;
    private final Line marker;
    private final Label label;

    public MtTrackScaleMarker(
            CanvasDimensions canvasDimensions
    ) {
        this.marker = new Line(0, 0, 0, 0);
        this.heightPortionProperty = new SimpleDoubleProperty(0);
        this.label = new Label(MusicTimestamp.format(0));

        this.getChildren().add(marker);
        this.getChildren().add(label);

        RelativePositioning heightPosition = new RelativePositioning(heightPortionProperty);
        this.canvasLineRelativePositioning = new CanvasLineRelativePositioning(canvasDimensions);
        this.canvasLineRelativePositioning.bind(marker, new LinePortions(
                new AbsolutePositioning(MARKER_WIDTH_START_PORTION),
                new AbsolutePositioning(MARKER_WIDTH_END_PORTION),
                heightPosition,
                heightPosition
        ));

        this.canvasRegionRelativePositioning = new CanvasRegionRelativePositioning(canvasDimensions);
        this.canvasRegionRelativePositioning.bind(label, new AbsolutePositioning(LABEL_WIDTH_PORTION), new RelativePositioning(heightPortionProperty));
    }

    ReadOnlyDoubleProperty heightPortionProperty() {
        return heightPortionProperty;
    }

    public void setPosition(double portion) {
        heightPortionProperty.set(portion);
    }

    public void setSeconds(double secondsThroughTrack) {
        label.setText(MusicTimestamp.format(secondsThroughTrack));
    }

    public void detach(){
        canvasLineRelativePositioning.unbind(marker);
        canvasRegionRelativePositioning.unbind(label);
    }

    Label label() {
        return label;
    }

    Line marker() {
        return marker;
    }

}
