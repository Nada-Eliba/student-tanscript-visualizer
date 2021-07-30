package org.nadaeliba.studentTranscript.interfaces;

public interface QueryString {
    String GET_STUDENT_QUERY
        = "FOR s IN @@studentsCollection FILTER TO_NUMBER(s.id) == @id RETURN s";

    String GET_SEMESTER_QUERY
        = "RETURN DOCUMENT(@id)";

    String GET_COURSE_QUERY
        = "RETURN DOCUMENT(@id)";

    String GET_CURRENT_SEMESTER_ID_QUERY
        = "FOR semesterEdge IN @@currentSemesterCollection " +
            "FILTER semesterEdge._from == @studentID " +
            "RETURN semesterEdge._to";

    String GET_COMPLETED_SEMESTERS_ID_QUERY
        = "FOR semesterEdge IN @@completedSemestersCollection " +
                "FILTER semesterEdge._from == @studentID " +
                "RETURN semesterEdge._to";

    String GET_SEMESTER_COURSES_ID_QUERY
        = "FOR courseEdge IN @@semesterCoursesCollection " +
            "FILTER courseEdge._from == @semesterID " +
            "RETURN courseEdge._to";
}
