package org.nadaeliba.studentTranscript.interfaces;

import com.arangodb.ArangoCollection;

public interface CollectionName {
    /**collections names**/

    String VERTEX_COLLECTION_NAME__STUDENTS = "students";
    String VERTEX_COLLECTION_NAME__SEMESTERS = "semesters";
    String VERTEX_COLLECTION_NAME__COURSES = "courses";
    String EDGE_COLLECTION_NAME__STUDENT_CURRENT_SEMESTER
        = "is_currently_enrolled";
    String EDGE_COLLECTION_NAME__STUDENT_COMPLETED_SEMESTER
        = "has_completed";
    String EDGE_COLLECTION_NAME__SEMESTER_COURSES = "has_course";
}
