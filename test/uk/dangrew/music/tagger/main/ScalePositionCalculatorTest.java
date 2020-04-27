package uk.dangrew.music.tagger.main;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.OptionalDouble;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class ScalePositionCalculatorTest {

    private MusicTrackState musicTrackState;
    private ScalePositionCalculator systemUnderTest;

    @Before public void initialiseSystemUnderTest(){
        musicTrackState = new MusicTrackState();
        systemUnderTest = new ScalePositionCalculator();
    }

    public static final Object[] provideCases() {
        return new Object[]{
                new Object[]{0.0, 0.3, 5.0, 0.05, 0.0, OptionalDouble.of(0.3)},
                new Object[]{0.0, 0.3, 5.0, 0.05, 5.0, OptionalDouble.of(0.35)},
                new Object[]{0.0, 0.3, 5.0, 0.05, 100.0, OptionalDouble.empty()},
                new Object[]{0.0, 0.3, 5.0, 0.05, -5.0, OptionalDouble.of(0.25)},
                new Object[]{0.0, 0.3, 5.0, 0.05, -100.0, OptionalDouble.empty()},

                new Object[]{0.0, 0.3, 5.0, 0.05, 2.5, OptionalDouble.of(0.325)},
                new Object[]{0.0, 0.3, 5.0, 0.05, 4, OptionalDouble.of(0.34)},
                new Object[]{0.0, 0.3, 5.0, 0.05, -1, OptionalDouble.of(0.29)},

                new Object[]{0.0, 0.2, 5.0, 0.05, 0.0, OptionalDouble.of(0.2)},
                new Object[]{0.0, 0.2, 5.0, 0.05, 5.0, OptionalDouble.of(0.25)},
                new Object[]{0.0, 0.2, 5.0, 0.05, 100.0, OptionalDouble.empty()},
                new Object[]{0.0, 0.2, 5.0, 0.05, -5.0, OptionalDouble.of(0.15)},
                new Object[]{0.0, 0.2, 5.0, 0.05, -100.0, OptionalDouble.empty()},

                new Object[]{10.0, 0.3, 5.0, 0.05, 0.0, OptionalDouble.of(0.2)},
                new Object[]{10.0, 0.3, 5.0, 0.05, 5.0, OptionalDouble.of(0.25)},
                new Object[]{10.0, 0.3, 5.0, 0.05, 100.0, OptionalDouble.empty()},
                new Object[]{10.0, 0.3, 5.0, 0.05, -5.0, OptionalDouble.of(0.15)},
                new Object[]{10.0, 0.3, 5.0, 0.05, -100.0, OptionalDouble.empty()},

                new Object[]{0.0, 0.3, 5.0, 0.01, 0.0, OptionalDouble.of(0.3)},
                new Object[]{0.0, 0.3, 5.0, 0.01, 5.0, OptionalDouble.of(0.31)},
                new Object[]{0.0, 0.3, 5.0, 0.01, 1000.0, OptionalDouble.empty()},
                new Object[]{0.0, 0.3, 5.0, 0.01, -5.0, OptionalDouble.of(0.29)},
                new Object[]{0.0, 0.3, 5.0, 0.01, -1000.0, OptionalDouble.empty()},

                new Object[]{0.0, 0.3, 1.0, 0.05, 0.0, OptionalDouble.of(0.3)},
                new Object[]{0.0, 0.3, 1.0, 0.05, 5.0, OptionalDouble.of(0.55)},
                new Object[]{0.0, 0.3, 1.0, 0.05, 100.0, OptionalDouble.empty()},
                new Object[]{0.0, 0.3, 1.0, 0.05, -3.0, OptionalDouble.of(0.15)},
                new Object[]{0.0, 0.3, 1.0, 0.05, -100.0, OptionalDouble.empty()},
        };
    }//End Method

    @Parameters(method = "provideCases")
    @Test
    public void shouldCalculatePositionWithinRange(
            double currentTime,
            double currentPosition,
            double scaleTimeInterval,
            double scalePositionInterval,
            double secondsToCalculateFor,
            OptionalDouble result
    ){
        musicTrackState.currentTimeProperty().set(currentTime);
        musicTrackState.currentPositionProperty().set(currentPosition);
        musicTrackState.scaleTimeIntervalProperty().set(scaleTimeInterval);
        musicTrackState.scalePositionIntervalProperty().set(scalePositionInterval);

        assertThat(systemUnderTest.calculatePositionFor(secondsToCalculateFor, musicTrackState), is(result));
    }


}