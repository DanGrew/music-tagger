/*
 * ----------------------------------------
 *      Nutrient Usage Tracking System
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2017
 * ----------------------------------------
 */
package uk.dangrew.music.tagger.io;

import uk.dangrew.music.tagger.model.MusicTimestamp;
import uk.dangrew.music.tagger.model.MusicTrack;

class MusicTrackParseModel {
   
   private final MusicTrack musicTrack;
   
   private double seconds;
   private String tagText;

   MusicTrackParseModel(MusicTrack musicTrack ) {
      this.musicTrack = musicTrack;
   }//End Constructor
   
   void startTag() {
      this.seconds = 0.0;
      this.tagText = null;
   }//End Method

   void finishTag() {
      musicTrack.tag(new MusicTimestamp(seconds), tagText);
   }//End Method
   
   void setTimestamp(Double value ) {
      this.seconds = value;
   }//End Method
   
   void setText( String value ) {
      this.tagText = value;
   }//End Method

   void setName(String name) {
      musicTrack.nameProperty().set(name);
   }
}//End Class
