# Java练习项目一：

# 学生管理系统

## 项目简介
本项目是一个基于Java的学生管理系统，支持学生信息的增删改查等基本操作，适用于学习Java面向对象编程（具有一定水平）和基础项目开发。可根据需要自行扩展。

## 主要功能
- 添加学生信息
- 删除学生信息
- 修改学生信息
- 查询学生信息
- 学生状态管理

## 目录结构
此项目由于复杂程度不高，没有进行包的设计和处理，可根据需要，自行处理。
```
untitled/
  └─ src/
      └─ com/
          ├─ Main.java                  # 程序入口
          ├─ QueryType.java             # 查询类型枚举
          ├─ Student.java               # 学生实体类
          ├─ StudentManagementApp.java  # 学生管理主程序
          ├─ StudentManagementException.java # 自定义异常
          ├─ StudentStatus.java         # 学生状态枚举
          ├─ StuService.java            # 学生服务接口
          └─ StuServiceImpl.java        # 学生服务实现类
```

## 如何运行
1. 使用IDEA等IDE导入本项目。
2. 进入`src/com/Main.java`，运行主方法即可启动学生管理系统。

## 环境要求
- 建议使用 **Java 11** 或更高版本
- 推荐使用 IntelliJ IDEA 或 Eclipse 等主流Java IDE

## 注意事项
- 请确保已正确配置Java开发环境（JDK）
- 运行前请检查`src/com`目录结构是否完整
- 如需自定义功能，请遵循面向对象设计原则进行扩展

## 可扩展性
- 支持添加更多学生属性（如邮箱、地址等）
- 可集成数据库实现持久化存储
- 可扩展为Web端或移动端应用
- 支持权限管理、批量导入导出等高级功能
