package com;


public class Main {
    public static void main(String[] args){
        StuService stuService = new StuServiceImpl();
        StudentManagementApp app = new StudentManagementApp(stuService);
        app.start();
    }
}
