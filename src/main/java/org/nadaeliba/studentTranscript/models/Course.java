package org.nadaeliba.studentTranscript.models;

public class Course {
    public enum Grade{
        A,
        A_MINUS,
        B_PLUS,
        B,
        B_MINUS,
        C_PLUS,
        C,
        C_MINUS,
        D,
        F,
        W,
        U
    }
    private String id;
    private String name;
    private String code;
    private float gpa;
    private Grade grade;
    private String key;

    public Course(){}

    public Course(String id, String name, String code, Float gpa, String key){
        this.id = id;
        this.code = code;
        this.name = name;
        this.gpa = gpa;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public float getGpa(){
        return gpa;
    }
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Grade getGrade(){
        if(gpa >= 3.7)
            return Grade.A;

        if((gpa < 3.7) && (gpa >= 3.3))
            return Grade.A_MINUS;

        if((gpa < 3.3) && (gpa >= 3.0))
            return Grade.B_PLUS;

        if((gpa < 3.0) && (gpa >= 2.7))
            return Grade.B;

        if((gpa < 2.7) && (gpa >= 2.3))
            return Grade.B_MINUS;

        if((gpa < 2.3) && (gpa >= 2.0))
            return Grade.C_PLUS;

        if((gpa < 2.0) && (gpa >= 1.7))
            return Grade.C;

        if((gpa < 1.7) && (gpa >= 1.3))
            return Grade.C_MINUS;

        if((gpa < 1.3) && (gpa >= 1.0))
            return Grade.D;

        if((gpa < 1) && (gpa > 0))
            return Grade.F;

        if(gpa == 0)
            return Grade.W;

        return Grade.U;
    }
}
