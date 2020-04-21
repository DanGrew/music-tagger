package uk.dangrew.music.tagger.ui.positioning;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Node;

import java.util.OptionalDouble;
import java.util.function.DoubleConsumer;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class NodePositioningTester {

    private final Node node;
    private final DoubleProperty width;
    private final DoubleProperty height;

    public NodePositioningTester(Node node, DoubleProperty width, DoubleProperty height){
        this.node = node;
        this.width = width;
        this.height = height;
    }

    public void assertThatNodeTranslatesWhenWidthDimensionChanges(double widthPortion){
        assertThat(node.getTranslateX(), is( 0.0));

        width.set(1000.0);
        assertThat(node.getTranslateX(), is( width.get() * widthPortion));
    }

    public void assertThatNodeTranslatesWhenHeightDimensionChanges(double heightPortion){
        assertThat(node.getTranslateY(), is( 0.0));

        height.set(500.0);
        assertThat(node.getTranslateY(), is( height.get() * heightPortion));
    }

    public void assertThatWidthPositionRecalculatesWhenPropertiesChange(ReadOnlyDoubleProperty widthProperty, DoubleConsumer widthUpdater){
        width.set(1000.0);
        assertThat(node.getTranslateX(), is( width.get() * widthProperty.get()));

        widthUpdater.accept(0.6);
        assertThat(node.getTranslateX(), is( width.get() * widthProperty.get()));
    }

    public void assertThatHeightPositionRecalculatesWhenPropertiesChange(ReadOnlyDoubleProperty heightProperty, DoubleConsumer heightUpdater){
        height.set(1000.0);
        assertThat(node.getTranslateY(), is( height.get() * heightProperty.get()));

        heightUpdater.accept(0.6);
        assertThat(node.getTranslateY(), is( height.get() * heightProperty.get()));
    }

    public void assertThatNodeDoesNotTranslateWhenWidthDimensionChanges(double widthPortion){
        assertThat(node.getTranslateX(), is( 0.0));

        width.set(1000.0);
        assertThat(node.getTranslateX(), is( not(width.get() * widthPortion)));
    }

    public void assertThatNodeDoesNotTranslateWhenHeightDimensionChanges(double heightPortion){
        assertThat(node.getTranslateY(), is( 0.0));

        height.set(500.0);
        assertThat(node.getTranslateY(), is( not(height.get() * heightPortion)));
    }

    public void assertThatWidthPositionDoesNotRecalculateWhenPropertiesChange(ReadOnlyDoubleProperty widthProperty, DoubleConsumer widthUpdater){
        width.set(1000.0);
        widthUpdater.accept(0.1);
        double previous = width.get() * widthProperty.get();

        widthUpdater.accept(0.6);
        assertThat(node.getTranslateX(), is( not(previous)));
    }

    public void assertThatHeightPositionDoesNotRecalculateWhenPropertiesChange(ReadOnlyDoubleProperty heightProperty, DoubleConsumer heightUpdater){
        height.set(1000.0);
        heightUpdater.accept(0.1);
        double previous = height.get() * heightProperty.get();

        heightUpdater.accept(0.6);
        assertThat(node.getTranslateY(), is( not(previous)));
    }
}
