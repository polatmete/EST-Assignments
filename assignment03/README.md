# Environment Setup Intellij
## Requirements
Java 11 is used for all the exercises. There is no guarantee things will work with other versions. Use JUnit 5 for implementing your test suite.

This assignment covers **Chapter 6: Test doubles and mocks**, **Chapter 7: Designing for testability**, and **Chapter 10: Test code quality**. You will apply the concepts learned to design and implement tests for given exercises.

To guarantee consistency, the following steps are taken for every subtask!

## Naming Conventions
In a first step the project structure is updated to fit the naming conventions of java projects. This includes renaming the packages, some classes as well as splitting java files with multiple class definitions
wherever possible. This is done to create a better overview of the project and can be seen as a purely subjective change. Additionally, all classes are updated to follow the file path `src/main/java/zest/`. The corresponding test classes
are added under `src/test/java/zest/`.

## Java 11
The ```pom.xml``` of every subtask was updated with the following property to guarantee the usage of Java 11.
```
<properties>
    <java.version>11</java.version>
    [...]
</properties>
```

## AssertJ
Some subtasks require `AssertJ` for content comparison when dealing with java instances. The corresponding `pom.xml` contains the following additional dependency:
```
<dependency>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-core</artifactId>
    <version>3.25.3</version>
    <scope>test</scope>
</dependency>
```