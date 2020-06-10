package uk.dangrew.music.tagger.ui.components.track;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import uk.dangrew.kode.friendly.javafx.FriendlyMouseEvent;
import uk.dangrew.music.tagger.main.MusicTrackState;
import uk.dangrew.music.tagger.model.MusicTimestamp;
import uk.dangrew.music.tagger.model.MusicTrack;
import uk.dangrew.music.tagger.model.Tag;
import uk.dangrew.music.tagger.ui.positioning.*;

import java.util.HashMap;
import java.util.Map;

public class MtCurrentTimeSlider extends Pane {

    public static final double WIDTH_START_PORTION = 0.1;
    public static final double WIDTH_END_PORTION = 0.9;
    public static final double TRACK_LENGTH_PORTION_OF_WIDTH = WIDTH_END_PORTION - WIDTH_START_PORTION;
    public static final double HOOK_HEIGHT_START_PORTION = 0.93;
    public static final double HOOK_HEIGHT_END_PORTION = 0.97;
    public static final double SKELETON_HEIGHT_PORTION = 0.95;
    public static final double LABEL_WIDTH_START_PORTION = 0.05;
    public static final double LABEL_WIDTH_END_PORTION = 0.95;

    private final CanvasDimensions canvasDimensions;
    private final CanvasLineRelativePositioning canvasLineRelativePositioning;

    private final MusicTrack musicTrack;
    private final MusicTrackState musicTrackState;
    private final DoubleProperty currentTimeWidthProperty;

    private final Line skeleton;
    private final Line currentTimeLine;
    private final Line startHook;
    private final Line endHook;
    private final Label startLabel;
    private final Label endLabel;

    private final Map<Tag, MtctsJumpToWidget> tagLines;

    public MtCurrentTimeSlider(CanvasDimensions canvasDimensions, MusicTrack musicTrack, MusicTrackState musicTrackState) {
        this.canvasDimensions = canvasDimensions;
        this.musicTrack = musicTrack;
        this.musicTrackState = musicTrackState;
        this.tagLines = new HashMap<>();
        this.setPickOnBounds(false);

        this.skeleton = new Line();
        this.skeleton.setStrokeWidth(2);
        this.getChildren().add(skeleton);

        this.canvasLineRelativePositioning = new CanvasLineRelativePositioning(canvasDimensions);
        this.canvasLineRelativePositioning.bind(skeleton, new LinePortions(
                new AbsolutePositioning(WIDTH_START_PORTION),
                new AbsolutePositioning(WIDTH_END_PORTION),
                new AbsolutePositioning(SKELETON_HEIGHT_PORTION),
                new AbsolutePositioning(SKELETON_HEIGHT_PORTION)
        ));

        this.currentTimeWidthProperty = new SimpleDoubleProperty();

        this.currentTimeLine = new Line();
        this.currentTimeLine.setStroke(Color.GREEN);
        this.currentTimeLine.setStrokeWidth(5.0);
        this.getChildren().add(currentTimeLine);
        this.canvasLineRelativePositioning.bind(currentTimeLine, new LinePortions(
                new RelativePositioning(currentTimeWidthProperty),
                new RelativePositioning(currentTimeWidthProperty),
                new AbsolutePositioning(HOOK_HEIGHT_START_PORTION),
                new AbsolutePositioning(HOOK_HEIGHT_END_PORTION)
        ));

        this.startLabel = new Label(MusicTimestamp.format(0));
        this.getChildren().add(startLabel);
        CanvasRegionRelativePositioning canvasRegionRelativePositioning = new CanvasRegionRelativePositioning(canvasDimensions);
        canvasRegionRelativePositioning.bind(startLabel, new NodePortions(
                new AbsolutePositioning(LABEL_WIDTH_START_PORTION),
                new AbsolutePositioning(SKELETON_HEIGHT_PORTION)
        ));

        this.startHook = new Line();
        this.getChildren().add(startHook);
        this.canvasLineRelativePositioning.bind(startHook, new LinePortions(
                new AbsolutePositioning(WIDTH_START_PORTION),
                new AbsolutePositioning(WIDTH_START_PORTION),
                new AbsolutePositioning(HOOK_HEIGHT_START_PORTION),
                new AbsolutePositioning(HOOK_HEIGHT_END_PORTION)
        ));

        this.endLabel = new Label(MusicTimestamp.format(0));
        this.updateDurationLabel();
        this.getChildren().add(endLabel);
        canvasRegionRelativePositioning.bind(endLabel, new NodePortions(
                new AbsolutePositioning(LABEL_WIDTH_END_PORTION),
                new AbsolutePositioning(SKELETON_HEIGHT_PORTION)
        ));

        this.endHook = new Line();
        this.getChildren().add(endHook);
        this.canvasLineRelativePositioning.bind(endHook, new LinePortions(
                new AbsolutePositioning(WIDTH_END_PORTION),
                new AbsolutePositioning(WIDTH_END_PORTION),
                new AbsolutePositioning(HOOK_HEIGHT_START_PORTION),
                new AbsolutePositioning(HOOK_HEIGHT_END_PORTION)
        ));

        this.musicTrackState.currentTimeProperty().addListener((s, o, n) -> updatePosition());

        this.musicTrack.mediaPlayer().durationProperty().addListener((s, o, n) -> {
            updatePosition();
            updateDurationLabel();
        });

        this.currentTimeLine.setOnMouseDragged(event -> currentLineDragged(new FriendlyMouseEvent(event)));

        populateTags();
        ListChangeListener<Tag> listChangeListener = change -> {
            refreshTags();
            populateTags();
        };
        this.musicTrack.getTags().addListener(listChangeListener);
    }

