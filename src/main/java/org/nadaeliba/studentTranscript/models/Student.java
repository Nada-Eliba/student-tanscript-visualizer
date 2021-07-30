package org.nadaeliba.studentTranscript.models;

import com.arangodb.ArangoCursor;
import com.arangodb.util.MapBuilder;
import org.nadaeliba.studentTranscript.handlers.ArangoDBHandler;
import org.nadaeliba.studentTranscript.interfaces.CollectionName;
import org.nadaeliba.studentTranscript.interfaces.QueryString;

import java.util.ArrayList;
import java.util.HashMap;

public class Student {
    private int id;
    private String key;
    private String name;
    private String dataBaseID;
    static final ArangoDBHandler arangoDBHandler = new ArangoDBHandler();

    public Student(){}

    public Student(int id, String name, String databaseID, String key){
        this.id = id;
        this.name = name;
        this.dataBaseID = databaseID;
        this.key = key;
    }

    public String getName(){
        return name;
    }

    public String getDataBaseID() {
        return dataBaseID;
    }

    public int getId(){
        return id;
    }

    public String getKey() {
        return key;
    }

    public Semester getCurrentSemester(){
        // implement database method here
        ArangoCursor<Object> getCurrentSemesterQueryIDResult
                = arangoDBHandler.executeLinkDataQuery
                (QueryString.GET_CURRENT_SEMESTER_ID_QUERY,
                        new MapBuilder()
                            .put("@currentSemesterCollection",
                                CollectionName
                                    .EDGE_COLLECTION_NAME__STUDENT_CURRENT_SEMESTER)
                            .put("studentID", dataBaseID));

        if(getCurrentSemesterQueryIDResult.hasNext()){
            String currentSemesterID
                = String.valueOf(getCurrentSemesterQueryIDResult.next());
            ArangoCursor<Object> getCurrentSemesterQueryResult
                    = arangoDBHandler.executeLinkDataQuery
                    (QueryString.GET_SEMESTER_QUERY,
                        new MapBuilder().put("id", currentSemesterID));

            HashMap<String, String> currentSemesterHashMap
                = (HashMap) getCurrentSemesterQueryResult.next();
            System.out.println("+++++ " + currentSemesterHashMap);
            return new Semester(
                String.valueOf(currentSemesterHashMap.get("academicYear")),
                currentSemesterHashMap.get("season"),
                currentSemesterHashMap.get("_id"),
                currentSemesterHashMap.get("_key"));
        }
        return null;
    }

    public ArrayList<Semester> getCompletedSemesters(){
        // implement database method here
        ArrayList<Semester> completedSemesters = new ArrayList<>();
        ArangoCursor<Object> getCompletedSemestersIDQueryResult
            = arangoDBHandler.executeLinkDataQuery
            (QueryString.GET_COMPLETED_SEMESTERS_ID_QUERY,
                    new MapBuilder()
                        .put("@completedSemestersCollection",
                            CollectionName
                                .EDGE_COLLECTION_NAME__STUDENT_COMPLETED_SEMESTER)
                        .put("studentID", dataBaseID));

        while(getCompletedSemestersIDQueryResult.hasNext()){
            String completedSemesterID
                = String.valueOf(getCompletedSemestersIDQueryResult.next());
            ArangoCursor<Object> getCompletedSemestersQueryResult
                    = arangoDBHandler.executeLinkDataQuery
                    (QueryString.GET_SEMESTER_QUERY,
                        new MapBuilder().put("id", completedSemesterID));
            HashMap<String, String> completedSemestersHashmap
                = (HashMap) getCompletedSemestersQueryResult.next();
            completedSemesters.add(new Semester(
                    String.valueOf(completedSemestersHashmap.get("academicYear")),
                    completedSemestersHashmap.get("season"),
                    completedSemestersHashmap.get("_id"),
                    completedSemestersHashmap.get("_key")
                )
            );
        }
        return completedSemesters;
    }
}
