package uk.dangrew.music.tagger.main;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import uk.dangrew.music.tagger.ui.positioning.*;

import javax.swing.plaf.TableUI;
import java.util.*;

public class TagPaneUi extends Pane {

    static final List<Double> TEXT_POSITIONS = Arrays.asList(0.7, 0.8, 0.9);

    private final CanvasDimensions canvasDimensions;
    private final MusicTrack musicTrack;
    private final MusicTrackState musicTrackState;
    private final Map<Tag, TagWidget> tagWidgets;

    public TagPaneUi(MusicTrack musicTrack, CanvasDimensions canvasDimensions, MusicTrackState musicTrackState){
        this.canvasDimensions = canvasDimensions;
        this.musicTrack = musicTrack;
        this.musicTrackState = musicTrackState;
        this.tagWidgets = new HashMap<>();

        musicTrackState.currentTimeProperty().addListener( (s, o, n) ->{
            updateTagPositions();
        });

        musicTrack.getTags().forEach(this::createTagLine);
        ListChangeListener<Tag> listChangeListener = change -> musicTrack.getTags().forEach(this::createTagLine);
        musicTrack.getTags().addListener( listChangeListener);
    }

    private void createTagLine(Tag tag){
        if ( tagWidgets.containsKey(tag)){
            return;
        }

        TagWidget tagWidget = new TagWidget(tag, musicTrackState, canvasDimensions);
        tagWidgets.put(tag, tagWidget);
        getChildren().add(tagWidget);
    }

    private void updateTagPositions(){
        int index = 0;

        for (Tag tag : musicTrack.getTags()) {
            index = index % TEXT_POSITIONS.size();

            TagWidget tagWidget = tagWidgets.get(tag);
            if ( tagWidget == null ) {
                continue;
            }
            tagWidget.updatePosition(TEXT_POSITIONS.get(index));

            index++;
        }
    }

    TagWidget tagWidgetFor(Tag tag){
        return tagWidgets.get(tag);
    }

}
