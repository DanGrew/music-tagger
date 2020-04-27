package uk.dangrew.music.tagger.main;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class MtTrackScaleMarkerCalculatorTest {

    private static final double CUSTOM_PRECISION = 0.00001;

    private MusicTrackState configuration;
    private MtTrackScaleMarkerCalculator systemUnderTest;

    @Before
    public void initialiseSystemUnderTest() {
        configuration = new MusicTrackState();
        systemUnderTest = new MtTrackScaleMarkerCalculator(configuration);
    }

    public static final Object[] providePositionCases() {
        return new Object[]{
                /* default load position */
                new Object[]{0.0, 0.3, 5.0, 0.05, 0.0},
                /* one second in */
                new Object[]{1.0, 0.3, 5.0, 0.05, 0.04},
                /* six seconds in */
                new Object[]{6.0, 0.3, 5.0, 0.05, 0.04},

                /* default load position */
                new Object[]{0.0, 0.4, 5.0, 0.05, 0.0},
                /* one second in */
                new Object[]{1.0, 0.4, 5.0, 0.05, 0.04},
                /* six seconds in */
                new Object[]{6.0, 0.4, 5.0, 0.05, 0.04},

                /* current position half way through second */
                new Object[]{0.0, 0.225, 5.0, 0.05, 0.025},
                /* current position on one whole second forward */
                new Object[]{0.0, 0.21, 5.0, 0.05, 0.01},

                // 3s in, 1s position offset, 10s interval, for 10% height
                new Object[]{3.0, 0.21, 10.0, 0.1, 0.08},

                new Object[]{5.0, 0.2, 5.0, 0.05, 0.0},
                new Object[]{5.08, 0.2, 5.0, 0.05, 0.0492},
                new Object[]{5.2, 0.2, 5.0, 0.05, 0.048},
                new Object[]{0, 0.2, 5.0, 0.05 * 0.8, 0.02},
        };
    }//End Method

    @Parameters(method = "providePositionCases")
    @Test
    public void shouldCalculatePositionOffset(
            double currentTime,
            double currentPosition,
            double scaleTimeInterval,
            double scalePositionInterval,
            double result
    ) {
        configuration.currentTimeProperty().set(currentTime);
        configuration.currentPositionProperty().set(currentPosition);
        configuration.scaleTimeIntervalProperty().set(scaleTimeInterval);
        configuration.scalePositionIntervalProperty().set(scalePositionInterval);

        assertThat(systemUnderTest.calculateCurrentMarkerPositionOffset(), is(closeTo(result, CUSTOM_PRECISION)));
    }//End Method

    public static final Object[] provideSecondsCases() {
        return new Object[]{
                /* default load position */
                new Object[]{0.0, 0.2, 5.0, 0.05, -10},
                /* one second in */
                new Object[]{1.0, 0.2, 5.0, 0.05, -5},
                new Object[]{2.0, 0.2, 5.0, 0.05, -5},
                new Object[]{3.0, 0.2, 5.0, 0.05, -5},
                new Object[]{4.0, 0.2, 5.0, 0.05, -5},
                new Object[]{5.0, 0.2, 5.0, 0.05, -5},
                /* six seconds in */
                new Object[]{6.0, 0.2, 5.0, 0.05, 0.0},
                new Object[]{7.0, 0.2, 5.0, 0.05, 0.0},
                new Object[]{8.0, 0.2, 5.0, 0.05, 0.0},
                new Object[]{9.0, 0.2, 5.0, 0.05, 0.0},
                /* six seconds in */
                new Object[]{11.0, 0.2, 5.0, 0.05, 5.0},

                new Object[]{5.08, 0.2, 5.0, 0.05, 0},
                new Object[]{5.2, 0.2, 5.0, 0.05, 0},

                new Object[]{0.0, 0.3, 5.0, 0.05, -20},
                new Object[]{1.0, 0.3, 5.0, 0.05, -15},
                new Object[]{2.0, 0.3, 5.0, 0.05, -15},
                new Object[]{3.0, 0.3, 5.0, 0.05, -15},
                new Object[]{4.0, 0.3, 5.0, 0.05, -15},
                new Object[]{5.0, 0.3, 5.0, 0.05, -15},
                new Object[]{6.0, 0.3, 5.0, 0.05, -10},
                new Object[]{7.0, 0.3, 5.0, 0.05, -10},
                new Object[]{8.0, 0.3, 5.0, 0.05, -10},
                new Object[]{9.0, 0.3, 5.0, 0.05, -10},
                new Object[]{11.0, 0.3, 5.0, 0.05, -5.0},

                /* current position half way through second */
                new Object[]{0.0, 0.225, 5.0, 0.05, -10},
                new Object[]{1.0, 0.225, 5.0, 0.05, -10},
                new Object[]{3.0, 0.225, 5.0, 0.05, -5},
                /* current position on one whole second forward */
                new Object[]{0.0, 0.21, 5.0, 0.05, -10},

                 /* 3s in, 1s position offset, 10s interval, for 10% height */
                new Object[]{3.0, 0.25, 10.0, 0.1, -10},
        };
    }//End Method

    @Parameters(method = "provideSecondsCases")
    @Test
    public void shouldCalculateSecondsOffset(
            double currentTime,
            double currentPosition,
            double scaleTimeInterval,
            double scalePositionInterval,
            double result
    ) {
        configuration.currentTimeProperty().set(currentTime);
        configuration.currentPositionProperty().set(currentPosition);
        configuration.scaleTimeIntervalProperty().set(scaleTimeInterval);
        configuration.scalePositionIntervalProperty().set(scalePositionInterval);

        assertThat(systemUnderTest.calculateScaleStartSeconds(), is(closeTo(result, CUSTOM_PRECISION)));
    }//End Method

}