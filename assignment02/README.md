# Environment Setup Intellij
## Requirements
Java 11 is used for all the exercises. There is no guarantee things will work with other versions. Use JUnit 5 for implementing your test suite.

1. In this task, you are required to achieve the highest possible (ideally, 100\%) line coverage. for the provided Java solutions. Utilize the ```JaCoCo``` plugin to analyze and generate coverage reports.
2. Use **property-based testing** techniques to derive tests for the provided Java solutions.
   - Identify properties that should hold true for any inputs and document your rationale.
   - Use a property-based testing framework to automate the testing process.
   - hint: Add ```jqwik``` framework to your ```pom.xml```

To guarantee consistency, the following steps are taken for every subtask!

## Naming Conventions
In a first step the project structure is updated to fit the naming conventions of java projects. This includes renaming the packages, some classes as well as splitting java files with multiple class definitions
wherever possible. This is done to create a better overview of the project and can be seen as a purely subjective change.

## Java 11
The ```pom.xml``` of every subtask was updated with the following property to guarantee the usage of Java 11.
```
<properties>
    <java.version>11</java.version>
    [...]
</properties>
```

## JUnit
In addition, to successfully use JUnit for running the composed tests the following dependency was added to every ```pom.xml``` file.
```
<dependencies>
    <dependency>
        <groupId>org.junit.platform</groupId>
        <artifactId>junit-platform-launcher</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.junit.vintage</groupId>
        <artifactId>junit-vintage-engine</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.junit</groupId>
            <artifactId>junit-bom</artifactId>
            <version>5.10.2</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

## JaCoCo
For setting up JaCoCo as a coverage runner the following plugin was added to the maven configurations.
```
<build>
    <plugins>
        <plugin>
            <groupId>org.jacoco</groupId>
            <artifactId>jacoco-maven-plugin</artifactId>
            <version>0.8.11</version>
        </plugin>
    <plugins>
<build>
```
Then the JUnit configuration inside Intellij is updated as follows:
1. Click on `Modify options`.
2. Under `Code Coverage` the option `Specify alternative coverage runner` is selected.
3. In the appearing drop-down at the bottom the coverage runner `JaCoCo` is chosen.
4. By running the test suite in coverage mode the test report is generated and manually added to a `test_reports` directory under `jacoco-reports`.

## jqwik
For using ```jqwik``` to test properties the following dependency was added to the maven configurations.
```
<dependencies>
    [...]
    <dependency>
        <groupId>net.jqwik</groupId>
        <artifactId>jqwik</artifactId>
        <version>1.8.4</version>
        <scope>test</scope>
    </dependency>
<dependencies>
```
Properties are tested whenever the JUnit test suite is executed. The output is copy-pasted into the corresponding ```log.txt```.

## Additional Dependencies
For the task ```unique_paths_grid``` an additional dependency is used to compute binomial coefficients. This guarantees usage of various code during computation to
test for the property.
```
<dependencies>
    [...]
    <dependency>
        <groupId>com.google.guava</groupId>
        <artifactId>guava</artifactId>
        <version>33.1.0-jre</version>
    </dependency>
<dependencies>
```