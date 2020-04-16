package uk.dangrew.music.tagger.ui;

import javafx.scene.layout.Pane;
import uk.dangrew.music.tagger.recorder.MusicController;
import uk.dangrew.music.tagger.recorder.MusicTrackConfiguration;

/**
 * {@link MusicTrackEditor} provides the top level ui component for the system.
 */
public class MusicTrackEditor extends Pane {

    public MusicTrackEditor(MusicController musicController, MusicTrackConfiguration configuration) {
        CanvasDimensions canvasDimensions = new CanvasDimensions(widthProperty(), heightProperty());

        this.getChildren().add(new MusicTrackUi(canvasDimensions, musicController, configuration));
    }
}
