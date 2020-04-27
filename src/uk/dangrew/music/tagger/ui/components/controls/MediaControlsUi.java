package uk.dangrew.music.tagger.ui.components.controls;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import uk.dangrew.kode.javafx.style.JavaFxStyle;
import uk.dangrew.music.tagger.main.*;
import uk.dangrew.music.tagger.model.MusicTimestamp;
import uk.dangrew.music.tagger.model.ReadOnlyMedia;
import uk.dangrew.music.tagger.ui.components.MusicController;
import uk.dangrew.music.tagger.ui.positioning.AbsolutePositioning;
import uk.dangrew.music.tagger.ui.positioning.CanvasDimensions;
import uk.dangrew.music.tagger.ui.positioning.CanvasNodeRelativePositioning;

/**
 * Part of the Ui providing the controls for the associated {@link javafx.scene.media.Media}.
 */
public class MediaControlsUi extends GridPane {

    static final String PLAY_TEXT = "Play";
    static final String PAUSE_TEXT = "Pause";

    static final String RECORD_TEXT = "Record";
    static final String STOP_RECORDING_TEXT = "Stop Recording";

    static final double WIDTH_PORTION = 0.5;
    static final double HEIGHT_PORTION = 0.05;

    private final ReadOnlyMedia musicTrack;

    private final Button play;
    private final Button stop;
    private final Button plus30;
    private final Button minus30;
    private final Button speedUp;
    private final Button slowDown;

    private final Button recordButton;

    private final Label currentTimeLabel;

    public MediaControlsUi(CanvasDimensions canvasDimensions, MusicController musicController, MusicTrackState musicTrackState){
        this.musicTrack = musicController.getMedia();

        CanvasNodeRelativePositioning canvasNodeRelativePositioning = new CanvasNodeRelativePositioning(canvasDimensions);
        canvasNodeRelativePositioning.bind(this, new AbsolutePositioning(WIDTH_PORTION), new AbsolutePositioning(HEIGHT_PORTION));

        this.play = new Button(PLAY_TEXT);
        this.play.setOnAction(event -> musicController.togglePause());
        this.play.setFocusTraversable(false);
        this.stop = new Button("Stop");
        this.stop.setOnAction(event -> musicController.stop());
        this.stop.setFocusTraversable(false);
        this.plus30 = new Button("+30s");
        this.plus30.setOnAction(event -> musicController.plus30());
        this.plus30.setFocusTraversable(false);
        this.minus30 = new Button("-30s");
        this.minus30.setOnAction(event -> musicController.minus30());
        this.minus30.setFocusTraversable(false);
        this.speedUp = new Button(">>");
        this.speedUp.setOnAction(event -> musicController.speedUp());
        this.speedUp.setFocusTraversable(false);
        this.slowDown = new Button("<<");
        this.slowDown.setOnAction(event -> musicController.slowDown());
        this.slowDown.setFocusTraversable(false);

        int column = 0;
        this.add(slowDown, column++, 0);
        this.add(minus30, column++, 0);
        this.add(play, column++, 0);
        this.add(stop, column++, 0);
        this.add(plus30, column++, 0);
        this.add(speedUp, column++, 0);

        this.recordButton = new Button(RECORD_TEXT);
        this.recordButton.setFocusTraversable(false);
        this.recordButton.setTextAlignment(TextAlignment.CENTER);
        this.recordButton.setAlignment(Pos.CENTER);
        this.recordButton.setMaxWidth(Double.MAX_VALUE);
        this.recordButton.setOnAction(event -> musicController.toggleRecording());
        add(recordButton, 0, 1);
        GridPane.setColumnSpan(recordButton, column);
        GridPane.setHalignment(recordButton, HPos.CENTER);

        this.currentTimeLabel = new Label("Unknown");
        this.currentTimeLabel.setTextAlignment(TextAlignment.CENTER);
        this.currentTimeLabel.setAlignment(Pos.CENTER);
        this.currentTimeLabel.setBorder(new JavaFxStyle().borderFor(Color.BLACK, 1));
        this.currentTimeLabel.setMaxWidth(Double.MAX_VALUE);
        this.add(currentTimeLabel, 0, 2);
        GridPane.setColumnSpan(currentTimeLabel, column);
        GridPane.setHalignment(currentTimeLabel, HPos.CENTER);

        musicController.getMedia().playingProperty().addListener( (s, o, n) -> handlePlayingChange(n));
        musicController.getMedia().currentTimeProperty().addListener( (s, o, n) -> handleCurrentTimeChange());
        musicController.getMedia().rateProperty().addListener( (s, o, n) -> handleCurrentTimeChange());

        musicTrackState.recordingProperty().addListener((s, o, n) -> handleRecordingEnablement(n));
    }

    private void handlePlayingChange(boolean isPlaying){
        if ( isPlaying) {
            play.setText(PAUSE_TEXT);
        } else {
            play.setText(PLAY_TEXT);
        }
    }

    private void handleCurrentTimeChange(){
        currentTimeLabel.setText(
                MusicTimestamp.format(musicTrack.currentTimeProperty().get().toSeconds()) + " at " + musicTrack.rateProperty().get() + "x");
    }

    private void handleRecordingEnablement(boolean isRecording){
        play.setDisable(isRecording);
        stop.setDisable(isRecording);
        plus30.setDisable(isRecording);
        minus30.setDisable(isRecording);
        speedUp.setDisable(isRecording);
        slowDown.setDisable(isRecording);

        if ( isRecording) {
            recordButton.setText(STOP_RECORDING_TEXT);
        } else {
            recordButton.setText(RECORD_TEXT);
        }
    }

    Button play() {
        return play;
    }

    Button stop() {
        return stop;
    }

    Button minus30() {
        return minus30;
    }

    Button plus30() {
        return plus30;
    }

    Button slowDown() {
        return slowDown;
    }

    Button speedUp() {
        return speedUp;
    }

    Label currentTime() {
        return currentTimeLabel;
    }

    Button record(){
        return recordButton;
    }
}
