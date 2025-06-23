package com;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 * 系统入口：菜单交互
 * 依赖StudentService接口，解耦具体实现
 */
class StudentManagementApp {
    private final StuService studentService;
    private final Scanner scanner;
    private static final String INVALID_INPUT = "输入无效，请重新输入";

    public StudentManagementApp(StuService studentService) {
        this.studentService = studentService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("正在初始化系统...");
        try {
            // 获取数据文件路径
            String userHome = System.getProperty("user.home");
            Path dataFile = Paths.get(userHome, "student_data.txt");
            
            if (Files.exists(dataFile)) {
                System.out.println("检测到现有数据文件");
            } else {
                System.out.println("未检测到数据文件，将在保存时创建");
            }
            
            // 启动时加载文件
            studentService.loadFromFile();

            System.out.println("\n=== 学生学籍管理系统 ===");

            //noinspection InfiniteLoopStatement （忽略无限循环语句检查）
            while (true) {
                try {
                    printMenu();
                    int choice = readIntInput("请选择操作(1-7): ", 1, 7);
                    handleChoice(choice);
                } catch (StudentManagementException e) {
                    System.err.println("操作失败: " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("系统错误: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("系统启动失败: " + e.getMessage());
            System.exit(1);
        }
    }

    private void printMenu() {
        System.out.println("\n请选择操作：");
        System.out.println("1. 注册学生");
        System.out.println("2. 修改学籍");
        System.out.println("3. 删除学籍");
        System.out.println("4. 查询学籍");
        System.out.println("5. 保存到文件");
        System.out.println("6. 列出所有信息");
        System.out.println("7. 退出系统");
    }

    private void handleChoice(int choice) {
        switch (choice) {
            case 1 -> registerStudent();
            case 2 -> updateStudent();
            case 3 -> deleteStudent();
            case 4 -> queryStudent();
            case 5 -> {
                studentService.saveToFile();
                System.out.println("数据已保存");
            }
            case 6 -> studentService.OutDate();
            case 7 -> {
                System.out.println("正在保存数据...");
                studentService.saveToFile();
                System.out.println("系统退出");
                System.exit(0);
            }
            default -> System.out.println("无效选项，请重新选择");
        }
    }

    private void registerStudent() {
        System.out.println("=== 注册学生 ===");
        try {
            String studentId = readNonEmptyInput("请输入学号: ");
            String name = readNonEmptyInput("请输入姓名: ");
            String gender = readGender();
            int age = readIntInput("请输入年龄: ", 15, 50);
            String nativePlace = readInput("请输入籍贯: ");
            String department = readNonEmptyInput("请输入系别: ");
            String major = readNonEmptyInput("请输入专业: ");
            String className = readNonEmptyInput("请输入班级: ");
            String status = readStatus();

            Student student = new Student.Builder()
                    .studentId(studentId)
                    .name(name)
                    .gender(gender)
                    .age(age)
                    .nativePlace(nativePlace)
                    .department(department)
                    .major(major)
                    .className(className)
                    .status(status)
                    .build();
            studentService.addStudent(student);
        } catch (Exception e) {
            throw new StudentManagementException("注册学生失败: " + e.getMessage());
        }
    }

    private void updateStudent() {
        System.out.println("=== 修改学籍 ===");
        try {
            String studentId = readNonEmptyInput("请输入要修改的学号: ");
            System.out.println("请输入新的学生信息：");
            
            String name = readNonEmptyInput("姓名: ");
            String gender = readGender();
            int age = readIntInput("年龄: ", 15, 50);
            String nativePlace = readInput("籍贯: ");
            String department = readNonEmptyInput("系别: ");
            String major = readNonEmptyInput("专业: ");
            String className = readNonEmptyInput("班级: ");
            String status = readStatus();

            Student newStudent = new Student.Builder()
                    .studentId(studentId)
                    .name(name)
                    .gender(gender)
                    .age(age)
                    .nativePlace(nativePlace)
                    .department(department)
                    .major(major)
                    .className(className)
                    .status(status)
                    .build();
            studentService.updateStudent(studentId, newStudent);
        } catch (Exception e) {
            throw new StudentManagementException("修改学籍失败: " + e.getMessage());
        }
    }

    private void deleteStudent() {
        System.out.println("=== 删除学籍 ===");
        try {
            String studentId = readNonEmptyInput("请输入要删除的学号: ");
            System.out.print("确认删除吗？(y/n): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if ("y".equals(confirm)) {
                studentService.deleteStudent(studentId);
            } else {
                System.out.println("操作已取消");
            }
        } catch (Exception e) {
            throw new StudentManagementException("删除学籍失败: " + e.getMessage());
        }
    }

    private void queryStudent() {
        System.out.println("=== 查询学籍 ===");
        try {
            System.out.println(QueryType.getMenuDisplay());
            int typeChoice = readIntInput("请选择查询类型: ", 1, QueryType.values().length);
            QueryType type = QueryType.fromMenuChoice(typeChoice);
            
            String keyword = readNonEmptyInput("请输入关键词: ");
            List<Student> result = studentService.queryStudents(keyword, type);
            
            if (result.isEmpty()) {
                System.out.println("未找到匹配的学生信息");
            } else {
                System.out.println("找到 " + result.size() + " 条记录：");
                result.forEach(System.out::println);
            }
        } catch (Exception e) {
            throw new StudentManagementException("查询失败: " + e.getMessage());
        }
    }

    private String readInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private String readNonEmptyInput(String prompt) {
        while (true) {
            String input = readInput(prompt);
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println(INVALID_INPUT);
        }
    }

    private int readIntInput(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("请输入%d-%d之间的数字%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println(INVALID_INPUT);
            }
        }
    }

    private String readGender() {
        while (true) {
            String gender = readNonEmptyInput("请输入性别(男/女): ");
            if (gender.equals("男") || gender.equals("女")) {
                return gender;
            }
            System.out.println("性别必须是'男'或'女'");
        }
    }

    private String readStatus() {
        while (true) {
            System.out.println("可用的学籍状态：");
            for (String status : StudentStatus.getValidStatusValues()) {
                System.out.print(status + "  ");
            }
            System.out.println();
            
            String status = readNonEmptyInput("请输入学籍状态: ");
            try {
                StudentStatus.fromString(status);
                return status;
            } catch (StudentManagementException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}