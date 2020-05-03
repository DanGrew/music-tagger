package uk.dangrew.music.tagger.io;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import uk.dangrew.jupa.file.protocol.WorkspaceJsonPersistingProtocol;
import uk.dangrew.jupa.json.marshall.DynamicModelMarshaller;
import uk.dangrew.jupa.json.parse.JsonParser;
import uk.dangrew.music.tagger.model.MusicTimestamp;
import uk.dangrew.music.tagger.model.MusicTrack;
import uk.dangrew.music.tagger.model.Tag;

import java.util.Random;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MusicTrackIoIntegrationTest {

    private MusicTrack testConfigurationMusicTrack;
    private MusicTrack populatedMusicTrack;

    private JsonParser writeHandler;
    private DynamicModelMarshaller readMarshaller;

    @Before public void initialiseSystemUnderTest(){
        testConfigurationMusicTrack = new MusicTrack();
        populatedMusicTrack = new MusicTrack();
        provideTestConfiguration();
    }

    private void provideTestConfiguration() {
        testConfigurationMusicTrack.nameProperty().set("Name");

        testConfigurationMusicTrack.tag(new MusicTimestamp(0.11937428652099458), "dc6c7397-b726-46aa-8de3-c0aff8e594ba");
        testConfigurationMusicTrack.tag(new MusicTimestamp(5.013166458975615), "a06bbb35-f92a-4068-a24c-86e0fa23afb7");
        testConfigurationMusicTrack.tag(new MusicTimestamp(7.8336562973764226), "29b84449-aa93-4711-bd01-f9930ec85bde");
        testConfigurationMusicTrack.tag(new MusicTimestamp(8.95285433078947), "0af77ec3-edde-4221-bb50-baee1506f09f");
        testConfigurationMusicTrack.tag(new MusicTimestamp(4.738896400456967), "13a2e251-b0cb-4c91-b8cb-bca9168e8ba5");
        testConfigurationMusicTrack.tag(new MusicTimestamp(5.1138471753885595), "0c2d3388-1f1c-4662-a778-bf98143c6c12");
        testConfigurationMusicTrack.tag(new MusicTimestamp(0.41657345941160107), "8f25e2af-3976-4d8d-86b0-b0ab7ba58484");
        testConfigurationMusicTrack.tag(new MusicTimestamp(0.1577821228126175), "65d526d2-fd0d-4aea-b620-3a7a53867f09");
        testConfigurationMusicTrack.tag(new MusicTimestamp(9.488708593167813), "39db190e-3067-4ecd-a6f1-26a6197849ea");
        testConfigurationMusicTrack.tag(new MusicTimestamp(4.7890661038712015), "9156d579-f47c-4dc9-893e-c77e1d2e2f1a");
    }

    @Test
    public void shouldWriteFile() {
        MusicTrackPersistence writePersistence = new MusicTrackPersistence(testConfigurationMusicTrack);

        JSONObject jsonObject = new JSONObject();
        writePersistence.structure().build(jsonObject);
        writePersistence.writeHandles().parse(jsonObject);

        MusicTrackPersistence readPersistence = new MusicTrackPersistence(populatedMusicTrack);
        readPersistence.readHandles().parse(jsonObject);

        System.out.println(jsonObject);
        assertThatTestConfigurationDataIsPresent();
    }

    @Test public void shouldReadFile() {
        MusicTrackPersistence readPersistence = new MusicTrackPersistence(populatedMusicTrack);
        readMarshaller = new DynamicModelMarshaller(
                readPersistence::structure,
                readPersistence.readHandles(),
                readPersistence.writeHandles()
        );
        readMarshaller.read(new WorkspaceJsonPersistingProtocol("music-track.txt", getClass()));

        assertThatTestConfigurationDataIsPresent();
    }

    private void assertThatTestConfigurationDataIsPresent(){
        assertThat(populatedMusicTrack.nameProperty().get(), is(testConfigurationMusicTrack.nameProperty().get()));
        assertThat(populatedMusicTrack.getTags().size(), is(testConfigurationMusicTrack.getTags().size()));

        for (int i = 0; i < populatedMusicTrack.getTags().size(); i++) {
            Tag writeTag = testConfigurationMusicTrack.getTags().get(i);
            Tag readTag = populatedMusicTrack.getTags().get(i);

            assertThat(writeTag.getMusicTimestamp().seconds(), is(readTag.getMusicTimestamp().seconds()));
            assertThat(writeTag.getTextProperty().get(), is(readTag.getTextProperty().get()));
        }
    }

}