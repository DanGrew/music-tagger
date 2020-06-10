package uk.dangrew.music.tagger.ui.positioning;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.shape.Line;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.DoubleConsumer;
import java.util.function.Function;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LinePositioningTester {

    private final DoubleProperty width;
    private final DoubleProperty height;

    private final Line node;

    public LinePositioningTester(Line node, DoubleProperty width, DoubleProperty height) {
        this.width = width;
        this.height = height;
        this.node = node;
    }

    public void assertThatFixedWidthIsRespectedWhenDimensionChanges(
            OptionalDouble startWidthPortion,
            OptionalDouble endWidthPortion
    ) {
        width.set(1000.0);
        startWidthPortion.ifPresent(portion -> assertThat(node.getStartX(), is(portion * width.get())));
        endWidthPortion.ifPresent(portion -> assertThat(node.getEndX(), is(portion * width.get())));

        height.set(500.0);
        startWidthPortion.ifPresent(portion -> assertThat(node.getStartX(), is(portion * width.get())));
        endWidthPortion.ifPresent(portion -> assertThat(node.getEndX(), is(portion * width.get())));
    }

    public void assertThatFixedHeightIsRespectedWhenDimensionChanges(
            OptionalDouble startHeightPortion,
            OptionalDouble endHeightPortion
    ) {
        width.set(1000.0);
        startHeightPortion.ifPresent(portion -> assertThat(node.getStartY(), is(portion * height.get())));
        endHeightPortion.ifPresent(portion -> assertThat(node.getEndY(), is(portion * height.get())));

        height.set(500.0);
        startHeightPortion.ifPresent(portion -> assertThat(node.getStartY(), is(portion * height.get())));
        endHeightPortion.ifPresent(portion -> assertThat(node.getEndY(), is(portion * height.get())));
    }

    public void assertThatRelativeWidthIsRespectedWhenDimensionChanges(
            Optional<DoubleProperty> startWidthProperty,
            Optional<DoubleProperty> endWidthProperty
    ) {
        width.set(1000.0);

        startWidthProperty.ifPresent(property -> assertThat(node.getStartX(), is(property.get() * width.get())));
        endWidthProperty.ifPresent(property -> assertThat(node.getEndX(), is(property.get() * width.get())));

        startWidthProperty.ifPresent(property -> property.set(0.21));
        startWidthProperty.ifPresent(property -> assertThat(node.getStartX(), is(property.get() * width.get())));
        endWidthProperty.ifPresent(property -> property.set(0.31));
        endWidthProperty.ifPresent(property -> assertThat(node.getEndX(), is(property.get() * width.get())));
    }

    public void assertThatRelativeHeightIsRespectedWhenDimensionChanges(
            Optional<ReadOnlyDoubleProperty> startHeightProperty,
            Optional<DoubleConsumer> startHeightPropertySetter,
            Optional<ReadOnlyDoubleProperty> endHeightProperty,
            Optional<DoubleConsumer> endHeightPropertySetter
    ) {
        height.set(500.0);

        startHeightProperty.ifPresent(property -> assertThat(node.getStartY(), is(property.get() * height.get())));
        endHeightProperty.ifPresent(property -> assertThat(node.getEndY(), is(property.get() * height.get())));

        startHeightPropertySetter.ifPresent(setter -> setter.accept(0.41));
        startHeightProperty.ifPresent(property -> assertThat(node.getStartY(), is(property.get() * height.get())));
        endHeightPropertySetter.ifPresent(setter -> setter.accept(0.53));
        endHeightProperty.ifPresent(property -> assertThat(node.getEndY(), is(property.get() * height.get())));
    }

    public void assertThatLineDoesNotTranslateWhenWidthDimensionChanges(
            OptionalDouble startWidthPortion,
            OptionalDouble endWidthPortion
    ) {
        width.set(1000.0);
        startWidthPortion.ifPresent(portion -> assertThat(node.getStartX(), is(not(portion * width.get()))));
        endWidthPortion.ifPresent(portion -> assertThat(node.getEndX(), is(not(portion * width.get()))));

        height.set(500.0);
        startWidthPortion.ifPresent(portion -> assertThat(node.getStartX(), is(not(portion * width.get()))));
        endWidthPortion.ifPresent(portion -> assertThat(node.getEndX(), is(not(portion * width.get()))));
    }

    public void assertThatLineDoesNotTranslateWhenHeightDimensionChanges(
            OptionalDouble startHeightPortion,
            OptionalDouble endHeightPortion
    ) {
        width.set(1000.0);
        startHeightPortion.ifPresent(portion -> assertThat(node.getStartY(), is(not(portion * height.get()))));
        endHeightPortion.ifPresent(portion -> assertThat(node.getEndY(), is(not(portion * height.get()))));

        height.set(500.0);
        startHeightPortion.ifPresent(portion -> assertThat(node.getStartY(), is(not(portion * height.get()))));
        endHeightPortion.ifPresent(portion -> assertThat(node.getEndY(), is(not(portion * height.get()))));
    }

    public void assertThatPositionDoesNotRecalculateWhenWidthPropertiesChange(
            Optional<DoubleProperty> startWidthProperty,
            Optional<DoubleProperty> endWidthProperty
    ) {
        width.set(1000.0);

        startWidthProperty.ifPresent(property -> assertThat(node.getStartX(), is(not(property.get() * width.get()))));
        endWidthProperty.ifPresent(property -> assertThat(node.getEndX(), is(not(property.get() * width.get()))));

        startWidthProperty.ifPresent(property -> property.set(0.21));
        startWidthProperty.ifPresent(property -> assertThat(node.getStartX(), is(not(property.get() * width.get()))));
        endWidthProperty.ifPresent(property -> property.set(0.31));
        endWidthProperty.ifPresent(property -> assertThat(node.getEndX(), is(not(property.get() * width.get()))));
    }

    public void assertThatPositionDoesNotRecalculateWhenHeightPropertiesChange(
            Optional<ReadOnlyDoubleProperty> startHeightProperty,
            Optional<DoubleConsumer> startHeightPropertySetter,
            Optional<ReadOnlyDoubleProperty> endHeightProperty,
            Optional<DoubleConsumer> endHeightPropertySetter
    ) {
        height.set(500.0);

        startHeightProperty.ifPresent(property -> assertThat(node.getStartY(), is(not(property.get() * height.get()))));
        endHeightProperty.ifPresent(property -> assertThat(node.getEndY(), is(not(property.get() * height.get()))));

        startHeightPropertySetter.ifPresent(setter -> setter.accept(0.41));
        startHeightProperty.ifPresent(property -> assertThat(node.getStartY(), is(not(property.get() * height.get()))));
        endHeightPropertySetter.ifPresent(setter -> setter.accept(0.53));
        endHeightProperty.ifPresent(property -> assertThat(node.getEndY(), is(not(property.get() * height.get()))));
    }

}
