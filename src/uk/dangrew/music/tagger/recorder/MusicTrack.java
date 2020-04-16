package uk.dangrew.music.tagger.recorder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import uk.dangrew.kode.observable.PrivatelyModifiableObservableListImpl;

import java.io.File;
import java.util.List;

/**
 * {@link MusicTrack} provides the information for the current focus of the tool, in terms of {@link Media} and model
 * data.
 */
public class MusicTrack {

    private final ChangeableMedia mediaPlayer;
    private final ObservableList<MusicTimestamp> tags;
    private final ObservableList<MusicTimestamp> publicTags;

    public MusicTrack() {
        this.mediaPlayer = new ChangeableMedia();
        this.tags = FXCollections.observableArrayList();
        this.publicTags = new PrivatelyModifiableObservableListImpl<>(new SortedList<>(tags));
    }

    public void tag(MusicTimestamp musicTimestamp) {
        this.tags.add(musicTimestamp);
    }

    public ObservableList<MusicTimestamp> getTags() {
        return publicTags;
    }

    public ChangeableMedia mediaPlayer() {
        return mediaPlayer;
    }
}
