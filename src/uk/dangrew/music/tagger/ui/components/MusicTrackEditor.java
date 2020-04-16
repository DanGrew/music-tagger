package uk.dangrew.music.tagger.ui.components;

import javafx.scene.layout.Pane;
import uk.dangrew.music.tagger.main.MusicController;
import uk.dangrew.music.tagger.main.MusicTrackConfiguration;
import uk.dangrew.music.tagger.ui.positioning.CanvasDimensions;

/**
 * {@link MusicTrackEditor} provides the top level ui component for the system.
 */
public class MusicTrackEditor extends Pane {

    public MusicTrackEditor(MusicController musicController, MusicTrackConfiguration configuration) {
        CanvasDimensions canvasDimensions = new CanvasDimensions(widthProperty(), heightProperty());

        this.getChildren().add(new MusicTrackUi(canvasDimensions, musicController, configuration));
    }
}
