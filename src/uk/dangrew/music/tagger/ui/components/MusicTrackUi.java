package uk.dangrew.music.tagger.ui.components;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import uk.dangrew.music.tagger.main.MusicController;
import uk.dangrew.music.tagger.main.MusicTrackConfiguration;
import uk.dangrew.music.tagger.ui.positioning.AbsolutePositioning;
import uk.dangrew.music.tagger.ui.positioning.CanvasDimensions;
import uk.dangrew.music.tagger.ui.positioning.CanvasLineRelativePositioning;
import uk.dangrew.music.tagger.ui.positioning.LinePortions;

/**
 * {@link MusicTrackUi} provides the ui component for the {@link uk.dangrew.music.tagger.main.MusicTrack} itself,
 * including skeleton, current position and scale.
 */
public class MusicTrackUi extends Pane {

    static final double WIDTH_PORTION = 0.5;
    static final double START_HEIGHT_PORTION = 0.1;
    static final double END_HEIGHT_PORTION = 0.9;

    private final Line skeleton;
    private final CanvasLineRelativePositioning canvasLineRelativePositioning;

    public MusicTrackUi(CanvasDimensions canvasDimensions, MusicController musicController, MusicTrackConfiguration configuration) {
        this.canvasLineRelativePositioning = new CanvasLineRelativePositioning(canvasDimensions);

        this.skeleton = new Line(0, 0, 0, 0);
        this.skeleton.setFill(Color.BLACK);
        this.skeleton.setStrokeWidth(5);
        this.getChildren().add(skeleton);

        this.canvasLineRelativePositioning.bind(skeleton, new LinePortions(
                new AbsolutePositioning(0.5),
                new AbsolutePositioning(0.5),
                new AbsolutePositioning(0.1),
                new AbsolutePositioning(0.9)
        ));

        this.getChildren().add(new MtTrackScale(canvasDimensions, configuration));
        this.getChildren().add(new MediaControlsUi(canvasDimensions, musicController));
        this.getChildren().add(new MtCurrentPosition(canvasDimensions, configuration));
    }

    Line skeleton() {
        return skeleton;
    }
}
