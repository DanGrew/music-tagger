package uk.dangrew.music.tagger.ui.components.controls;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.junit.Before;
import org.junit.Test;
import uk.dangrew.music.tagger.main.MusicTrackState;
import uk.dangrew.music.tagger.model.MusicTrack;
import uk.dangrew.music.tagger.ui.positioning.CanvasDimensions;
import uk.dangrew.music.tagger.ui.positioning.NodePositioningTester;
import uk.dangrew.sd.graphics.launch.TestApplication;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class MusicTrackConfigurationUiTest {

    private DoubleProperty width;
    private DoubleProperty height;

    private MusicTrack musicTrack;
    private MusicTrackState musicTrackState;
    private MusicTrackConfigurationUi systemUnderTest;

    @Before
    public void initialiseSystemUnderTest() {
        TestApplication.startPlatform();

        width = new SimpleDoubleProperty(0.0);
        height = new SimpleDoubleProperty(0.0);
        musicTrack = new MusicTrack();
        musicTrackState = new MusicTrackState();
        systemUnderTest = new MusicTrackConfigurationUi(musicTrack, musicTrackState, new CanvasDimensions(width, height));
    }

    @Test
    public void shouldBePositionedOnCanvas() {
        NodePositioningTester tester = new NodePositioningTester(systemUnderTest, width, height);
        tester.assertThatNodeTranslatesWhenWidthDimensionChanges(MusicTrackConfigurationUi.WIDHT_PORTION);
        tester.assertThatNodeTranslatesWhenHeightDimensionChanges(MusicTrackConfigurationUi.HEIGHT_PORTION);
    }

    @Test
    public void shouldUpdateConfiguration() {
        systemUnderTest.getScaleTimeIntervalField().setText("1.5");
        assertThat(musicTrackState.scaleTimeIntervalProperty().get(), is(1.5));

        musicTrackState.scaleTimeIntervalProperty().set(0.6);
        systemUnderTest.getScaleTimeIntervalField().setText("0.6");
    }

    @Test
    public void shouldDisableWhenRecording() {
        assertThat(systemUnderTest.isDisable(), is(false));
        musicTrackState.recordingProperty().set(true);
        assertThat(systemUnderTest.isDisable(), is(true));
    }

    @Test public void shouldUpdateName(){
        assertThat(musicTrack.nameProperty().get(), is(nullValue()));
        systemUnderTest.getNameField().setText("Name");
        assertThat(musicTrack.nameProperty().get(), is("Name"));
        musicTrack.nameProperty().set("Different");
        assertThat(systemUnderTest.getNameField().getText(), is("Different"));
    }
}