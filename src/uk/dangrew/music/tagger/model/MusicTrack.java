package uk.dangrew.music.tagger.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.media.Media;
import uk.dangrew.kode.observable.PrivatelyModifiableObservableListImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@link MusicTrack} provides the information for the current focus of the tool, in terms of {@link Media} and model
 * data.
 */
public class MusicTrack {

    private final StringProperty nameProperty;

    private final ObservableList<Tag> tags;
    private final ObservableList<Tag> publicTags;

    private final ChangeableMedia mediaPlayer;

    public MusicTrack() {
        this.nameProperty = new SimpleStringProperty();
        this.mediaPlayer = new ChangeableMedia();
        this.tags = FXCollections.observableArrayList();
        this.publicTags = new PrivatelyModifiableObservableListImpl<>(tags);
    }

    public StringProperty nameProperty() {
        return nameProperty;
    }

    public Tag tag(MusicTimestamp musicTimestamp) {
        return tag(musicTimestamp, null);
    }

    public Tag tag(MusicTimestamp musicTimestamp, String text) {
        Tag tag = new Tag(musicTimestamp, text);
        tags.add(tag);
        return tag;
    }

    public void clearTags(){
        this.tags.clear();
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
    }

    public ObservableList<Tag> getTags() {
        return publicTags;
    }

    public List<Tag> getSortedTags() {
        List<Tag> sorted = new ArrayList<>(getTags());
        Collections.sort(sorted);
        return sorted;
    }

    public ChangeableMedia mediaPlayer() {
        return mediaPlayer;
    }
}
