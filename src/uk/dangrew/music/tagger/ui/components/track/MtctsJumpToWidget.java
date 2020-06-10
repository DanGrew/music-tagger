package uk.dangrew.music.tagger.ui.components.track;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import uk.dangrew.music.tagger.model.MusicTrack;
import uk.dangrew.music.tagger.model.Tag;
import uk.dangrew.music.tagger.ui.positioning.*;

/**
 * Music Track Current Time Slider Jump To Widget.
 */
public class MtctsJumpToWidget extends Line{

    static final double WIDTH_START_PORTION = MtCurrentTimeSlider.WIDTH_START_PORTION;
    static final double TRACK_LENGTH_PORTION_OF_WIDTH = MtCurrentTimeSlider.TRACK_LENGTH_PORTION_OF_WIDTH;
    static final double HOOK_HEIGHT_START_PORTION = MtCurrentTimeSlider.HOOK_HEIGHT_START_PORTION;
    static final double HOOK_HEIGHT_END_PORTION = MtCurrentTimeSlider.HOOK_HEIGHT_END_PORTION;

    private final Tag tag;
    private final MusicTrack musicTrack;
    private final DoubleProperty widthPositionProperty;

    public MtctsJumpToWidget(Tag tag, MusicTrack musicTrack, CanvasDimensions canvasDimensions){
        this.tag = tag;
        this.musicTrack = musicTrack;
        this.widthPositionProperty = new SimpleDoubleProperty(0.0);

        this.setStroke(Color.MAGENTA);
        this.setStrokeWidth(2);

        this.updateWidthPosition();

        new CanvasLineRelativePositioning(canvasDimensions).bind(this, new LinePortions(
                new RelativePositioning(widthPositionProperty),
                new RelativePositioning(widthPositionProperty),
                new AbsolutePositioning(HOOK_HEIGHT_START_PORTION),
                new AbsolutePositioning(HOOK_HEIGHT_END_PORTION)
        ));

        this.setOnMouseReleased(event -> tagLineReleased(tag));

        tag.getMusicTimestamp().secondsProperty().addListener((s, o, n) -> updateWidthPosition());
    }

    private void updateWidthPosition(){
        double tagTimeInSeconds = tag.getMusicTimestamp().secondsProperty().get();
        double trackLengthInSeconds = musicTrack.mediaPlayer().durationProperty().get().toSeconds();
        double portionOfSong = tagTimeInSeconds / trackLengthInSeconds;
        double widthPosition = TRACK_LENGTH_PORTION_OF_WIDTH * portionOfSong + WIDTH_START_PORTION;
        widthPositionProperty.set(widthPosition);
    }

    void tagLineReleased(Tag tag) {
        musicTrack.mediaPlayer().seek(Duration.seconds(tag.getMusicTimestamp().secondsProperty().get()));
    }

    DoubleProperty widthPositionProperty() {
        return widthPositionProperty;
    }
}
