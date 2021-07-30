package org.nadaeliba.studentTranscript.models;

import com.arangodb.ArangoCursor;
import com.arangodb.util.MapBuilder;
import org.json.JSONObject;
import org.nadaeliba.studentTranscript.handlers.ArangoDBHandler;
import org.nadaeliba.studentTranscript.handlers.TranscriptJSONHandler;
import org.nadaeliba.studentTranscript.interfaces.CollectionName;
import org.nadaeliba.studentTranscript.interfaces.QueryString;

import java.util.ArrayList;
import java.util.HashMap;

public class Transcript {
    int studentID;
    String transcriptText;
    static final ArangoDBHandler arangoDBHandler = new ArangoDBHandler();

    public Transcript(){}

    public Transcript(int studentID){
        this.studentID = studentID;
    }

    public Student getStudent(){
        ArangoCursor<Object> getStudentQueryResult
            = arangoDBHandler.executeLinkDataQuery
                (QueryString.GET_STUDENT_QUERY,
                    new MapBuilder()
                        .put("@studentsCollection",
                            CollectionName.VERTEX_COLLECTION_NAME__STUDENTS)
                        .put("id", studentID));
//        System.out.println(studentID);
        if(getStudentQueryResult.hasNext()){
            HashMap<String, String> studentHashMap
                = (HashMap) getStudentQueryResult.next();
            return new Student(Integer.valueOf(studentHashMap.get("id")),
                studentHashMap.get("name"),
                studentHashMap.get("_id"),
                studentHashMap.get("_key"));
        }
        return  null;
    }

    public String getTranscriptText(HashMap<String, Object> transcriptObjects) {
        StringBuilder transcriptStringBuilder = new StringBuilder();
        TranscriptJSONHandler transcriptJSONHandler = new TranscriptJSONHandler();
        ArrayList<JSONObject> transcriptJSONObjects
            = transcriptJSONHandler.getTranscriptObjectsJSON(transcriptObjects);
        for(JSONObject transcriptJSONObject : transcriptJSONObjects){
            transcriptStringBuilder.append(transcriptJSONObject);
            transcriptStringBuilder.append("\r");
        }
        this.transcriptText = transcriptStringBuilder.toString();
        return transcriptText;
    }

}
