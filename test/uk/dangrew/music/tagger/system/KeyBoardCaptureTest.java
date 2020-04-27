package uk.dangrew.music.tagger.system;

import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import org.junit.Before;
import org.junit.Test;
import uk.dangrew.music.tagger.system.KeyBoardCapture;
import uk.dangrew.music.tagger.utility.KeyEventCreator;

import java.util.function.Consumer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class KeyBoardCaptureTest {

    private Scene scene;
    private KeyBoardCapture systemUnderTest;

    @Before
    public void initialiseSystemUnderTest(){
        scene = new Scene(new BorderPane());
        systemUnderTest = new KeyBoardCapture(scene);
    }

    @Test
    public void shouldCaptureTypedEventsAndForwardOn(){
        KeyEvent event = new KeyEventCreator().create(KeyEvent.KEY_TYPED);
        Consumer<KeyEvent> listener = mock(Consumer.class);

        systemUnderTest.capture(KeyEvent.KEY_TYPED, listener);

        scene.getOnKeyTyped().handle(event);
        verify(listener).accept(event);

        systemUnderTest.stopCapture(KeyEvent.KEY_TYPED, listener);

        scene.getOnKeyTyped().handle(event);
        verify(listener).accept(event);
    }

    @Test
    public void shouldCapturePressedEventsAndForwardOn(){
        KeyEvent event = new KeyEventCreator().create(KeyEvent.KEY_PRESSED);
        Consumer<KeyEvent> listener = mock(Consumer.class);

        systemUnderTest.capture(KeyEvent.KEY_PRESSED, listener);

        scene.getOnKeyPressed().handle(event);
        verify(listener).accept(event);

        systemUnderTest.stopCapture(KeyEvent.KEY_PRESSED, listener);

        scene.getOnKeyTyped().handle(event);
        verify(listener).accept(event);
    }

    @Test
    public void shouldCaptureReleasedEventsAndForwardOn(){
        KeyEvent event = new KeyEventCreator().create(KeyEvent.KEY_RELEASED);
        Consumer<KeyEvent> listener = mock(Consumer.class);

        systemUnderTest.capture(KeyEvent.KEY_RELEASED, listener);

        scene.getOnKeyReleased().handle(event);
        verify(listener).accept(event);

        systemUnderTest.stopCapture(KeyEvent.KEY_RELEASED, listener);

        scene.getOnKeyTyped().handle(event);
        verify(listener).accept(event);
    }

    @Test public void shouldSupportMultipleRegistrations(){
        KeyEvent event = new KeyEventCreator().create(KeyEvent.KEY_RELEASED);
        Consumer<KeyEvent> listener = mock(Consumer.class);
        Consumer<KeyEvent> listener2 = mock(Consumer.class);
        Consumer<KeyEvent> listener3 = mock(Consumer.class);

        systemUnderTest.capture(KeyEvent.KEY_RELEASED, listener);
        systemUnderTest.capture(KeyEvent.KEY_RELEASED, listener2);
        systemUnderTest.capture(KeyEvent.KEY_RELEASED, listener3);

        scene.getOnKeyReleased().handle(event);
    }

}