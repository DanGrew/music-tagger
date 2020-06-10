package uk.dangrew.music.tagger.ui.components.tagging;

import javafx.collections.ListChangeListener;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import uk.dangrew.music.tagger.model.MusicTrack;
import uk.dangrew.music.tagger.main.MusicTrackState;
import uk.dangrew.music.tagger.model.Tag;
import uk.dangrew.music.tagger.ui.positioning.*;

import java.util.*;

public class TagPaneUi extends Pane {

    static final List<Double> TEXT_POSITIONS = Arrays.asList(0.7, 0.81, 0.92);

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

        populateTags();
        ListChangeListener<Tag> listChangeListener = change -> {
            refreshTags();
            populateTags();
        };
        musicTrack.getTags().addListener( listChangeListener);
    }

    private void populateTags(){
        musicTrack.getTags().forEach(this::createTagLine);
    }

    private void refreshTags(){
        Map<Tag, TagWidget> copy = new HashMap<>(tagWidgets);
        for (Map.Entry<Tag, TagWidget> tagLineEntry : copy.entrySet()) {
            if ( !musicTrack.getTags().contains(tagLineEntry.getKey())){
                tagWidgets.remove(tagLineEntry.getKey());
                getChildren().remove(tagLineEntry.getValue());
            }
        }
    }

    private void createTagLine(Tag tag){
        if ( tagWidgets.containsKey(tag)){
            return;
        }

        TagWidget tagWidget = new TagWidget(tag, musicTrack, musicTrackState, canvasDimensions);
        tagWidgets.put(tag, tagWidget);
        getChildren().add(tagWidget);

        updateTagPositions();
    }

    private void updateTagPositions(){
        int index = 0;

        for (Tag tag : musicTrack.getSortedTags()) {
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
