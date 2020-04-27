package uk.dangrew.music.tagger.ui.components.tagging;

import uk.dangrew.kode.utility.core.DoubleMath;
import uk.dangrew.music.tagger.main.MusicTrackState;
import uk.dangrew.music.tagger.ui.components.track.MtCurrentPosition;
import uk.dangrew.music.tagger.ui.components.track.MtTrackScaleMarkerCalculator;

import java.util.OptionalDouble;

public class ScalePositionCalculator {

    private final DoubleMath doubleMath;

    public ScalePositionCalculator(){
        this(new DoubleMath(MtTrackScaleMarkerCalculator.MODULUS_SCALE_FACTOR));
    }

    ScalePositionCalculator(DoubleMath doubleMath){
        this.doubleMath = doubleMath;
    }

    public OptionalDouble calculatePositionFor(double seconds, MusicTrackState configuration){
        double currentTime = configuration.currentTimeProperty().get();
        double currentPosition = configuration.currentPositionProperty().get();
        double scaleTimeInterval = configuration.scaleTimeIntervalProperty().get();
        double scalePositionInterval = configuration.scalePositionIntervalProperty().get();

        double portionPerSecond = doubleMath.doubleDivision(scalePositionInterval, scaleTimeInterval);
        double timeDifference = doubleMath.doubleSubtraction(seconds, currentTime);
        double positionDifference = timeDifference * portionPerSecond;

        double resultingPosition = doubleMath.doubleAddition(currentPosition, positionDifference);
        if ( resultingPosition < MtCurrentPosition.MINIMUM_POSITION ) {
            return OptionalDouble.empty();
        } else if ( resultingPosition > MtCurrentPosition.MAXIMUM_POSITION ) {
            return OptionalDouble.empty();
        } else {
            return OptionalDouble.of(resultingPosition);
        }
    }
}
