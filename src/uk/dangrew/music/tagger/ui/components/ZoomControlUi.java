package uk.dangrew.music.tagger.ui.components;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import uk.dangrew.music.tagger.main.MusicTrackState;
import uk.dangrew.music.tagger.ui.positioning.AbsolutePositioning;
import uk.dangrew.music.tagger.ui.positioning.CanvasDimensions;
import uk.dangrew.music.tagger.ui.positioning.CanvasNodeRelativePositioning;

public class ZoomControlUi extends GridPane {

    static final double HEIGHT_PORTION = 0.8;
    static final double ZOOM_IN_FACTOR = 1.25;
    static final double ZOOM_OUT_FACTOR = 0.8;

    private final MusicTrackState musicTrackState;
    private final Button zoomIn;
    private final Button zoomOut;

    public ZoomControlUi(MusicTrackState musicTrackState, CanvasDimensions canvasDimensions){
        this.musicTrackState = musicTrackState;

        this.zoomIn = new Button("Zoom In");
        this.zoomIn.setMaxWidth(Double.MAX_VALUE);
        this.zoomIn.setFocusTraversable(false);
        this.add(zoomIn, 0, 0);
        this.zoomIn.setOnAction( event -> handleScalePositionIntervalChange(ZOOM_IN_FACTOR));

        this.zoomOut = new Button("Zoom Out");
        this.zoomOut.setMaxWidth(Double.MAX_VALUE);
        this.zoomOut.setFocusTraversable(false);
        this.add(zoomOut, 0, 1);
        this.zoomOut.setOnAction( event -> handleScalePositionIntervalChange(ZOOM_OUT_FACTOR));

        new CanvasNodeRelativePositioning(canvasDimensions).bind(
                this,
                new AbsolutePositioning(MtCurrentPosition.WIDTH_END_PORTION),
                new AbsolutePositioning(HEIGHT_PORTION)
        );
        this.musicTrackState.recordingProperty().addListener((s, o, n) -> handleRecordingEnablement(n));
    }

    private void handleRecordingEnablement(boolean isRecording){
        zoomIn.setDisable(isRecording);
        zoomOut.setDisable(isRecording);
    }

    private void handleScalePositionIntervalChange(double scaleFactor){
        musicTrackState.scalePositionIntervalProperty().set(musicTrackState.scalePositionIntervalProperty().get() * scaleFactor);
    }

    Button zoomIn(){
        return zoomIn;
    }

    Button zoomOut(){
        return zoomOut;
    }
}
