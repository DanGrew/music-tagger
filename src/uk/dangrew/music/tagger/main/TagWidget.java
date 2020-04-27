package uk.dangrew.music.tagger.main;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import uk.dangrew.music.tagger.ui.positioning.*;

import java.util.OptionalDouble;

public class TagWidget extends Pane {

    static final double WIDTH_START_PORTION = 0.46;
    static final double WIDTH_END_PORTION = 0.54;

    private final Tag tag;
    private final MusicTrackState musicTrackState;
    private final ScalePositionCalculator scalePositionCalculator;

    private final Line trackLine;
    private final TextField textField;

    private final DoubleProperty heightProperty;
    private final DoubleProperty textFieldWidthProperty;


    public TagWidget(Tag tag, MusicTrackState musicTrackState, CanvasDimensions canvasDimensions) {
        this(new ScalePositionCalculator(), tag, musicTrackState, canvasDimensions);
    }

    TagWidget(
            ScalePositionCalculator scalePositionCalculator,
            Tag tag,
            MusicTrackState musicTrackState,
            CanvasDimensions canvasDimensions
    ){
        this.tag = tag;
        this.musicTrackState = musicTrackState;
        this.scalePositionCalculator = scalePositionCalculator;

        this.trackLine = new Line();
        this.trackLine.setStrokeWidth(2);
        this.trackLine.setStroke(Color.MAGENTA);
        this.getChildren().add(trackLine);

        this.heightProperty = new SimpleDoubleProperty(0.0);
        RelativePositioning heightPositioning = new RelativePositioning(heightProperty);

        CanvasLineRelativePositioning canvasLineRelativePositioning = new CanvasLineRelativePositioning(canvasDimensions);
        canvasLineRelativePositioning.bind(trackLine, new LinePortions(
                new AbsolutePositioning(WIDTH_START_PORTION),
                new AbsolutePositioning(WIDTH_END_PORTION),
                heightPositioning,
                heightPositioning
        ));

        this.textField = new TextField();
        this.textField.setDisable(true);
        this.textField.textProperty().bindBidirectional(tag.getTextProperty());
        this.getChildren().add(textField);

        this.textFieldWidthProperty = new SimpleDoubleProperty(0.7);
        CanvasNodeRelativePositioning canvasNodeRelativePositioning = new CanvasNodeRelativePositioning(canvasDimensions);
        canvasNodeRelativePositioning.bind(textField, new RelativePositioning(textFieldWidthProperty), heightPositioning);

        musicTrackState.recordingProperty().addListener((s, o, n) -> textField.setDisable(n));
    }

    public void updatePosition(double widthPosition){
        textFieldWidthProperty.set(widthPosition);

        OptionalDouble result = scalePositionCalculator.calculatePositionFor(tag.getMusicTimestamp().seconds(), musicTrackState);
        if ( result.isPresent()){
            heightProperty.set(result.getAsDouble());
            if ( !getChildren().contains(trackLine)){
                getChildren().add(trackLine);
                getChildren().add(textField);
            }
        } else {
            getChildren().remove(trackLine);
            getChildren().remove(textField);
        }
    }

    Tag getTag(){
        return tag;
    }

    DoubleProperty heightPortionProperty() {
        return heightProperty;
    }

    DoubleProperty getTextFieldWidthProperty(){
        return textFieldWidthProperty;
    }

    Line getTrackLine(){
        return trackLine;
    }

    TextField getTextField(){
        return textField;
    }
}
