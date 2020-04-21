package uk.dangrew.music.tagger.main;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.dangrew.kode.friendly.controlsfx.FriendlyFileChooser;
import uk.dangrew.kode.friendly.javafx.FriendlyMediaPlayer;
import uk.dangrew.kode.launch.TestApplication;

import java.io.File;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class MtMenuBarTest {

    @Mock private FriendlyMediaPlayer mediaPlayer;

    @Mock private FriendlyFileChooser fileChooser;
    @Mock private ChangeableMedia changeableMedia;
    @Mock private MusicTrack musicTrack;
    private MtMenuBar systemUnderTest;

    @Before public void initialiseSystemUnderTest(){
        TestApplication.startPlatform();
        initMocks(this);

        when(musicTrack.mediaPlayer()).thenReturn(changeableMedia);
        systemUnderTest = new MtMenuBar(fileChooser, file -> mediaPlayer, musicTrack);
    }

    @Test public void shouldChangeMedia(){
        when(fileChooser.showOpenDialog(null)).thenReturn(null);
        systemUnderTest.loadTrack().fire();
        verify(changeableMedia, never()).changeMedia(any());

        when(fileChooser.showOpenDialog(null)).thenReturn(mock(File.class));
        systemUnderTest.loadTrack().fire();
        verify(changeableMedia).changeMedia(mediaPlayer);
    }

}