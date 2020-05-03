package uk.dangrew.music.tagger.ui.components;

import javafx.scene.control.ButtonType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import uk.dangrew.jupa.file.protocol.ArbitraryLocationProtocol;
import uk.dangrew.jupa.json.marshall.DynamicModelMarshaller;
import uk.dangrew.kode.friendly.controlsfx.FriendlyFileChooser;
import uk.dangrew.kode.friendly.javafx.FriendlyMediaPlayer;
import uk.dangrew.music.tagger.model.ChangeableMedia;
import uk.dangrew.music.tagger.model.MusicTrack;
import uk.dangrew.music.tagger.ui.SystemAlerts;
import uk.dangrew.sd.graphics.launch.TestApplication;

import java.io.File;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class MtMenuBarTest {

    @Captor private ArgumentCaptor<ArbitraryLocationProtocol> protocolCaptor;

    @Mock private DynamicModelMarshaller dynamicModelMarshaller;
    @Mock private SystemAlerts systemAlerts;

    @Mock private FriendlyMediaPlayer mediaPlayer;

    @Mock private FriendlyFileChooser fileChooser;
    @Mock private ChangeableMedia changeableMedia;
    @Mock private MusicTrack musicTrack;
    private MtMenuBar systemUnderTest;

    @Before public void initialiseSystemUnderTest(){
        TestApplication.startPlatform();
        initMocks(this);

        when(musicTrack.mediaPlayer()).thenReturn(changeableMedia);
        when(systemAlerts.showTagClearanceAlert()).thenReturn(Optional.of(ButtonType.NO));
        systemUnderTest = new MtMenuBar(
                fileChooser,
                systemAlerts,
                file -> mediaPlayer,
                dynamicModelMarshaller,
                musicTrack
        );
    }

    @Test public void shouldChangeMedia(){
        when(fileChooser.showOpenDialog(null)).thenReturn(null);
        systemUnderTest.loadTrackItem().fire();
        verify(changeableMedia, never()).changeMedia(any());

        when(fileChooser.showOpenDialog(null)).thenReturn(mock(File.class));
        systemUnderTest.loadTrackItem().fire();
        verify(changeableMedia).changeMedia(mediaPlayer);
    }

    @Test public void shouldSaveMusicTrack(){
        File file = mock(File.class);
        when(fileChooser.showSaveDialog(any())).thenReturn(file);

        systemUnderTest.saveTagsItem().fire();
        verify(dynamicModelMarshaller).write(protocolCaptor.capture());
        assertThat(protocolCaptor.getValue().getSource(), is(file));
    }

    @Test public void shouldLoadMusicTrack(){
        File file = mock(File.class);
        when(fileChooser.showOpenDialog(any())).thenReturn(file);

        systemUnderTest.loadTagsItem().fire();
        verify(dynamicModelMarshaller).read(protocolCaptor.capture());
        assertThat(protocolCaptor.getValue().getSource(), is(file));

        verify(musicTrack, never()).clearTags();
    }

    @Test public void shouldClearTagsOnLoad(){
        File file = mock(File.class);
        when(fileChooser.showOpenDialog(any())).thenReturn(file);

        when(systemAlerts.showTagClearanceAlert()).thenReturn(Optional.of(ButtonType.YES));
        systemUnderTest.loadTagsItem().fire();
        verify(dynamicModelMarshaller).read(protocolCaptor.capture());
        assertThat(protocolCaptor.getValue().getSource(), is(file));

        verify(musicTrack).clearTags();
    }

}