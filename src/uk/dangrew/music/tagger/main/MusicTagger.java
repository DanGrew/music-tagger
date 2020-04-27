/*
 * ----------------------------------------
 *      Nutrient Usage Tracking System
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2017
 * ----------------------------------------
 */
package uk.dangrew.music.tagger.main;

import com.sun.javafx.application.PlatformImpl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import uk.dangrew.music.tagger.model.MusicTrack;
import uk.dangrew.music.tagger.system.KeyBoardCapture;
import uk.dangrew.music.tagger.ui.components.MtMenuBar;
import uk.dangrew.music.tagger.ui.components.MusicController;
import uk.dangrew.music.tagger.ui.components.MusicTrackEditor;

import java.io.File;

/**
 * Entry point to the system for launching.
 */
public class MusicTagger extends Application {

    static final String TITLE = "Music Tagger";

    private static KeyBoardCapture KEY_BOARD_CAPTURE;

    public MusicTagger() {
    }//End Constructor

    public static KeyBoardCapture keyBoard() {
        return KEY_BOARD_CAPTURE;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle(TITLE);
        stage.setOnCloseRequest(event -> {
            PlatformImpl.exit();
            System.exit(0);
        });

        MusicTrack musicTrack = new MusicTrack();
        MusicTrackState musicTrackState = new MusicTrackState();

        BorderPane wrapper = new BorderPane();
        wrapper.setTop(new MtMenuBar(musicTrack));
        Scene scene = new Scene(wrapper);
        KEY_BOARD_CAPTURE = new KeyBoardCapture(scene);
        stage.setScene(scene);

        musicTrack.mediaPlayer().useMediaFile(new File("/Users/Amelia/Music/Amazon Music/twenty one pilots/Blurryface/01-06- Lane Boy.mp3").toURI().toString());
        MusicController musicController = new MusicController(musicTrack.mediaPlayer(), musicTrackState);

        musicTrack.mediaPlayer().currentTimeProperty().addListener((s, o, n) -> {
            musicTrackState.currentTimeProperty().setValue(n.toSeconds());
        });

        MusicTrackEditor editor = new MusicTrackEditor(musicTrack, musicController, musicTrackState);
        wrapper.setCenter(editor);
        stage.setMaximized(true);
        stage.show();
    }//End Method

    public static void main(String[] args) {
        launch();
    }//End Method

}//End Class