    private void populateTags() {
        musicTrack.getTags().forEach(this::createTagJumpTo);
    }

    private void refreshTags() {
        Map<Tag, Line> copy = new HashMap<>(tagLines);
        for (Map.Entry<Tag, Line> tagLineEntry : copy.entrySet()) {
            if (!musicTrack.getTags().contains(tagLineEntry.getKey())) {
                tagLines.remove(tagLineEntry.getKey());
                getChildren().remove(tagLineEntry.getValue());
            }
        }
    }

    private void createTagJumpTo(Tag tag) {
        MtctsJumpToWidget line = tagLines.get(tag);
        if (line != null) {
            return;
        }

        line = new MtctsJumpToWidget(tag, musicTrack, canvasDimensions);
        getChildren().add(line);
        tagLines.put(tag, line);
    }

    void currentLineDragged(FriendlyMouseEvent event) {
        double startScreenWidth = canvasDimensions.width() * WIDTH_START_PORTION;
        double endScreenWidth = canvasDimensions.width() * WIDTH_END_PORTION;
        double trackScreenLength = endScreenWidth - startScreenWidth;
        double offsetMousePosition = event.friendly_getX() - WIDTH_START_PORTION * canvasDimensions.width();
        double songProgress = offsetMousePosition / trackScreenLength;
        double duration = musicTrack.mediaPlayer().durationProperty().get().toSeconds();

        musicTrack.mediaPlayer().seek(Duration.seconds(duration * songProgress));
    }

    private void updatePosition() {
        double durationInSeconds = musicTrack.mediaPlayer().durationProperty().get().toSeconds();
        double totalTrackPortionLength = TRACK_LENGTH_PORTION_OF_WIDTH;
        double widthPortionPerSecond = totalTrackPortionLength / durationInSeconds;
        currentTimeWidthProperty.set(widthPortionPerSecond * musicTrackState.currentTimeProperty().get() + WIDTH_START_PORTION);
    }

    private void updateDurationLabel() {
        endLabel.setText(MusicTimestamp.format(musicTrack.mediaPlayer().durationProperty().get()));
    }

    DoubleProperty currentTimeWidthProperty() {
        return currentTimeWidthProperty;
    }

    Line skeleton() {
        return skeleton;
    }

    Line startHook() {
        return startHook;
    }

    Line endHook() {
        return endHook;
    }

    Line currentTimeLine() {
        return currentTimeLine;
    }

    Label startLabel() {
        return startLabel;
    }

    Label endLabel() {
        return endLabel;
    }

    Line lineForTag(Tag tag) {
        return tagLines.get(tag);
    }
}
