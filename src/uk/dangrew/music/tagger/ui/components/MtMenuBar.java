package uk.dangrew.music.tagger.ui.components;

import javafx.scene.control.*;
import javafx.scene.media.Media;
import uk.dangrew.kode.friendly.controlsfx.FriendlyFileChooser;
import uk.dangrew.kode.friendly.javafx.FriendlyMediaPlayer;
import uk.dangrew.jupa.file.protocol.ArbitraryLocationProtocol;
import uk.dangrew.music.tagger.io.MusicTrackPersistence;
import uk.dangrew.music.tagger.model.MusicTrack;
import uk.dangrew.jupa.json.marshall.DynamicModelMarshaller;
import uk.dangrew.music.tagger.ui.SystemAlerts;

import java.io.File;
import java.util.Optional;
import java.util.function.Function;

public class MtMenuBar extends MenuBar {

    private final FriendlyFileChooser fileChooser;
    private final SystemAlerts systemAlerts;
    private final DynamicModelMarshaller dynamicModelMarshaller;

    private final Function<File, FriendlyMediaPlayer> mediaProvider;
    private final MusicTrack musicTrack;

    private final MenuItem loadTrack;
    private final MenuItem loadTags;
    private final MenuItem saveTags;
    private final MenuItem clearTags;

    public MtMenuBar(MusicTrack musicTrack) {
        this(
                new FriendlyFileChooser(),
                new SystemAlerts(),
                file -> new FriendlyMediaPlayer(new Media(file.toURI().toString())),
                new MusicTrackPersistence(musicTrack).constructDynamicModelMarshaller(),
                musicTrack
        );
    }

    MtMenuBar(
            FriendlyFileChooser fileChooser,
            SystemAlerts systemAlerts,
            Function<File, FriendlyMediaPlayer> mediaProvider,
            DynamicModelMarshaller dynamicModelMarshaller,
            MusicTrack musicTrack
    ) {
        this.fileChooser = fileChooser;
        this.systemAlerts = systemAlerts;
        this.mediaProvider = mediaProvider;
        this.dynamicModelMarshaller = dynamicModelMarshaller;
        this.musicTrack = musicTrack;

        Menu file = new Menu("File");
        this.getMenus().add(file);

        this.loadTrack = new MenuItem("Load Audio");
        this.loadTrack.setOnAction(event -> handleLoadTrack());
        file.getItems().add(loadTrack);

        this.loadTags = new MenuItem("Load Tags");
        this.loadTags.setOnAction(event -> loadTags());
        file.getItems().add(loadTags);

        this.saveTags = new MenuItem("Save Tags");
        this.saveTags.setOnAction(event -> save());
        file.getItems().add(saveTags);

        this.clearTags = new MenuItem("Clear Tags");
        this.clearTags.setOnAction(event -> clearTags());
        file.getItems().add(clearTags);
    }

    private void save() {
        File result = fileChooser.showSaveDialog(null);
        if (result == null) {
            return;
        }

        dynamicModelMarshaller.write(new ArbitraryLocationProtocol(result));
    }

    private void loadTags(){
        File result = fileChooser.showOpenDialog(null);
        if (result == null) {
            return;
        }

        clearTags();

        dynamicModelMarshaller.read(new ArbitraryLocationProtocol(result));
    }

    private void clearTags(){
        Optional<ButtonType> clearTagsResult = systemAlerts.showTagClearanceAlert();
        if (clearTagsResult.get() == ButtonType.YES){
            musicTrack.clearTags();
        }
    }

    private void handleLoadTrack() {
        File result = fileChooser.showOpenDialog(null);
        if (result == null) {
            return;
        }
        musicTrack.mediaPlayer().changeMedia(mediaProvider.apply(result));
    }

    MenuItem loadTrackItem() {
        return loadTrack;
    }

    MenuItem loadTagsItem() {
        return loadTags;
    }

    MenuItem saveTagsItem() {
        return saveTags;
    }

    MenuItem clearTagsItem(){
        return clearTags;
    }
}
