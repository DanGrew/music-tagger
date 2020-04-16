package uk.dangrew.music.tagger.ui.positioning;

import javafx.scene.shape.Line;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * {@link CanvasLineRelativePositioning} provides a method of positioning {@link Line}s on a conceptual canvas based on {@link PortionProvider}s.
 */
public class CanvasLineRelativePositioning {

    private final CanvasDimensions canvasDimensions;
    private final Map<Line, LinePortions> linePortions;

    public CanvasLineRelativePositioning(
            CanvasDimensions canvasDimensions
    ) {
        this.canvasDimensions = canvasDimensions;
        this.linePortions = new LinkedHashMap<>();

        this.canvasDimensions.registerForWidthChange(this::handleWidthChange);
        this.canvasDimensions.registerForHeightChange(this::handleHeightChange);
    }

    public void bind(Line node, LinePortions portionProvider) {
        this.linePortions.put(node, portionProvider);
        portionProvider.getStartWidthProvider().registerForUpdates(update -> recalculateWidthStart(node, portionProvider));
        portionProvider.getEndWidthProvider().registerForUpdates(update -> recalculateWidthEnd(node, portionProvider));
        portionProvider.getStartHeightProvider().registerForUpdates(update -> recalculateHeightStart(node, portionProvider));
        portionProvider.getEndHeightProvider().registerForUpdates(update -> recalculateHeightEnd(node, portionProvider));
    }

    private void handleWidthChange(double widthDelta) {
        for (Entry<Line, LinePortions> entry : linePortions.entrySet()) {
            recalculateWidthStart(entry.getKey(), entry.getValue());
            recalculateWidthEnd(entry.getKey(), entry.getValue());
        }
    }

    private void handleHeightChange(double heightDelta) {
        for (Entry<Line, LinePortions> entry : linePortions.entrySet()) {
            recalculateHeightStart(entry.getKey(), entry.getValue());
            recalculateHeightEnd(entry.getKey(), entry.getValue());
        }
    }

    private void recalculateWidthStart(Line line, LinePortions portionProvider) {
        double width = canvasDimensions.width();

        double startPosition = portionProvider.getStartWidthProvider().getPositioning() * width;
        line.setStartX(startPosition);
    }

    private void recalculateWidthEnd(Line line, LinePortions portionProvider){
        double width = canvasDimensions.width();

        double endPosition = portionProvider.getEndWidthProvider().getPositioning() * width;
        line.setEndX(endPosition);
    }

    private void recalculateHeightStart(Line line, LinePortions portionProvider) {
        double height = canvasDimensions.height();

        double startPosition = portionProvider.getStartHeightProvider().getPositioning() * height;
        line.setStartY(startPosition);
    }

    private void recalculateHeightEnd(Line line, LinePortions portionProvider){
        double height = canvasDimensions.height();

        double endPosition = portionProvider.getEndHeightProvider().getPositioning() * height;
        line.setEndY(endPosition);
    }

}
