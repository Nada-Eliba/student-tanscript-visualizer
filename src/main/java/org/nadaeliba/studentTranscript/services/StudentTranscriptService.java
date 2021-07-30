package org.nadaeliba.studentTranscript.services;

import org.json.JSONArray;
import org.nadaeliba.studentTranscript.models.Course;
import org.nadaeliba.studentTranscript.models.Semester;
import org.nadaeliba.studentTranscript.models.Student;

import org.nadaeliba.studentTranscript.models.Transcript;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

import static java.util.Objects.isNull;

@Service
public class StudentTranscriptService {
    public String getTranscript(int studentID){
        String response = "";
        Transcript transcript = new Transcript(studentID);
        Student student;
        Semester currentSemester;
        ArrayList<Course> currentSemesterCourses;
        ArrayList<Semester> completedSemesters;
        HashMap<Semester, ArrayList<Course>> completedCourses;
        ArrayList<Course> completedSemesterCourses;
        HashMap<String, Object> transcriptObjects;

        student = transcript.getStudent();
        if(isNull(student)) {

            return "";
        }
        transcriptObjects = new HashMap<>();
        transcriptObjects.put("student", student);

        currentSemester = student.getCurrentSemester();
        transcriptObjects.put("currentSemester", currentSemester);

        currentSemesterCourses = currentSemester.getCourses();
        transcriptObjects.put("currentSemesterCourses", currentSemesterCourses);

        completedSemesters = student.getCompletedSemesters();
        if(completedSemesters.isEmpty())
            return "";

        completedCourses = new HashMap<>();
        for(Semester completedSemester : completedSemesters) {
            completedSemesterCourses = completedSemester.getCourses();
            completedCourses.put(completedSemester, completedSemesterCourses);
        }
        transcriptObjects.put("completedSemestersWithCourses", completedCourses);

        response = transcript.getTranscriptText(transcriptObjects);
        System.out.println("============= ");
        return response;
    }

}
