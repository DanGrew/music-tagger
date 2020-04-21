package uk.dangrew.music.tagger.ui.positioning;

/**
 * {@link NodePortions} provides a wrapper for {@link PortionProvider}s such that a {@link javafx.scene.shape.Line}
 * start and end points can be controlled by the associated conceptual canvas.
 */
public class NodePortions {

    private final PortionProvider widthProportion;
    private final PortionProvider heightProportion;

    public NodePortions(
            PortionProvider widthProportion,
            PortionProvider heightProportion
    ) {
        this.widthProportion = widthProportion;
        this.heightProportion = heightProportion;
    }

    public PortionProvider getWidthProportion() {
        return widthProportion;
    }

    public PortionProvider getHeightProportion() {
        return heightProportion;
    }
}
