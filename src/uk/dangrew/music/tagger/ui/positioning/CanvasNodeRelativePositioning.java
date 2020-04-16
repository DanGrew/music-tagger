package uk.dangrew.music.tagger.ui.positioning;

import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.util.Pair;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * {@link CanvasNodeRelativePositioning} provides a method of positioning {@link Node}s on a conceptual canvas based on {@link PortionProvider}s.
 */
public class CanvasNodeRelativePositioning {

    private final CanvasDimensions canvasDimensions;
    private final Map<Region, Pair<PortionProvider, PortionProvider>> regionAlignments;

    public CanvasNodeRelativePositioning(
            CanvasDimensions canvasDimensions
    ) {
        this.canvasDimensions = canvasDimensions;
        this.regionAlignments = new LinkedHashMap<>();

        this.canvasDimensions.registerForWidthChange(this::handleWidthChange);
        this.canvasDimensions.registerForHeightChange(this::handleHeightChange);
    }

    public void bind(Region node, PortionProvider portionOfWidth, PortionProvider portionOfHeight) {
        regionAlignments.put(node, new Pair<>(portionOfWidth, portionOfHeight));
        portionOfWidth.registerForUpdates(update -> recalculateWidth(portionOfWidth, node));
        portionOfHeight.registerForUpdates(update -> recalculateHeight(portionOfHeight, node));
    }

    private void handleWidthChange(double widthDelta) {
        for (Entry<Region, Pair<PortionProvider, PortionProvider>> entry : regionAlignments.entrySet()) {
            recalculateWidth(entry.getValue().getKey(), entry.getKey());
        }
    }

    private void handleHeightChange(double heightDelta) {
        for (Entry<Region, Pair<PortionProvider, PortionProvider>> entry : regionAlignments.entrySet()) {
            recalculateHeight(entry.getValue().getValue(), entry.getKey());
        }

    }

    private void recalculateWidth(PortionProvider portionProvider, Region region){
        if (portionProvider == null) {
            return;
        }
        double xTranslation = portionProvider.getPositioning() * canvasDimensions.width();
        xTranslation -= region.getWidth() / 2;
        region.setTranslateX(xTranslation);
    }

    private void recalculateHeight(PortionProvider portionProvider, Region region){
        if (portionProvider == null) {
            return;
        }
        double yTranslation = portionProvider.getPositioning() * canvasDimensions.height();
        yTranslation -= region.getHeight() / 2;
        region.setTranslateY(yTranslation);
    }

}
