package uk.dangrew.music.tagger.ui.components.track;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import uk.dangrew.music.tagger.main.MusicTrackState;
import uk.dangrew.music.tagger.model.MusicTrack;
import uk.dangrew.music.tagger.ui.components.MusicController;
import uk.dangrew.music.tagger.ui.components.controls.MediaControlsUi;
import uk.dangrew.music.tagger.ui.positioning.AbsolutePositioning;
import uk.dangrew.music.tagger.ui.positioning.CanvasDimensions;
import uk.dangrew.music.tagger.ui.positioning.CanvasLineRelativePositioning;
import uk.dangrew.music.tagger.ui.positioning.LinePortions;

/**
 * {@link MusicTrackUi} provides the ui component for the {@link MusicTrack} itself,
 * including skeleton, current position and scale.
 */
public class MusicTrackUi extends Pane {

    static final double WIDTH_PORTION = 0.5;
    static final double START_HEIGHT_PORTION = 0.1;
    static final double END_HEIGHT_PORTION = 0.9;

    static final double MARKER_WIDTH_START_PORTION = 0.35;
    static final double MARKER_WIDTH_END_PORTION = 0.65;

    private final Line skeleton;
    private final Line leftHook;
    private final Line rightHook;
    private final CanvasLineRelativePositioning canvasLineRelativePositioning;

    public MusicTrackUi(CanvasDimensions canvasDimensions, MusicController musicController, MusicTrackState musicTrackState) {
        this.canvasLineRelativePositioning = new CanvasLineRelativePositioning(canvasDimensions);

        this.skeleton = new Line();
        this.skeleton.setFill(Color.BLACK);
        this.skeleton.setStrokeWidth(5);
        this.getChildren().add(skeleton);

        this.leftHook = new Line();
        this.leftHook.setFill(Color.BLACK);
        this.getChildren().add(leftHook);

        this.rightHook = new Line();
        this.rightHook.setFill(Color.BLACK);
        this.getChildren().add(rightHook);

        this.canvasLineRelativePositioning.bind(skeleton, new LinePortions(
                new AbsolutePositioning(WIDTH_PORTION),
                new AbsolutePositioning(WIDTH_PORTION),
                new AbsolutePositioning(START_HEIGHT_PORTION),
                new AbsolutePositioning(END_HEIGHT_PORTION)
        ));

        this.canvasLineRelativePositioning.bind(leftHook, new LinePortions(
                new AbsolutePositioning(MARKER_WIDTH_START_PORTION),
                new AbsolutePositioning(MARKER_WIDTH_START_PORTION),
                new AbsolutePositioning(START_HEIGHT_PORTION),
                new AbsolutePositioning(END_HEIGHT_PORTION)
        ));
        this.canvasLineRelativePositioning.bind(rightHook, new LinePortions(
                new AbsolutePositioning(MARKER_WIDTH_END_PORTION),
                new AbsolutePositioning(MARKER_WIDTH_END_PORTION),
                new AbsolutePositioning(START_HEIGHT_PORTION),
                new AbsolutePositioning(END_HEIGHT_PORTION)
        ));

        this.getChildren().add(new MtTrackScale(canvasDimensions, musicTrackState));
        this.getChildren().add(new MediaControlsUi(canvasDimensions, musicController, musicTrackState));
        this.getChildren().add(new MtCurrentPosition(canvasDimensions, musicTrackState));
    }

    Line skeleton() {
        return skeleton;
    }

    Line leftHook() {
        return leftHook;
    }

    Line rightHook() {
        return rightHook;
    }
}
