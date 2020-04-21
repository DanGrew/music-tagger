package uk.dangrew.music.tagger.ui.components;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import uk.dangrew.kode.javafx.style.JavaFxStyle;
import uk.dangrew.music.tagger.main.MusicController;
import uk.dangrew.music.tagger.main.MusicTimestamp;
import uk.dangrew.music.tagger.ui.positioning.AbsolutePositioning;
import uk.dangrew.music.tagger.ui.positioning.CanvasDimensions;
import uk.dangrew.music.tagger.ui.positioning.CanvasNodeRelativePositioning;

/**
 * Part of the Ui providing the controls for the associated {@link javafx.scene.media.Media}.
 */
public class MediaControlsUi extends GridPane {

    static final String PLAY_TEXT = "Play";
    static final String PAUSE_TEXT = "Pause";

    static final double WIDTH_PORTION = 0.5;
    static final double HEIGHT_PORTION = 0.07;

    private final Button play;
    private final Button stop;
    private final Button plus30;
    private final Button minus30;
    private final Button speedUp;
    private final Button slowDown;

    private final Label currentTimeLabel;

    public MediaControlsUi(CanvasDimensions canvasDimensions, MusicController musicController){
        CanvasNodeRelativePositioning canvasNodeRelativePositioning = new CanvasNodeRelativePositioning(canvasDimensions);
        canvasNodeRelativePositioning.bind(this, new AbsolutePositioning(WIDTH_PORTION), new AbsolutePositioning(HEIGHT_PORTION));

        this.play = new Button("Play");
        this.play.setOnAction(event -> musicController.togglePause());
        this.stop = new Button("Stop");
        this.stop.setOnAction(event -> musicController.stop());
        this.plus30 = new Button("+30s");
        this.plus30.setOnAction(event -> musicController.plus30());
        this.minus30 = new Button("-30s");
        this.minus30.setOnAction(event -> musicController.minus30());
        this.speedUp = new Button(">>");
        this.speedUp.setOnAction(event -> musicController.speedUp());
        this.slowDown = new Button("<<");
        this.slowDown.setOnAction(event -> musicController.slowDown());

        int column = 0;
        add(slowDown, column++, 0);
        add(minus30, column++, 0);
        add(play, column++, 0);
        add(stop, column++, 0);
        add(plus30, column++, 0);
        add(speedUp, column++, 0);

        currentTimeLabel = new Label("0:00");
        currentTimeLabel.setTextAlignment(TextAlignment.CENTER);
        currentTimeLabel.setAlignment(Pos.CENTER);
        currentTimeLabel.setBorder(new JavaFxStyle().borderFor(Color.BLACK, 1));
        currentTimeLabel.setMaxWidth(Double.MAX_VALUE);
        add(currentTimeLabel, 0, 1);
        GridPane.setColumnSpan(currentTimeLabel, column);
        GridPane.setHalignment(currentTimeLabel, HPos.CENTER);

        musicController.getMedia().playingProperty().addListener( (s, o, n) -> handlePlayingChange(n));
        musicController.getMedia().currentTimeProperty().addListener( (s, o, n) -> handleCurrentTimeChange(n));
    }

    private void handlePlayingChange(boolean isPlaying){
        if ( isPlaying) {
            play.setText(PAUSE_TEXT);
        } else {
            play.setText(PLAY_TEXT);
        }
    }

    private void handleCurrentTimeChange(Duration currentTime){
        currentTimeLabel.setText(MusicTimestamp.format(currentTime.toSeconds()));
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
}
