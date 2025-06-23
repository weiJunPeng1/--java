package com;

/**
 * 查询类型枚举
 */
public enum QueryType {
    STUDENT_ID("学号", "按学号查询", Student::getStudentId),
    NAME("姓名", "按姓名查询", Student::getName),
    STATUS("学籍状态", "按学籍状态查询", student -> student.getStatus().getValue()),
    DEPARTMENT("系别", "按系别查询", Student::getDepartment),
    CLASS("班级", "按班级查询", Student::getClassName);

    private final String label;
    private final StudentFieldGetter fieldGetter;

    QueryType(String label, String ignoredDescription, StudentFieldGetter fieldGetter) {
        this.label = label;
        this.fieldGetter = fieldGetter;
    }

    public String getLabel() {
        return label;
    }
    public String getFieldValue(Student student) {
        return fieldGetter.getField(student);
    }

    public boolean matches(Student student, String keyword) {
        if (keyword == null || student == null) {
            return false;
        }
        String fieldValue = getFieldValue(student);
        return fieldValue != null && fieldValue.contains(keyword);
    }

    public static QueryType fromMenuChoice(int choice) {
        if (choice < 1 || choice > values().length) {
            throw new StudentManagementException(
                String.format("无效的查询类型选择：%d。请选择 1-%d 之间的数字",
                choice,
                values().length)
            );
        }
        return values()[choice - 1];
    }

    public static String getMenuDisplay() {
        StringBuilder menu = new StringBuilder("查询类型：\n");
        QueryType[] types = values();
        for (int i = 0; i < types.length; i++) {
            menu.append(String.format("%d.%s ", i + 1, types[i].getLabel()));
        }
        return menu.toString();
    }


    @FunctionalInterface
    private interface StudentFieldGetter {
        String getField(Student student);
    }
}
