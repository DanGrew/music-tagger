package uk.dangrew.music.tagger.ui.components;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.junit.Before;
import org.junit.Test;
import uk.dangrew.kode.launch.TestApplication;
import uk.dangrew.music.tagger.main.MusicTrackState;
import uk.dangrew.music.tagger.ui.positioning.CanvasDimensions;
import uk.dangrew.music.tagger.ui.positioning.NodePositioningTester;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class MusicTrackConfigurationUiTest {

    private DoubleProperty width;
    private DoubleProperty height;

    private MusicTrackState musicTrackState;
    private MusicTrackConfigurationUi systemUnderTest;

    @Before
    public void initialiseSystemUnderTest() {
        TestApplication.startPlatform();

        width = new SimpleDoubleProperty(0.0);
        height = new SimpleDoubleProperty(0.0);
        musicTrackState = new MusicTrackState();
        systemUnderTest = new MusicTrackConfigurationUi(musicTrackState, new CanvasDimensions(width, height));
    }

    @Test
    public void shouldBePositionedOnCanvas() {
        NodePositioningTester tester = new NodePositioningTester(systemUnderTest, width, height);
        tester.assertThatNodeTranslatesWhenWidthDimensionChanges(MusicTrackConfigurationUi.WIDHT_PORTION);
        tester.assertThatNodeTranslatesWhenHeightDimensionChanges(MusicTrackConfigurationUi.HEIGHT_PORTION);
    }

    @Test
    public void shouldUpdateConfiguration() {
        systemUnderTest.getScaleTimeInterval().setText("1.5");
        assertThat(musicTrackState.scaleTimeIntervalProperty().get(), is(1.5));

        musicTrackState.scaleTimeIntervalProperty().set(0.6);
        systemUnderTest.getScaleTimeInterval().setText("0.6");
    }

    @Test
    public void shouldDisableWhenRecording() {
        assertThat(systemUnderTest.isDisable(), is(false));
        musicTrackState.recordingProperty().set(true);
        assertThat(systemUnderTest.isDisable(), is(true));
    }
}