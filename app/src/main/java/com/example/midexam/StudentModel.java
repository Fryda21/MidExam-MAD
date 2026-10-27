package com.example.midexam;

public class StudentModel {
    private String studentId, name;
    private int age;

    public StudentModel(String studentId, String name, int age) {
        this.studentId = studentId;
        this.name = name;
        this.age = age;
    }

    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public int getAge() { return age; }
}