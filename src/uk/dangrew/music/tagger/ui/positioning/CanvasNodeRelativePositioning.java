package uk.dangrew.music.tagger.ui.positioning;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

/**
 * {@link CanvasNodeRelativePositioning} provides a method of positioning {@link Node}s on a conceptual canvas based on {@link PortionProvider}s.
 */
public class CanvasNodeRelativePositioning {

    private final CanvasDimensions canvasDimensions;
    private final Map<Region, NodePortions> regionAlignments;
    private final Map<NodePortions, List<Consumer<Double>>> registrations;

    public CanvasNodeRelativePositioning(
            CanvasDimensions canvasDimensions
    ) {
        this.canvasDimensions = canvasDimensions;
        this.regionAlignments = new LinkedHashMap<>();
        this.registrations = new LinkedHashMap<>();

        this.canvasDimensions.registerForWidthChange(delta -> handleWidthChange());
        this.canvasDimensions.registerForHeightChange(delta -> handleHeightChange());
    }

    public void bind(Region node, PortionProvider portionOfWidth, PortionProvider portionOfHeight) {
        bind(node, new NodePortions(portionOfWidth, portionOfHeight));
    }

    public void bind(Region node, NodePortions nodePortions){
        if ( regionAlignments.containsKey(node)){
            return;
        }
        regionAlignments.put(node, nodePortions);

        List<Consumer<Double>> linePortionRegistrations = registrations.computeIfAbsent(nodePortions, functions -> new ArrayList<>());
        Consumer<Double> widthRegistration = update -> recalculateWidth(node, nodePortions.getWidthProportion());
        Consumer<Double> heightRegistration = update -> recalculateHeight(node, nodePortions.getHeightProportion());
        linePortionRegistrations.add(widthRegistration);
        linePortionRegistrations.add(heightRegistration);

        nodePortions.getWidthProportion().registerForUpdates(widthRegistration);
        nodePortions.getHeightProportion().registerForUpdates(heightRegistration);

        //note: could be a memory leak
        node.widthProperty().addListener((s, o, n) -> widthRegistration.accept(n.doubleValue()));
        node.heightProperty().addListener((s, o, n) -> heightRegistration.accept(n.doubleValue()));
    }

    public void unbind(Region node) {
        if ( !regionAlignments.containsKey(node)){
            return;
        }

        NodePortions nodePortions = regionAlignments.remove(node);
        List<Consumer<Double>> nodePortionRegistrations = registrations.remove(nodePortions);
        nodePortions.getWidthProportion().unregister(nodePortionRegistrations.get(0));
        nodePortions.getHeightProportion().unregister(nodePortionRegistrations.get(1));
    }

    private void handleWidthChange() {
        for (Entry<Region, NodePortions> entry : regionAlignments.entrySet()) {
            recalculateWidth(entry.getKey(), entry.getValue().getWidthProportion());
        }
    }

    private void handleHeightChange() {
        for (Entry<Region, NodePortions> entry : regionAlignments.entrySet()) {
            recalculateHeight(entry.getKey(), entry.getValue().getHeightProportion());
        }
    }

    private void recalculateWidth(Region region, PortionProvider portionProvider){
        if (portionProvider == null) {
            return;
        }
        double xTranslation = portionProvider.getPositioning() * canvasDimensions.width();
        xTranslation -= region.getWidth() / 2;
        region.setTranslateX(xTranslation);
    }

    private void recalculateHeight(Region region, PortionProvider portionProvider){
        if (portionProvider == null) {
            return;
        }
        double yTranslation = portionProvider.getPositioning() * canvasDimensions.height();
        yTranslation -= region.getHeight() / 2;
        region.setTranslateY(yTranslation);
    }
}
