package uk.dangrew.music.tagger.system;

import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class KeyBoardCapture {

    private final Scene scene;
    private final Map<EventType<KeyEvent>, Set<Consumer<KeyEvent>>> registrations;

    public KeyBoardCapture(Scene scene){
        this.scene = scene;
        this.registrations = new LinkedHashMap<>();
        this.registrations.put(KeyEvent.KEY_PRESSED, new LinkedHashSet<>());
        this.registrations.put(KeyEvent.KEY_RELEASED, new LinkedHashSet<>());
        this.registrations.put(KeyEvent.KEY_TYPED, new LinkedHashSet<>());

        this.scene.setOnKeyPressed(this::notifyKeyEvent);
        this.scene.setOnKeyReleased(this::notifyKeyEvent);
        this.scene.setOnKeyTyped(this::notifyKeyEvent);
    }

    public void capture(EventType<KeyEvent> type, Consumer<KeyEvent> consumer){
        registrations.get(type).add(consumer);
    }

    public void stopCapture(EventType<KeyEvent> type, Consumer<KeyEvent> consumer){
        registrations.get(type).remove(consumer);
    }

    private void notifyKeyEvent(KeyEvent keyEvent){
        registrations.get(keyEvent.getEventType()).forEach( listener -> listener.accept(keyEvent));
    }
}
