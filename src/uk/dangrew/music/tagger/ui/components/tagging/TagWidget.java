package uk.dangrew.music.tagger.ui.components.tagging;

import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import uk.dangrew.kode.friendly.javafx.FriendlyMouseEvent;
import uk.dangrew.kode.javafx.glyph.GlyphCircle;
import uk.dangrew.kode.javafx.glyph.GlyphCircleBuilder;
import uk.dangrew.music.tagger.main.MusicTrackState;
import uk.dangrew.music.tagger.model.MusicTrack;
import uk.dangrew.music.tagger.model.Tag;
import uk.dangrew.music.tagger.ui.components.track.MtCurrentPosition;
import uk.dangrew.music.tagger.ui.positioning.*;

import java.util.OptionalDouble;

public class TagWidget extends Pane {

    static final double WIDTH_START_PORTION = 0.46;
    static final double WIDTH_END_PORTION = 0.54;
    public static final int GLYPH_RADIUS_VALUE = 8;

    private final CanvasDimensions canvasDimensions;
    private final MusicTrack musicTrack;
    private final Tag tag;
    private final MusicTrackState musicTrackState;
    private final ScalePositionCalculator scalePositionCalculator;

    private final Pane textFieldAndControls;
    private final GlyphCircle moveButton;
    private final GlyphCircle deleteButton;

    private final Line trackLine;
    private final TextField textField;

    private final DoubleProperty heightProperty;
    private final DoubleProperty textFieldWidthProperty;

    public TagWidget(
            Tag tag,
            MusicTrack musicTrack,
            MusicTrackState musicTrackState,
            CanvasDimensions canvasDimensions
    ) {
        this(new ScalePositionCalculator(), tag, musicTrack, musicTrackState, canvasDimensions);
    }

    TagWidget(
            ScalePositionCalculator scalePositionCalculator,
            Tag tag,
            MusicTrack musicTrack,
            MusicTrackState musicTrackState,
            CanvasDimensions canvasDimensions
    ) {
        this.tag = tag;
        this.musicTrack = musicTrack;
        this.musicTrackState = musicTrackState;
        this.scalePositionCalculator = scalePositionCalculator;
        this.canvasDimensions = canvasDimensions;
        this.heightProperty = new SimpleDoubleProperty(0.0);

        this.setPickOnBounds(false);

        RelativePositioning heightPositioning = new RelativePositioning(heightProperty);
        CanvasLineRelativePositioning canvasLineRelativePositioning = new CanvasLineRelativePositioning(canvasDimensions);
        CanvasRegionRelativePositioning canvasRegionRelativePositioning = new CanvasRegionRelativePositioning(canvasDimensions);

        this.trackLine = new Line();
        this.trackLine.setStrokeWidth(2);
        this.trackLine.setStroke(Color.MAGENTA);
        this.getChildren().add(trackLine);

        canvasLineRelativePositioning.bind(trackLine, new LinePortions(
                new AbsolutePositioning(WIDTH_START_PORTION),
                new AbsolutePositioning(WIDTH_END_PORTION),
                heightPositioning,
                heightPositioning
        ));

        this.textField = new TextField();
        this.textField.setDisable(true);
        this.textField.textProperty().bindBidirectional(tag.getTextProperty());

        this.textFieldWidthProperty = new SimpleDoubleProperty(0.7);

        this.textFieldAndControls = new Pane();
        this.textFieldAndControls.setPickOnBounds(false);
        this.textFieldAndControls.getChildren().add(textField);
        canvasRegionRelativePositioning.bind(textFieldAndControls, new RelativePositioning(textFieldWidthProperty), heightPositioning);

        this.moveButton = new GlyphCircleBuilder(new MaterialIconView(MaterialIcon.OPEN_WITH))
                .withRadius(GLYPH_RADIUS_VALUE)
                .boundHorizontally(textField.widthProperty().subtract(GLYPH_RADIUS_VALUE))
                .fixedVertically(-GLYPH_RADIUS_VALUE)
                .withDrag(event -> mouseDragged(new FriendlyMouseEvent(event)))
                .build();

        this.deleteButton = new GlyphCircleBuilder(new MaterialIconView(MaterialIcon.CLOSE))
                .withRadius(GLYPH_RADIUS_VALUE)
                .withGlyphColour(Color.RED)
                .boundHorizontally(textField.widthProperty().subtract(GLYPH_RADIUS_VALUE))
                .boundVertically(textField.heightProperty().subtract(GLYPH_RADIUS_VALUE))
                .withAction( event -> musicTrack.removeTag(tag))
                .build();

        this.textFieldAndControls.getChildren().add(moveButton);
        this.textFieldAndControls.getChildren().add(deleteButton);
        this.getChildren().add(textFieldAndControls);

        tag.getMusicTimestamp().secondsProperty().addListener((s, o, n) -> updatePosition());
        musicTrackState.currentPositionProperty().addListener((s, o, n) -> updatePosition());
        musicTrackState.scalePositionIntervalProperty().addListener((s, o, n) -> updatePosition());
        musicTrackState.scaleTimeIntervalProperty().addListener((s, o, n) -> updatePosition());
        musicTrackState.recordingProperty().addListener((s, o, n) -> textField.setDisable(n));

        updateEnablement();
        musicTrackState.recordingProperty().addListener((s, o, n) -> updateEnablement());
        musicTrack.mediaPlayer().playingProperty().addListener((s, o, n) -> updateEnablement());

    }

