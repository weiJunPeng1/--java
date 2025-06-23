package com;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 学生学籍业务实现类
 */
public class StuServiceImpl implements StuService {
    private static final String DATA_DIR = "D:\\StudentManagement";
    private static final String DATA_FILE = "student_data.txt";
    private final List<Student> studentList = new ArrayList<>();
    private final Path dataFilePath;

    public StuServiceImpl() {
        this.dataFilePath = Paths.get(DATA_DIR, DATA_FILE);
        // 确保目录存在
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            throw new StudentManagementException("无法创建数据目录: " + e.getMessage());
        }
        loadFromFile();
    }

    @Override
    public void addStudent(Student student) {
        // 检查学号是否已存在
        if (studentList.stream().anyMatch(s -> s.getStudentId().equals(student.getStudentId()))) {
            throw new StudentManagementException("学号 [" + student.getStudentId() + "] 已存在");
        }

        studentList.add(student);
        try {
            saveToFile();
            System.out.println("学生[" + student.getStudentId() + "] 注册成功");
        } catch (StudentManagementException e) {
            studentList.remove(student);
            throw e;
        }
    }

    @Override
    public void updateStudent(String studentId, Student newStudent) {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new StudentManagementException("学号不能为空");
        }

        // 如果新学号与旧学号不同，检查新学号是否已存在
        if (!studentId.equals(newStudent.getStudentId()) &&
            studentList.stream().anyMatch(s -> s.getStudentId().equals(newStudent.getStudentId()))) {
            throw new StudentManagementException("新学号 [" + newStudent.getStudentId() + "] 已存在");
        }

        boolean found = false;
        for (int i = 0; i < studentList.size(); i++) {
            if (studentList.get(i).getStudentId().equals(studentId)) {
                studentList.set(i, newStudent);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new StudentManagementException("未找到学号为 [" + studentId + "] 的学生");
        }

        saveToFile();
        System.out.println("学生[" + studentId + "] 信息更新成功");
    }

    @Override
    public void deleteStudent(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new StudentManagementException("学号不能为空");
        }

        int originalSize = studentList.size();
        studentList.removeIf(s -> s.getStudentId().equals(studentId));

        if (studentList.size() == originalSize) {
            throw new StudentManagementException("未找到学号为 [" + studentId + "] 的学生");
        }

        saveToFile();
        System.out.println("学生[" + studentId + "] 信息删除成功");
    }

    @Override
    public List<Student> queryStudents(String keyword, QueryType type) {
        if (keyword == null || type == null) {
            throw new StudentManagementException("搜索关键词和类型不能为空");
        }

        List<Student> results = studentList.stream()
                .filter(student -> type.matches(student, keyword))
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            System.out.println("未找到匹配的学生记录");
        }

        return results;
    }

    @Override
    public void saveToFile() {
        try {
            // 确保父目录存在
            Files.createDirectories(dataFilePath.getParent());
            
            // 创建临时文件
            Path tempFile = Files.createTempFile(dataFilePath.getParent(), "student_", ".tmp");
            
            try (BufferedWriter bw = Files.newBufferedWriter(tempFile)) {
                for (Student student : studentList) {
                    String line = String.join(",",
                            student.getStudentId(),
                            student.getName(),
                            student.getGender(),
                            String.valueOf(student.getAge()),
                            student.getNativePlace(),
                            student.getDepartment(),
                            student.getMajor(),
                            student.getClassName(),
                            student.getStatus().getValue()
                    );
                    bw.write(line);
                    bw.newLine();
                }
                bw.flush();
                
                // 原子性地替换文件
                Files.move(tempFile, dataFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StudentManagementException("保存学生数据失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void loadFromFile() {
        studentList.clear();
        if (!Files.exists(dataFilePath)) {
            return;
        }

        try (BufferedReader br = Files.newBufferedReader(dataFilePath)) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                try {
                    String[] data = line.split(",");
                    if (data.length != 9) {
                        System.err.println("警告：第" + lineNumber + "行数据格式不正确，已跳过");
                        continue;
                    }

                    Student student = new Student.Builder()
                            .studentId(data[0])
                            .name(data[1])
                            .gender(data[2])
                            .age(Integer.parseInt(data[3]))
                            .nativePlace(data[4])
                            .department(data[5])
                            .major(data[6])
                            .className(data[7])
                            .status(data[8])
                            .build();
                    studentList.add(student);
                } catch (NumberFormatException e) {
                    System.err.println("警告：第" + lineNumber + "行年龄格式不正确，已跳过");
                } catch (StudentManagementException e) {
                    System.err.println("警告：第" + lineNumber + "行数据验证失败：" + e.getMessage());
                }
            }
            System.out.println("成功加载" + studentList.size() + "条学生记录");
        } catch (IOException e) {
            throw new StudentManagementException("加载学生数据失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void OutDate() {
        if (studentList.isEmpty()) {
            System.out.println("没有学生记录");
            return;
        }
        
        studentList.forEach(System.out::println);
    }
}