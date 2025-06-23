package com;

/**
 * 学生学籍状态枚举
 */
public enum StudentStatus {
    ENROLLED("入学", "正常入学就读"),
    SUSPENDED("休学", "暂时休学"),
    WITHDRAWN("退学", "已办理退学"),
    REPEATED("留级", "需要重修学年");

    private final String value;
    private final String description;

    StudentStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static StudentStatus fromString(String text) {
        for (StudentStatus status : StudentStatus.values()) {
            if (status.value.equals(text)) {
                return status;
            }
        }
        throw new StudentManagementException(
            String.format("无效的学籍状态：'%s'。有效状态为：%s", 
            text, 
            String.join("、", getValidStatusValues()))
        );
    }

    public static String[] getValidStatusValues() {
        StudentStatus[] statuses = StudentStatus.values();
        String[] values = new String[statuses.length];
        for (int i = 0; i < statuses.length; i++) {
            values[i] = statuses[i].getValue();
        }
        return values;
    }

    @Override
    public String toString() {
        return value;
    }
} 