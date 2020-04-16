package uk.dangrew.music.tagger.ui.components;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import uk.dangrew.kode.friendly.javafx.FriendlyMouseEvent;
import uk.dangrew.music.tagger.main.MusicTrackConfiguration;
import uk.dangrew.music.tagger.ui.positioning.*;

/**
 * {@link MtCurrentPosition} - Music Track Current Position provides the ui component for the current position of the
 * track which can be moved.
 */
public class MtCurrentPosition extends Line {

    public static final double MINIMUM_POSITION = 0.1;
    public static final double MAXIMUM_POSITION = 0.9;
    public static final double WIDTH_START_PORTION = 0.3;
    public static final double WIDTH_END_PORTION = 0.7;

    private final CanvasDimensions canvasDimensions;
    private final MusicTrackConfiguration musicTrackConfiguration;

    public MtCurrentPosition(
            CanvasDimensions canvasDimensions,
            MusicTrackConfiguration configuration
    ) {
        super(0, 0, 0, 0);
        this.canvasDimensions = canvasDimensions;
        this.musicTrackConfiguration = configuration;

        this.setStroke(Color.BLUE);
        this.setStrokeWidth(5);

        CanvasLineRelativePositioning canvasLineRelativePositioning = new CanvasLineRelativePositioning(canvasDimensions);
        RelativePositioning heightPositioning = new RelativePositioning(configuration.currentPositionProperty(), MINIMUM_POSITION, MAXIMUM_POSITION);
        canvasLineRelativePositioning.bind(this, new LinePortions(
                new AbsolutePositioning(WIDTH_START_PORTION),
                new AbsolutePositioning(WIDTH_END_PORTION),
                heightPositioning,
                heightPositioning
        ));

        this.setOnMouseDragged(event -> mouseDragged(new FriendlyMouseEvent(event)));
    }

    void mouseDragged(FriendlyMouseEvent event) {
        double height = canvasDimensions.height();
        double portionAdjusted = event.getY() / height;
        musicTrackConfiguration.currentPositionProperty().set(portionAdjusted);
    }
}
