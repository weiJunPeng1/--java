package com;


import java.util.List;

public interface StuService {
    void addStudent(Student student);
    void updateStudent(String userid,Student student);
    void deleteStudent(String userid);
    List<Student> queryStudents(String keyword, QueryType type);
    void saveToFile();

    void loadFromFile();
    void OutDate();
}
