package uk.dangrew.music.tagger.ui;

/**
 * {@link LinePortions} provides a wrapper for {@link PortionProvider}s such that a {@link javafx.scene.shape.Line}
 * start and end points can be controlled by the associated conceptual canvas.
 */
public class LinePortions {

    private final PortionProvider startWidthProportion;
    private final PortionProvider endWidthProportion;

    private final PortionProvider startHeightProportion;
    private final PortionProvider endHeightProportion;

    public LinePortions(
            PortionProvider startWidthProportion,
            PortionProvider endWidthProportion,
            PortionProvider startHeightProportion,
            PortionProvider endHeightProportion
    ) {
        this.startWidthProportion = startWidthProportion;
        this.endWidthProportion = endWidthProportion;
        this.startHeightProportion = startHeightProportion;
        this.endHeightProportion = endHeightProportion;
    }

    public PortionProvider getStartWidthProvider() {
        return startWidthProportion;
    }

    public PortionProvider getEndWidthProvider() {
        return endWidthProportion;
    }

    public PortionProvider getStartHeightProvider() {
        return startHeightProportion;
    }

    public PortionProvider getEndHeightProvider() {
        return endHeightProportion;
    }
}
