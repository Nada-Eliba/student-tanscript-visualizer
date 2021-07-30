package org.nadaeliba.studentTranscript.models;

import com.arangodb.ArangoCursor;
import com.arangodb.util.MapBuilder;
import org.nadaeliba.studentTranscript.handlers.ArangoDBHandler;
import org.nadaeliba.studentTranscript.interfaces.CollectionName;
import org.nadaeliba.studentTranscript.interfaces.QueryString;

import java.util.ArrayList;
import java.util.HashMap;

public class Semester {
    static final ArangoDBHandler arangoDBHandler = new ArangoDBHandler();

    public enum Season {
        FALL,
        SPRING,
        SUMMER
    }

    private String academicYear;
    private Season season;
    private String databaseID;
    private String key;

    public Semester(){
    }

    public Semester
        (String academicYear, String season, String dataBaseID, String key){
        this.academicYear = academicYear;
        this.season = Season.valueOf(season.toUpperCase());
        this.databaseID = dataBaseID;
        this.key = key;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public Season getSeason() {
        return season;
    }

    public String getKey() { return key;}

    public ArrayList<Course> getCourses(){
        ArrayList<Course> semesterCourses = new ArrayList<>();
        ArangoCursor<Object> getSemesterCoursesIDQueryResult
            = arangoDBHandler.executeLinkDataQuery
                (QueryString.GET_SEMESTER_COURSES_ID_QUERY,
                    new MapBuilder()
                        .put("@semesterCoursesCollection",
                            CollectionName.EDGE_COLLECTION_NAME__SEMESTER_COURSES)
                        .put("semesterID", databaseID));
        while(getSemesterCoursesIDQueryResult.hasNext()){
            String courseID
                = String.valueOf(getSemesterCoursesIDQueryResult.next());
            ArangoCursor<Object> getSemesterCoursesQueryResult
                    = arangoDBHandler.executeLinkDataQuery
                        (QueryString.GET_COURSE_QUERY,
                            new MapBuilder().put("id", courseID));
            HashMap<String, String> courseHashmap
                = (HashMap) getSemesterCoursesQueryResult.next();
            semesterCourses.add(new Course(courseID,
                courseHashmap.get("name"),
                courseHashmap.get("code"),
                Float.parseFloat(String.valueOf(courseHashmap.get("gpa"))),
                String.valueOf(courseHashmap.get("_key"))));
        }
        return semesterCourses;
    }
}
