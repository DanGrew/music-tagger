package uk.dangrew.music.tagger.ui.positioning;

import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

/**
 * {@link CanvasLineRelativePositioning} provides a method of positioning {@link Line}s on a conceptual canvas based on {@link PortionProvider}s.
 */
public class CanvasLineRelativePositioning {

    private final CanvasDimensions canvasDimensions;
    private final Map<Line, LinePortions> linePortions;
    private final Map<LinePortions, List<Consumer<Double>>> registrations;

    public CanvasLineRelativePositioning(
            CanvasDimensions canvasDimensions
    ) {
        this.canvasDimensions = canvasDimensions;
        this.linePortions = new LinkedHashMap<>();
        this.registrations = new LinkedHashMap<>();

        this.canvasDimensions.registerForWidthChange(this::handleWidthChange);
        this.canvasDimensions.registerForHeightChange(this::handleHeightChange);
    }

    public void bind(Line node, LinePortions portionProvider) {
        if ( linePortions.containsKey(node)){
            return;
        }
        this.linePortions.put(node, portionProvider);

        List<Consumer<Double>> linePortionRegistrations = registrations.computeIfAbsent(portionProvider, functions -> new ArrayList<>());
        Consumer<Double> widthStartRegistration = update -> recalculateWidthStart(node, portionProvider);
        Consumer<Double> widthEndRegistration = update -> recalculateWidthEnd(node, portionProvider);
        Consumer<Double> heightStartRegistration = update -> recalculateHeightStart(node, portionProvider);
        Consumer<Double> heightEndRegistration = update -> recalculateHeightEnd(node, portionProvider);
        linePortionRegistrations.add(widthStartRegistration);
        linePortionRegistrations.add(widthEndRegistration);
        linePortionRegistrations.add(heightStartRegistration);
        linePortionRegistrations.add(heightEndRegistration);

        portionProvider.getStartWidthProvider().registerForUpdates(widthStartRegistration);
        portionProvider.getEndWidthProvider().registerForUpdates(widthEndRegistration);
        portionProvider.getStartHeightProvider().registerForUpdates(heightStartRegistration);
        portionProvider.getEndHeightProvider().registerForUpdates(heightEndRegistration);
    }

    public void unbind(Line line) {
        if ( !linePortions.containsKey(line)){
            return;
        }

        LinePortions currentLinePortions = linePortions.remove(line);
        List<Consumer<Double>> currentLineRegistrations = registrations.get(currentLinePortions);
        currentLinePortions.getStartWidthProvider().unregister(currentLineRegistrations.get(0));
        currentLinePortions.getEndWidthProvider().unregister(currentLineRegistrations.get(1));
        currentLinePortions.getStartHeightProvider().unregister(currentLineRegistrations.get(2));
        currentLinePortions.getEndHeightProvider().unregister(currentLineRegistrations.get(3));
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
