package com;

import java.io.Serial;
import java.io.Serializable;

/**
 * 学生实体类
 * 实现Serializable支持序列化到文件，现使用File类保存方式
 */
public class Student implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final int MIN_AGE = 15;
    private static final int MAX_AGE = 50;

    private final String studentId; // 学号（唯一标识）
    private final String name; // 姓名
    private final String gender; // 性别
    private final int age; // 年龄
    private final String nativePlace; // 籍贯
    private final String department; // 系别
    private final String major; // 专业
    private final String className; // 班级
    private final StudentStatus status; // 学籍状态

    private Student(Builder builder) {
        // 验证必填字段
        if (builder.studentId == null || builder.studentId.trim().isEmpty()) {
            throw new StudentManagementException("学号不能为空");
        }
        if (builder.name == null || builder.name.trim().isEmpty()) {
            throw new StudentManagementException("姓名不能为空");
        }
        if (builder.gender == null || !builder.gender.matches("[男女]")) {
            throw new StudentManagementException("性别必须是'男'或'女'");
        }
        if (builder.age < MIN_AGE || builder.age > MAX_AGE) {
            throw new StudentManagementException("年龄必须在" + MIN_AGE + "到" + MAX_AGE + "岁之间");
        }
        if (builder.department == null || builder.department.trim().isEmpty()) {
            throw new StudentManagementException("系别不能为空");
        }
        if (builder.major == null || builder.major.trim().isEmpty()) {
            throw new StudentManagementException("专业不能为空");
        }
        if (builder.className == null || builder.className.trim().isEmpty()) {
            throw new StudentManagementException("班级不能为空");
        }

        this.studentId = builder.studentId.trim();
        this.name = builder.name.trim();
        this.gender = builder.gender;
        this.age = builder.age;
        this.nativePlace = builder.nativePlace == null ? "" : builder.nativePlace.trim();
        this.department = builder.department.trim();
        this.major = builder.major.trim();
        this.className = builder.className.trim();
        this.status = builder.status;
    }

    public static class Builder {
        private String studentId;
        private String name;
        private String gender;
        private int age;
        private String nativePlace;
        private String department;
        private String major;
        private String className;
        private StudentStatus status = StudentStatus.ENROLLED; // 默认为入学状态

        public Builder studentId(String studentId) {
            this.studentId = studentId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder age(int age) {
            this.age = age;
            return this;
        }

        public Builder nativePlace(String nativePlace) {
            this.nativePlace = nativePlace;
            return this;
        }

        public Builder department(String department) {
            this.department = department;
            return this;
        }

        public Builder major(String major) {
            this.major = major;
            return this;
        }

        public Builder className(String className) {
            this.className = className;
            return this;
        }

        public Builder status(String status) {
            this.status = StudentStatus.fromString(status);
            return this;
        }

        public Student build() {
            return new Student(this);
        }
    }

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public String getDepartment() {
        return department;
    }

    public String getMajor() {
        return major;
    }

    public String getClassName() {
        return className;
    }

    public StudentStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "学号：'" + studentId + '\'' +
                ", 姓名：'" + name + '\'' +
                ", 性别：'" + gender + '\'' +
                ", 年龄：" + age +
                ", 籍贯：'" + nativePlace + '\'' +
                ", 系别：'" + department + '\'' +
                ", 专业：'" + major + '\'' +
                ", 班级：'" + className + '\'' +
                ", 学籍状态：'" + status.getValue() + " (" + status.getDescription() + ")'";
    }
}
