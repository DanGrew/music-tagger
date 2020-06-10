package uk.dangrew.music.tagger.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

public class Tag implements Comparable<Tag> {

    private final MusicTimestamp musicTimestamp;
    private final StringProperty textProperty;

    public Tag(MusicTimestamp musicTimestamp) {
        this(musicTimestamp, null);
    }

    public Tag(MusicTimestamp musicTimestamp, String text) {
        this.musicTimestamp = musicTimestamp;
        this.textProperty = new SimpleStringProperty(text);
    }

    public MusicTimestamp getMusicTimestamp() {
        return musicTimestamp;
    }

    public StringProperty getTextProperty() {
        return textProperty;
    }

    @Override
    public int compareTo(Tag o) {
        int timestampCompare = Double.compare(this.musicTimestamp.secondsProperty().get(), o.musicTimestamp.secondsProperty().get());
        if (timestampCompare != 0) {
            return timestampCompare;
        }

        if ( textProperty.get() == null && o.textProperty.get() == null ){
            return 0;
        } else if ( textProperty.get() == null ) {
            return -1;
        } else if (o.textProperty.get() == null){
            return 1;
        }
        int textCompare = this.textProperty.get().compareTo(o.textProperty.get());
        return textCompare;
    }
}
