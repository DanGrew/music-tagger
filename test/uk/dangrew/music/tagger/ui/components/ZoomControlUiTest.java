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
import static org.junit.Assert.assertThat;

public class ZoomControlUiTest {

    private DoubleProperty width;
    private DoubleProperty height;

    private MusicTrackState musicTrackState;
    private ZoomControlUi systemUnderTest;

    @Before
    public void initialiseSystemUnderTest(){
        TestApplication.startPlatform();

        width = new SimpleDoubleProperty();
        height = new SimpleDoubleProperty();
        musicTrackState = new MusicTrackState();
        systemUnderTest = new ZoomControlUi(musicTrackState, new CanvasDimensions(width, height));
    }

    @Test
    public void shouldBePositionedOnCanvas(){
        NodePositioningTester tester = new NodePositioningTester(systemUnderTest, width, height);
        tester.assertThatNodeTranslatesWhenWidthDimensionChanges(MtCurrentPosition.WIDTH_END_PORTION);
        tester.assertThatNodeTranslatesWhenHeightDimensionChanges(ZoomControlUi.HEIGHT_PORTION);
    }

    @Test public void shouldDisableWhenRecording(){
        assertThat(systemUnderTest.zoomIn().isDisable(), is(false));
        assertThat(systemUnderTest.zoomOut().isDisable(), is(false));

        musicTrackState.recordingProperty().set(true);

        assertThat(systemUnderTest.zoomIn().isDisable(), is(true));
        assertThat(systemUnderTest.zoomOut().isDisable(), is(true));
    }

    @Test public void shouldChangeScalePositionInterval(){
        musicTrackState.scalePositionIntervalProperty().set(1.0);

        systemUnderTest.zoomIn().fire();
        assertThat(musicTrackState.scalePositionIntervalProperty().get(), is(ZoomControlUi.ZOOM_IN_FACTOR));

        systemUnderTest.zoomOut().fire();
        assertThat(musicTrackState.scalePositionIntervalProperty().get(), is(1.0));

        systemUnderTest.zoomOut().fire();
        assertThat(musicTrackState.scalePositionIntervalProperty().get(), is(ZoomControlUi.ZOOM_OUT_FACTOR));
    }
}