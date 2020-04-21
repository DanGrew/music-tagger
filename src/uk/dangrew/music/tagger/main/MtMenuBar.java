package uk.dangrew.music.tagger.main;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.media.Media;
import uk.dangrew.kode.friendly.controlsfx.FriendlyFileChooser;
import uk.dangrew.kode.friendly.javafx.FriendlyMediaPlayer;

import java.io.File;
import java.util.function.Function;

public class MtMenuBar extends MenuBar {

    private final FriendlyFileChooser fileChooser;
    private final Function<File, FriendlyMediaPlayer> mediaProvider;
    private final MusicTrack musicTrack;
    private final MenuItem loadTrack;

    public MtMenuBar(MusicTrack musicTrack) {
        this(
                new FriendlyFileChooser(),
                file -> new FriendlyMediaPlayer(new Media(file.toURI().toString())),
                musicTrack
        );
    }

    MtMenuBar(FriendlyFileChooser fileChooser, Function<File, FriendlyMediaPlayer> mediaProvider, MusicTrack musicTrack) {
        this.musicTrack = musicTrack;
        this.fileChooser = fileChooser;
        this.mediaProvider = mediaProvider;

        Menu file = new Menu("File");
        this.getMenus().add(file);

        this.loadTrack = new MenuItem("Load Track");
        this.loadTrack.setOnAction(event -> handleLoadTrack());
        file.getItems().add(loadTrack);
    }

    private void handleLoadTrack() {
        File result = fileChooser.showOpenDialog(null);
        if (result == null) {
            return;
        }
        musicTrack.mediaPlayer().changeMedia(mediaProvider.apply(result));
    }

    MenuItem loadTrack() {
        return loadTrack;
    }
}
