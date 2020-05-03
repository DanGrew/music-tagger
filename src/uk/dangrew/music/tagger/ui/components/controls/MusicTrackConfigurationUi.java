package uk.dangrew.music.tagger.ui.components.controls;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import uk.dangrew.kode.javafx.custom.BoundDoubleAsTextProperty;
import uk.dangrew.kode.javafx.custom.BoundTextProperty;
import uk.dangrew.kode.javafx.custom.PropertiesPane;
import uk.dangrew.kode.javafx.style.PropertyRowBuilder;
import uk.dangrew.music.tagger.main.MusicTrackState;
import uk.dangrew.music.tagger.model.MusicTrack;
import uk.dangrew.music.tagger.ui.positioning.AbsolutePositioning;
import uk.dangrew.music.tagger.ui.positioning.CanvasDimensions;
import uk.dangrew.music.tagger.ui.positioning.CanvasRegionRelativePositioning;

public class MusicTrackConfigurationUi extends GridPane {

    static final double WIDHT_PORTION = 0.15;
    static final double HEIGHT_PORTION = 0.5;

    private final TextField scaleTimeIntervalField;
    private final TextField nameField;

    public MusicTrackConfigurationUi(MusicTrack musicTrack, MusicTrackState musicTrackState, CanvasDimensions canvasDimensions) {
        BoundDoubleAsTextProperty scaleTimeIntervalBinding = new BoundDoubleAsTextProperty(
                musicTrackState.scaleTimeIntervalProperty().asObject(),
                true
        );
        this.scaleTimeIntervalField = scaleTimeIntervalBinding.region();

        BoundTextProperty nameBinding = new BoundTextProperty(convertStringToObject(musicTrack.nameProperty()), true);
        this.nameField = nameBinding.region();

        this.add(new PropertiesPane("Music Track Configuration",
                new PropertyRowBuilder()
                        .withLabelName("Music Track Name")
                        .withBinding(nameBinding)
        ), 0, 0);
        this.add(new PropertiesPane("System Settings",
                new PropertyRowBuilder()
                        .withLabelName("Scale Internval Time")
                        .withBinding(scaleTimeIntervalBinding)
        ), 0, 1);

        new CanvasRegionRelativePositioning(canvasDimensions).bind(
                this,
                new AbsolutePositioning(WIDHT_PORTION),
                new AbsolutePositioning(HEIGHT_PORTION)
        );

        musicTrackState.recordingProperty().addListener((s, o, n) -> setDisable(n));
    }

    private ObjectProperty<String> convertStringToObject(StringProperty stringProperty) {
        ObjectProperty<String> property = new SimpleObjectProperty<>();
        stringProperty.bindBidirectional(property);
        return property;
    }

    TextField getScaleTimeIntervalField() {
        return scaleTimeIntervalField;
    }

    TextField getNameField() {
        return nameField;
    }
}
