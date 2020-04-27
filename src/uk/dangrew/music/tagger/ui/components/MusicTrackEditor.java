package uk.dangrew.music.tagger.ui.components;

import javafx.scene.layout.Pane;
import uk.dangrew.music.tagger.main.*;
import uk.dangrew.music.tagger.model.MusicTrack;
import uk.dangrew.music.tagger.ui.components.controls.MusicTrackConfigurationUi;
import uk.dangrew.music.tagger.ui.components.controls.ZoomControlUi;
import uk.dangrew.music.tagger.ui.components.tagging.TagCapture;
import uk.dangrew.music.tagger.ui.components.tagging.TagPaneUi;
import uk.dangrew.music.tagger.ui.components.track.MusicTrackUi;
import uk.dangrew.music.tagger.ui.positioning.CanvasDimensions;

/**
 * {@link MusicTrackEditor} provides the top level ui component for the system.
 */
public class MusicTrackEditor extends Pane {

    public MusicTrackEditor(MusicTrack musicTrack, MusicController musicController, MusicTrackState musicTrackState) {
        CanvasDimensions canvasDimensions = new CanvasDimensions(widthProperty(), heightProperty());

        new TagCapture(musicTrack, musicTrackState);
        this.getChildren().add(new TagPaneUi(musicTrack, canvasDimensions, musicTrackState));
        this.getChildren().add(new MusicTrackUi(canvasDimensions, musicController, musicTrackState));
        this.getChildren().add(new MusicTrackConfigurationUi(musicTrackState, canvasDimensions));
        this.getChildren().add(new ZoomControlUi(musicTrackState, canvasDimensions));
    }
}
