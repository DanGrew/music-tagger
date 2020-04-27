package uk.dangrew.music.tagger.ui.components.controls;

import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import uk.dangrew.kode.javafx.custom.BoundDoubleAsTextProperty;
import uk.dangrew.kode.javafx.custom.PropertiesPane;
import uk.dangrew.kode.javafx.style.PropertyRowBuilder;
import uk.dangrew.music.tagger.main.MusicTrackState;
import uk.dangrew.music.tagger.ui.positioning.AbsolutePositioning;
import uk.dangrew.music.tagger.ui.positioning.CanvasDimensions;
import uk.dangrew.music.tagger.ui.positioning.CanvasNodeRelativePositioning;

public class MusicTrackConfigurationUi extends BorderPane {

    static final double WIDHT_PORTION = 0.15;
    static final double HEIGHT_PORTION = 0.5;

    private final TextField scaleTimeInterval;

    public MusicTrackConfigurationUi(MusicTrackState musicTrackState, CanvasDimensions canvasDimensions) {
        BoundDoubleAsTextProperty scaleTimeIntervalBinding = new BoundDoubleAsTextProperty(
                musicTrackState.scaleTimeIntervalProperty().asObject(),
                true
        );
        this.scaleTimeInterval = scaleTimeIntervalBinding.region();

        this.setCenter(new PropertiesPane("Music Track Configuration",
                new PropertyRowBuilder()
                        .withLabelName("Scale Internval Time")
                        .withBinding(scaleTimeIntervalBinding)
        ));

        new CanvasNodeRelativePositioning(canvasDimensions).bind(
                this,
                new AbsolutePositioning(WIDHT_PORTION),
                new AbsolutePositioning(HEIGHT_PORTION)
        );

        musicTrackState.recordingProperty().addListener((s, o, n) -> setDisable(n));
    }

    TextField getScaleTimeInterval(){
        return scaleTimeInterval;
    }
}
