/*
 * ----------------------------------------
 *      Nutrient Usage Tracking System
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2017
 * ----------------------------------------
 */
package uk.dangrew.music.tagger.io;

import uk.dangrew.jupa.json.marshall.DynamicModelMarshaller;
import uk.dangrew.jupa.json.parse.JsonParser;
import uk.dangrew.jupa.json.parse.handle.key.JsonArrayWithObjectParseHandler;
import uk.dangrew.jupa.json.parse.handle.type.DoubleParseHandle;
import uk.dangrew.jupa.json.parse.handle.type.StringParseHandle;
import uk.dangrew.jupa.json.structure.JsonStructure;
import uk.dangrew.jupa.json.write.handle.key.JsonArrayWithObjectWriteHandler;
import uk.dangrew.jupa.json.write.handle.key.JsonValueWriteHandler;
import uk.dangrew.jupa.json.write.handle.type.JsonWriteHandleImpl;
import uk.dangrew.music.tagger.model.MusicTrack;

public class MusicTrackPersistence {

    static final String NAME = "name";
    static final String TAGS = "tags";
    static final String TAG = "tag";

    static final String TIMESTAMP = "timestamp";
    static final String TEXT = "text";

    private final JsonStructure structure;
    private final JsonParser parserWithReadHandles;
    private final MusicTrackParseModel parseModel;
    private final JsonParser parserWithWriteHandles;
    private final MusicTrackWriteModel writeModel;

    public MusicTrackPersistence(MusicTrack musicTrack) {
        this(new MusicTrackParseModel(musicTrack), new MusicTrackWriteModel(musicTrack));
    }//End Constructor

    MusicTrackPersistence(MusicTrackParseModel parseModel, MusicTrackWriteModel writeModel) {
        this.structure = new JsonStructure();
        this.parseModel = parseModel;
        this.parserWithReadHandles = new JsonParser();
        this.writeModel = writeModel;
        this.parserWithWriteHandles = new JsonParser();

        constructStructure();
        constructReadHandles();
        constructWriteHandles();
    }//End Constructor

    /**
     * Method to construct the {@link JsonStructure}.
     */
    private void constructStructure() {
        structure.child(NAME, structure.root());
        structure.array(TAGS, structure.root(), writeModel::initialiseTags);
        structure.child(TAG, TAGS);
        structure.child(TIMESTAMP, TAG);
        structure.child(TEXT, TAG);
    }//End Method

    private void constructReadHandles() {
        parserWithReadHandles.when(NAME, new StringParseHandle(parseModel::setName));
        parserWithReadHandles.when(TAGS, new StringParseHandle(new JsonArrayWithObjectParseHandler<>(
                parseModel::startTag, parseModel::finishTag, null, null)
        ));

        parserWithReadHandles.when(TIMESTAMP, new DoubleParseHandle(parseModel::setTimestamp));
        parserWithReadHandles.when(TEXT, new StringParseHandle(parseModel::setText));
    }//End Method

    private void constructWriteHandles() {
        parserWithWriteHandles.when(NAME, new JsonWriteHandleImpl(new JsonValueWriteHandler(writeModel::getName)));
        parserWithWriteHandles.when(TAGS, new JsonWriteHandleImpl(new JsonArrayWithObjectWriteHandler(
                writeModel::startWritingTag, null, null, null
        )));

        parserWithWriteHandles.when(TIMESTAMP, new JsonWriteHandleImpl(new JsonValueWriteHandler(writeModel::getTimestamp)));
        parserWithWriteHandles.when(TEXT, new JsonWriteHandleImpl(new JsonValueWriteHandler(writeModel::getText)));
    }//End Method

    public JsonStructure structure() {
        return structure;
    }//End Method

    public JsonParser readHandles() {
        return parserWithReadHandles;
    }//End Method

    public JsonParser writeHandles() {
        return parserWithWriteHandles;
    }//End Method

    public DynamicModelMarshaller constructDynamicModelMarshaller(){
        return new DynamicModelMarshaller(
                this::structure,
                readHandles(),
                writeHandles()
        );
    }
}//End Class
