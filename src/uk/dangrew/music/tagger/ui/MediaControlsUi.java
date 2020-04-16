package uk.dangrew.music.tagger.ui;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import uk.dangrew.music.tagger.recorder.MusicController;

/**
 * Part of the Ui providing the controls for the associated {@link javafx.scene.media.Media}.
 */
public class MediaControlsUi extends GridPane {

    static final double WIDTH_PORTION = 0.5;
    static final double HEIGHT_PORTION = 0.07;

    private final Button play;
    private final Button pause;
    private final Button stop;
    private final Button plus30;
    private final Button minus30;
    private final Button speedUp;
    private final Button slowDown;

    public MediaControlsUi(CanvasDimensions canvasDimensions, MusicController musicController){
        CanvasNodeRelativePositioning canvasNodeRelativePositioning = new CanvasNodeRelativePositioning(canvasDimensions);

        this.play = new Button("Play");
        this.play.setOnAction(event -> musicController.play());
        this.pause = new Button("Pause");
        this.pause.setOnAction(event -> musicController.pause());
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
        add(pause, column++, 0);
        add(stop, column++, 0);
        add(plus30, column++, 0);
        add(speedUp, column++, 0);

        canvasNodeRelativePositioning.bind(this, new AbsolutePositioning(WIDTH_PORTION), new AbsolutePositioning(HEIGHT_PORTION));
    }

    Button play() {
        return play;
    }

    Button pause() {
        return pause;
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

}
