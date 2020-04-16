package uk.dangrew.music.tagger.ui.positioning;

import javafx.beans.property.ReadOnlyDoubleProperty;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * {@link CanvasDimensions} provides an easy method for accessing the dimensions of a conceptual canvas, in terms of width and height.
 */
public class CanvasDimensions {

    private final Set<Consumer<Double>> widthHandlers;
    private final Set<Consumer<Double>> heightHandlers;

    private final ReadOnlyDoubleProperty width;
    private final ReadOnlyDoubleProperty height;

    public CanvasDimensions(
            ReadOnlyDoubleProperty width,
            ReadOnlyDoubleProperty height
    ){
        this.widthHandlers = new LinkedHashSet<>();
        this.heightHandlers = new LinkedHashSet<>();

        this.width = width;
        this.height = height;

        width.addListener((s,o,n) -> widthHandlers.forEach( handler -> handler.accept(n.doubleValue()-o.doubleValue())));
        height.addListener((s,o,n) -> heightHandlers.forEach( handler -> handler.accept(n.doubleValue()-o.doubleValue())));
    }

    public void registerForWidthChange(Consumer<Double> handler){
        this.widthHandlers.add(handler);
    }

    public void registerForHeightChange(Consumer<Double> handler){
        this.heightHandlers.add(handler);
    }

    public double width(){
        return width.get();
    }

    public double height(){
        return height.get();
    }

    public double halfWidth(){
        return width.get() / 2;
    }

    public double halfHeight(){
        return height.get() / 2;
    }
}