    private void updateEnablement() {
        boolean buttonsDisabled = musicTrackState.recordingProperty().get() || musicTrack.mediaPlayer().playingProperty().get();
        moveButton.friendly_setDisable(buttonsDisabled);
        deleteButton.friendly_setDisable(buttonsDisabled);
    }

    void mouseDragged(FriendlyMouseEvent event) {
        double height = canvasDimensions.height();
        double mousePositionAsPortionOfHeight = event.friendly_getSceneY() / height;
        double currentPosition = heightPortionProperty().get();
        double buttonHeightAsPortion = (textField.getHeight() - GLYPH_RADIUS_VALUE) / height;
        double change = mousePositionAsPortionOfHeight - currentPosition - buttonHeightAsPortion;
        if ( mousePositionAsPortionOfHeight - buttonHeightAsPortion > MtCurrentPosition.MAXIMUM_POSITION){
            return;
        }

        if ( mousePositionAsPortionOfHeight - buttonHeightAsPortion < MtCurrentPosition.MINIMUM_POSITION){
            return;
        }

        double secondsScaleFactor = musicTrackState.scalePositionIntervalProperty().get() / musicTrackState.scaleTimeIntervalProperty().get();
        tag.getMusicTimestamp().secondsProperty().set(tag.getMusicTimestamp().secondsProperty().get() + change / secondsScaleFactor);
    }

    private void updatePosition(){
        updatePosition(textFieldWidthProperty.get());
    }

    public void updatePosition(double widthPosition) {
        textFieldWidthProperty.set(widthPosition);

        OptionalDouble result = scalePositionCalculator.calculatePositionFor(tag.getMusicTimestamp().secondsProperty().get(), musicTrackState);
        if (result.isPresent()) {
            heightProperty.set(result.getAsDouble());
            if (!getChildren().contains(trackLine)) {
                getChildren().add(trackLine);
                getChildren().add(textFieldAndControls);
            }
        } else {
            getChildren().remove(trackLine);
            getChildren().remove(textFieldAndControls);
        }
    }

    Tag getTag() {
        return tag;
    }

    DoubleProperty heightPortionProperty() {
        return heightProperty;
    }

    DoubleProperty getTextFieldWidthProperty() {
        return textFieldWidthProperty;
    }

    Line getTrackLine() {
        return trackLine;
    }

    TextField getTextField() {
        return textField;
    }

    Pane textFieldAndControlsWrapper() {
        return textFieldAndControls;
    }

    GlyphCircle moveButton() {
        return moveButton;
    }

    GlyphCircle deleteButton() {
        return deleteButton;
    }
}
