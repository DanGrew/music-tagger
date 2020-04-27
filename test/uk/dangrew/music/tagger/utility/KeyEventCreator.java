package uk.dangrew.music.tagger.utility;

import javafx.beans.NamedArg;
import javafx.event.EventType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyEventCreator {

    public KeyEvent create(EventType<KeyEvent> type) {
        return create(type, KeyCode.K);
    }

    public KeyEvent create(EventType<KeyEvent> type, KeyCode key) {
        return new KeyEvent(
                null,
                null,
                type,
                " ",
                " ",
                key,
                false,
                false,
                false,
                false
        );
    }

}
