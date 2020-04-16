package uk.dangrew.music.tagger.ui;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.dangrew.kode.launch.TestApplication;
import uk.dangrew.music.tagger.recorder.MusicController;
import uk.dangrew.music.tagger.recorder.MusicTrackConfiguration;

import java.util.OptionalDouble;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

public class MusicTrackUiTest {

    private DoubleProperty width;
    private DoubleProperty height;

    private MusicTrackConfiguration configuration;
    @Mock private MusicController controller;
    private MusicTrackUi systemUnderTest;

    @Before
    public void initialiseSystemUnderTest() {
        TestApplication.startPlatform();
        initMocks(this);
        configuration = new MusicTrackConfiguration();
        systemUnderTest = new MusicTrackUi(
                new CanvasDimensions(width = new SimpleDoubleProperty(), height = new SimpleDoubleProperty()),
                controller,
                configuration
        );
    }

    @Test
    public void shouldBePositioning() {
        LinePositioningTester tester = new LinePositioningTester(systemUnderTest.skeleton(), width, height);
        tester.assertThatLineTranslatesWhenWidthDimensionChanges(
                OptionalDouble.of(MusicTrackUi.WIDTH_PORTION),
                OptionalDouble.of(MusicTrackUi.WIDTH_PORTION)
        );
        tester.assertThatLineTranslatesWhenHeightDimensionChanges(
                OptionalDouble.of(MusicTrackUi.START_HEIGHT_PORTION),
                OptionalDouble.of(MusicTrackUi.END_HEIGHT_PORTION)
        );
    }

    @Test public void shouldProvideComponents(){
        assertThat(systemUnderTest.getChildren().contains(systemUnderTest.skeleton()), is(true));
    }

}