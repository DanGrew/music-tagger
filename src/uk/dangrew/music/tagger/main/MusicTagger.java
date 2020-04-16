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
import javafx.scene.media.Media;
import javafx.stage.Stage;
import uk.dangrew.kode.friendly.javafx.FriendlyMediaPlayer;
import uk.dangrew.music.tagger.ui.components.MusicTrackEditor;

import java.io.File;

/**
 * Entry point to the system for launching.
 */
public class MusicTagger extends Application {

    static final String TITLE = "Music Tagger";

    public MusicTagger() {
    }//End Constructor

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle(TITLE);
        stage.setOnCloseRequest(event -> {
            PlatformImpl.exit();
            System.exit(0);
        });

        MusicTrack musicTrack = new MusicTrack();
        musicTrack.mediaPlayer().changeMedia(new FriendlyMediaPlayer(new Media(new File("/Users/Amelia/Music/Amazon Music/twenty one pilots/Blurryface/01-06- Lane Boy.mp3").toURI().toString())));
        musicTrack.tag(new MusicTimestamp(90));
        musicTrack.tag(new MusicTimestamp(120));
        MusicController musicController = new MusicController(musicTrack.mediaPlayer());
        MusicTrackConfiguration configuration = new MusicTrackConfiguration();

        musicTrack.mediaPlayer().currentTimeProperty().addListener((s, o, n) -> {
            configuration.currentTimeProperty().setValue(n.toSeconds());
        });

        stage.setScene(new Scene(new MusicTrackEditor(musicController, configuration)));
        stage.setMaximized(true);
        stage.show();
    }//End Method

    public static void main(String[] args) {
        launch();
    }//End Method

}//End Class
