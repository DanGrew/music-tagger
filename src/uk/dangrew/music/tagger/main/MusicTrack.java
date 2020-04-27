package uk.dangrew.music.tagger.main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.media.Media;
import uk.dangrew.kode.observable.PrivatelyModifiableObservableListImpl;

/**
 * {@link MusicTrack} provides the information for the current focus of the tool, in terms of {@link Media} and model
 * data.
 */
public class MusicTrack {

    private final ChangeableMedia mediaPlayer;
    private final ObservableList<Tag> tags;
    private final ObservableList<Tag> publicTags;

    public MusicTrack() {
        this.mediaPlayer = new ChangeableMedia();
        this.tags = FXCollections.observableArrayList();
        this.publicTags = new PrivatelyModifiableObservableListImpl<>(new SortedList<>(tags));
    }

    public void tag(MusicTimestamp musicTimestamp) {
        this.tags.add(new Tag(musicTimestamp));
    }

    public ObservableList<Tag> getTags() {
        return publicTags;
    }

    public ChangeableMedia mediaPlayer() {
        return mediaPlayer;
    }
}
