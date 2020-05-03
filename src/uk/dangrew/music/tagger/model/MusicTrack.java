package uk.dangrew.music.tagger.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

    private final StringProperty nameProperty;

    private final ObservableList<Tag> tags;
    private final ObservableList<Tag> publicTags;

    private final ChangeableMedia mediaPlayer;

    public MusicTrack() {
        this.nameProperty = new SimpleStringProperty();
        this.mediaPlayer = new ChangeableMedia();
        this.tags = FXCollections.observableArrayList();
        this.publicTags = new PrivatelyModifiableObservableListImpl<>(new SortedList<>(tags));
    }

    public StringProperty nameProperty() {
        return nameProperty;
    }

    public void tag(MusicTimestamp musicTimestamp) {
        this.tags.add(new Tag(musicTimestamp));
    }

    public void tag(MusicTimestamp musicTimestamp, String text) {
        this.tags.add(new Tag(musicTimestamp, text));
    }

    public void clearTags(){
        this.tags.clear();
    }

    public ObservableList<Tag> getTags() {
        return publicTags;
    }

    public ChangeableMedia mediaPlayer() {
        return mediaPlayer;
    }

}
