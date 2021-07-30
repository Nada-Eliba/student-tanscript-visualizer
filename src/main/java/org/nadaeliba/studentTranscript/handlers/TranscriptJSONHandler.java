package org.nadaeliba.studentTranscript.handlers;

import org.json.JSONObject;
import org.nadaeliba.studentTranscript.interfaces.CollectionName;
import org.nadaeliba.studentTranscript.models.Course;
import org.nadaeliba.studentTranscript.models.Semester;
import org.nadaeliba.studentTranscript.models.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TranscriptJSONHandler {
    ArrayList<JSONObject> transcriptObjectsJSON;
    ArrayList<JSONObject> transcriptSemestersJSON;
    ArrayList<JSONObject> transcriptCoursesJSON;
    ArrayList<JSONObject> transcriptEdgesJSON;

    public TranscriptJSONHandler() {}

    /**
     * {"$databaseID":{"name":$name, "id": $id, "cumulativeGPA": $cumulativeGPA,
     * "TYPE": "Student" }} //student
     *
     * {"$databaseID":{"season":$season, "academicYear": $academicYear,
     * "TYPE": "Semester"}} //current semester
     *
     * {"$databaseID":{"season":$season, "academicYear": $academicYear,
     * "TYPE": "Semester"}} //completed semester(s)
     *
     * {"$databaseID":{"code":$code, "name": $name, "gpa": $gpa, "grade": $grade
     * "TYPE": "Course"}} //course
     *
     * {"$databaseID":{"_from":$student_id, "_to": $semID, "directed": true
     * "TYPE": "is_currently_enrolled"}} //student -> currentSem
     *
     * {"$databaseID":{"_from":$student_id, "_to": $semID, "directed": true
     * "TYPE": "has_completed"}} //student -> currentSem
     *
     * {"$databaseID":{"_from":$semester_id, "_to": $courseID, "directed": true
     * "TYPE": "has_courses"}} //Semester -> course
     */
    public ArrayList<JSONObject> getTranscriptObjectsJSON(
            HashMap<String, Object> transcriptObjects) {

        transcriptObjectsJSON = new ArrayList<>();
        transcriptSemestersJSON = new ArrayList<>();
        transcriptCoursesJSON =  new ArrayList<>();
        transcriptEdgesJSON = new ArrayList<>();

        int edgeId = 1;
        JSONObject vertexJSON;
        JSONObject edgeJSON;


        Student student = (Student) transcriptObjects.get("student");
        vertexJSON = new JSONObject()
                .put("name", student.getName())
                .put("id", student.getId())
                .put("TYPE", "Student");
        transcriptObjectsJSON.add(addVertex(String.valueOf(student.getKey()),
                vertexJSON));

        Semester currentSemester = (Semester) transcriptObjects
                .get("currentSemester");
        vertexJSON = new JSONObject()
                .put("academicYear", currentSemester.getAcademicYear())
                .put("season", currentSemester.getSeason())
                .put("TYPE", "Semester");
        transcriptSemestersJSON.add(addVertex(String.valueOf(currentSemester.getKey()),
                vertexJSON));

        edgeJSON = new JSONObject()
            .put("source", student.getKey())
            .put("target", currentSemester.getKey())
            .put("directed", true)
            .put("TYPE",
                CollectionName.EDGE_COLLECTION_NAME__STUDENT_CURRENT_SEMESTER);
        transcriptEdgesJSON.add(addEdge(String.valueOf(++edgeId), edgeJSON));
        ArrayList<Course> currentSemesterCourses
            = (ArrayList<Course>) transcriptObjects.get("currentSemesterCourses");
        for(Course course : currentSemesterCourses) {
            vertexJSON = new JSONObject()
                .put("code", course.getCode())
                .put("name", course.getName())
                .put("gpa", "null")
                .put("grade", Course.Grade.U)
                .put("TYPE", "Course");
            transcriptCoursesJSON.add(addVertex(String.valueOf(course.getKey()),
                vertexJSON));

            edgeJSON = new JSONObject()
                    .put("source", currentSemester.getKey())
                    .put("target", String.valueOf(course.getKey()))
                    .put("directed", true)
                    .put("TYPE",
                        CollectionName.EDGE_COLLECTION_NAME__SEMESTER_COURSES);
            transcriptEdgesJSON.add(addEdge(String.valueOf(++edgeId), edgeJSON));
        }

        HashMap<Semester, ArrayList<Course>> completedSemestersWithCourses
            = (HashMap<Semester, ArrayList<Course>>)
                transcriptObjects.get("completedSemestersWithCourses");
        for(Map.Entry semesterWithCourses : completedSemestersWithCourses.entrySet()){
            Semester completedSemester = (Semester) semesterWithCourses.getKey();
            ArrayList<Course> courses = (ArrayList<Course>) semesterWithCourses.getValue();

            vertexJSON = new JSONObject()
                    .put("academicYear", completedSemester.getAcademicYear())
                    .put("season", completedSemester.getSeason())
                    .put("TYPE", "Semester");
            transcriptSemestersJSON.add(addVertex(String.valueOf(completedSemester.getKey()),
                    vertexJSON));

            edgeJSON = new JSONObject()
                    .put("source", student.getKey())
                    .put("target", completedSemester.getKey())
                    .put("directed", true)
                    .put("TYPE",
                        CollectionName.EDGE_COLLECTION_NAME__STUDENT_COMPLETED_SEMESTER);
            transcriptEdgesJSON.add(addEdge(String.valueOf(++edgeId), edgeJSON));

            for(Course course : courses) {
                vertexJSON = new JSONObject()
                        .put("code", course.getCode())
                        .put("name", course.getName())
                        .put("gpa", course.getGpa())
                        .put("grade", course.getGrade())
                        .put("TYPE", "Course");

                transcriptCoursesJSON.add(addVertex(String.valueOf(course.getKey()),
                        vertexJSON));

                edgeJSON = new JSONObject()
                        .put("source", completedSemester.getKey())
                        .put("target", String.valueOf(course.getKey()))
                        .put("directed", true)
                        .put("TYPE",
                                CollectionName.EDGE_COLLECTION_NAME__SEMESTER_COURSES);
                transcriptEdgesJSON.add(addEdge(String.valueOf(++edgeId), edgeJSON));
            }
        }
        transcriptObjectsJSON.addAll(transcriptSemestersJSON);
        transcriptObjectsJSON.addAll(transcriptCoursesJSON);
        transcriptObjectsJSON.addAll(transcriptEdgesJSON);

        return transcriptObjectsJSON;
    }

    private JSONObject addVertex(String key, JSONObject vertexJSON){
        JSONObject vertexJSONWithID;
        JSONObject addVertexJSON;

        vertexJSONWithID = new JSONObject()
                .put(key, vertexJSON);

        addVertexJSON = new JSONObject()
                .put("an", vertexJSONWithID);
        return addVertexJSON;
    }

    private JSONObject addEdge(String key, JSONObject edgeJSON){
        JSONObject edgeJSONWithID;
        JSONObject addEdgeJSON;

        edgeJSONWithID = new JSONObject()
                .put(key, edgeJSON);

        addEdgeJSON = new JSONObject()
                .put("ae", edgeJSONWithID);
        return addEdgeJSON;
    }
}
