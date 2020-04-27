package uk.dangrew.music.tagger.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Tag {

    private final MusicTimestamp musicTimestamp;
    private final StringProperty textProperty;

    public Tag(MusicTimestamp musicTimestamp){
        this.musicTimestamp = musicTimestamp;
        this.textProperty = new SimpleStringProperty();
    }

    public MusicTimestamp getMusicTimestamp() {
        return musicTimestamp;
    }

    public StringProperty getTextProperty() {
        return textProperty;
    }
}
