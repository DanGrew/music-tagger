/*
 * ----------------------------------------
 *      Nutrient Usage Tracking System
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2017
 * ----------------------------------------
 */
package uk.dangrew.music.tagger.io;

import uk.dangrew.music.tagger.model.MusicTrack;
import uk.dangrew.music.tagger.model.Tag;

import java.util.ArrayList;
import java.util.List;

class MusicTrackWriteModel {
   
   private final MusicTrack musicTrack;
   private final List< Tag > tagBuffer;
   private Tag currentTag;
   
   MusicTrackWriteModel(MusicTrack musicTrack ) {
      this.musicTrack = musicTrack;
      this.tagBuffer = new ArrayList<>();
   }//End Constructor

   Integer initialiseTags( String key ){
      tagBuffer.addAll(musicTrack.getTags());
      return tagBuffer.size();
   }//End Method
   
   void startWritingTag() {
      if ( tagBuffer.isEmpty() ) {
         return;
      }
      this.currentTag = tagBuffer.remove( 0 );
   }//End Method

   Double getTimestamp() {
      return currentTag.getMusicTimestamp().secondsProperty().get();
   }//End Method

   String getText() {
      return currentTag.getTextProperty().get();
   }//End Method

    String getName() {
      return musicTrack.nameProperty().get();
    }
}//End Class
