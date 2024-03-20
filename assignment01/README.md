# Environment Setup Intellij
## Requirements
Java 11 is used for all the exercises. There is no guarantee things will work with other versions. Use [JUnit 5](https://maven.apache.org/surefire/maven-surefire-plugin/examples/junit-platform.html) for implementing your test suite.\
You are asked to perform effective and systematic software testing for each exercise's solution. Specifically:
1. Perform **specification-based testing**, following the principles taught in the book and the lectures. Document each principle in the `Documentation.md`. If you find a bug, report the bug, the test that revealed the bug, as well as the bug fix.

2. Enhance the previous test suite using **structural testing**. Specifically, aim for maximizing *condition+branch* coverage, which can be measured using the [JaCoCo](https://www.eclemma.org/jacoco/trunk/doc/maven.html) plugin. Document the process in the `Documentation.md` file, by reporting which conditions, branches, or lines did you miss with specification-based testing (if any), and what tests did you add to cover them.

3. Now that you have a good testing suite, augment it further using **mutation testing** (you will need [PItest](https://pitest.org/quickstart/maven/) and [PItest plugin for JUnit 5](https://github.com/pitest/pitest-junit5-plugin)). Report the mutation coverage in the `Documentation.md`. Explain whether the mutants that survive (if any) are worth writing tests for or not. If the surviving mutants are more than three (3), choose one from each mutation category (*mutator* in PITest terminology).

To guarantee consistency, the following steps are taken for every subtask!

## Java 11
The pom.xml of every subtask was updated with the following property to guarantee the usage of Java 11.
```
<properties>
    <java.version>11</java.version>
    [...]
</properties>
```

## JUnit
In addition, to successfully use JUnit for running the composed tests the following dependency was added to every pom.xml file.
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

## Pitest
For using Pitest to compute the mutation coverage the following plugin was added to the maven configurations (including the JUnit dependency).
```
<build>
    <plugins>
        [...]
        <plugin>
            <groupId>org.pitest</groupId>
            <artifactId>pitest-maven</artifactId>
            <version>1.15.8</version>
            <dependencies>
                <dependency>
                    <groupId>org.pitest</groupId>
                    <artifactId>pitest-junit5-plugin</artifactId>
                    <version>1.2.1</version>
                </dependency>
            </dependencies>
        </plugin>
    </plugins>
</build>
```
The configuration inside Intellij is generated as follows:
1. Open the `Maven` tab on the right.
2. Open the maven configuration for the desired subtask.
3. Expand the directory `Plugins`.
4. Expand the plugin `pitest`.
5. Right-click on the item `pitest:mutationCoverage` and select `Run '<subtask> [pitest:mutationCoverage]'`.
6. After every code change the project is rebuilt before running the mutation coverage.
7. The generated directory `pit-reports` is manually added to the `test_reports` directory.