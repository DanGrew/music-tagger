package uk.dangrew.music.tagger.main;

import uk.dangrew.kode.utility.core.DoubleMath;
import uk.dangrew.music.tagger.ui.components.MtCurrentPosition;

public class MtTrackScaleMarkerCalculator {

    private static final int MODULUS_SCALE_FACTOR = 1000000;

    private final DoubleMath doubleMath;
    private final MusicTrackConfiguration configuration;

    public MtTrackScaleMarkerCalculator(MusicTrackConfiguration configuration) {
        this.configuration = configuration;
        this.doubleMath = new DoubleMath(MODULUS_SCALE_FACTOR);
    }

    public double calculateCurrentMarkerPositionOffset() {
        double currentTime = configuration.currentTimeProperty().get();
        double currentPosition = configuration.currentPositionProperty().get();
        double scaleTimeInterval = configuration.scaleTimeIntervalProperty().get();
        double scalePositionInterval = configuration.scalePositionIntervalProperty().get();

        double timeSecondsOffset = doubleMath.doubleModulus(currentTime, scaleTimeInterval);
        double invertedSecondsOffset = scaleTimeInterval - timeSecondsOffset;
        double heightPortionPerSecond = doubleMath.doubleDivision(scalePositionInterval, scaleTimeInterval);
        double timePortionOffset = invertedSecondsOffset * heightPortionPerSecond;

        double positionOffset = doubleMath.doubleModulus(currentPosition, scalePositionInterval);
        double totalOffset = timePortionOffset + positionOffset;
        return doubleMath.doubleModulus(totalOffset, scalePositionInterval);
    }

    public double calculateScaleStartSeconds() {
        double currentTime = configuration.currentTimeProperty().get();
        double currentPosition = configuration.currentPositionProperty().get();
        double scaleTimeInterval = configuration.scaleTimeIntervalProperty().get();
        double scalePositionInterval = configuration.scalePositionIntervalProperty().get();

        double portionBetweenMinimumAndCurrent = currentPosition - MtCurrentPosition.MINIMUM_POSITION;
        double secondsOffsettingMarkers = doubleMath.doubleModulus(currentTime, scaleTimeInterval);
        double portionPerSecond = doubleMath.doubleDivision(scalePositionInterval, scaleTimeInterval);
        double portionForOffsetSeconds = secondsOffsettingMarkers * portionPerSecond;
        double portionBetweenOffsetSecondsAndCurrent = portionBetweenMinimumAndCurrent - portionForOffsetSeconds;

        int markersBeforeCurrent = (int) doubleMath.doubleDivision(portionBetweenOffsetSecondsAndCurrent, scalePositionInterval);
        double offsetForPositioning = -markersBeforeCurrent * scaleTimeInterval;

        double wholeMarkersPassed = Math.floor(doubleMath.doubleDivision(currentTime, scaleTimeInterval));
        double secondsPassed = wholeMarkersPassed * scaleTimeInterval;
        return secondsPassed + offsetForPositioning;
    }
}
