package zest;


import net.jqwik.api.*;
import net.jqwik.api.constraints.IntRange;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CourseScheduleTest {

    @Test
    void withoutCircles() {
        int[][] input = {{1, 0}};
        assertTrue(CourseSchedule.canFinish(2, input));
    }

    @Test
    void withCircles() {
        int[][] input = {{1, 0}, {0, 1}};
        assertFalse(CourseSchedule.canFinish(2, input));
    }

    @Test
    void emptyPrerequisites() {
        int[][] input = {};
        assertTrue(CourseSchedule.canFinish(2, input));
    }

    @Test
    void nullPrerequisites() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                CourseSchedule.canFinish(2, null));

        assertEquals("Prerequisites are null.", exception.getMessage());
    }

    @Test
    void largeWithoutCircles() {
        int[][] input = {{1, 0}, {4, 0}, {5, 1}, {3, 2}, {2, 5}, {3, 0}};
        assertTrue(CourseSchedule.canFinish(6, input));
    }

    @Test
    void largeWithCircles() {
        int[][] input = {{1, 0}, {0, 4}, {4, 2}, {2, 3}, {3, 5}, {5, 1}};
        assertFalse(CourseSchedule.canFinish(6, input));
    }

    @Test
    void invalidNumCourses() {
        int[][] input = {{1, 0}, {0, 1}};
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                CourseSchedule.canFinish(0, input));

        assertEquals("Number of courses must be higher than 0.", exception.getMessage());
    }

    @Test
    void referencingItself() {
        int[][] input = {{1, 0}, {0, 0}};
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                CourseSchedule.canFinish(2, input));

        assertEquals("Courses can't reference themselves.", exception.getMessage());
    }

    @Test
    void invalidPrerequisite() {
        int[][] input = {{1, 0}, {2, 0}};
        ArrayIndexOutOfBoundsException exception = assertThrows(ArrayIndexOutOfBoundsException.class, () ->
                CourseSchedule.canFinish(2, input));

        assertEquals("Prerequisites references non-existing course.", exception.getMessage());
    }

    @Test
    void negativePrerequisite() {
        int[][] input = {{1, 0}, {2, -1}};
        ArrayIndexOutOfBoundsException exception = assertThrows(ArrayIndexOutOfBoundsException.class, () ->
                CourseSchedule.canFinish(2, input));

        assertEquals("Prerequisites references non-existing course.", exception.getMessage());
    }

    @Property
    void validRangeWithoutCircles(
            @ForAll @IntRange(min = 11, max = 100) int numCourses,
            @ForAll @IntRange(max = 100) int numRequisites
    ) {
        int[][] prerequisites = new int[numRequisites][2];
        for (int i = 0; i < numRequisites; i++) {
            prerequisites[i][0] = Arbitraries.integers().between(0, numCourses / 2 - 1).sample();
            prerequisites[i][1] = Arbitraries.integers().between(numCourses / 2, numCourses - 1).sample();
        }

        assertTrue(CourseSchedule.canFinish(numCourses, prerequisites));
    }

    @Property
    void validRangeWithCircles(
            @ForAll @IntRange(min = 11, max = 100) int numCourses,
            @ForAll @IntRange(min = 2, max = 100) int numRequisites
    ) {
        int[][] prerequisites = new int[numRequisites][2];
        for (int i = 0; i < numRequisites; i++) {
            prerequisites[i][0] = Arbitraries.integers().between(0, numCourses / 2 - 1).sample();
            prerequisites[i][1] = Arbitraries.integers().between(numCourses / 2, numCourses - 1).sample();
        }

        // Change the last tuple to the first one but reversed to make sure there is a circle.
        prerequisites[numRequisites - 1][0] = prerequisites[0][1];
        prerequisites[numRequisites - 1][1] = prerequisites[0][0];

        assertFalse(CourseSchedule.canFinish(numCourses, prerequisites));
    }

    @Property
    void invalidNoOfCourses(
            @ForAll @IntRange(min = Integer.MIN_VALUE, max = 0) int numCourses,
            @ForAll @IntRange(min = 2, max = 100) int numRequisites
    ) {
        int[][] prerequisites = new int[numRequisites][2];
        for (int i = 0; i < numRequisites; i++) {
            prerequisites[i][0] = Arbitraries.integers().between(0, 100).sample();
            prerequisites[i][1] = Arbitraries.integers().between(0, 100).sample();
        }

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                CourseSchedule.canFinish(numCourses, prerequisites));

        assertEquals("Number of courses must be higher than 0.", exception.getMessage());
    }

    @Property
    void referencingItself(
            @ForAll @IntRange(min = 11, max = 100) int numCourses,
            @ForAll @IntRange(min = 2, max = 100) int numRequisites
    ) {
        int[][] prerequisites = new int[numRequisites][2];
        for (int i = 0; i < numRequisites; i++) {
            prerequisites[i][0] = Arbitraries.integers().between(0, numCourses / 2 - 1).sample();
            prerequisites[i][1] = Arbitraries.integers().between(numCourses / 2, numCourses - 1).sample();
        }

        // Change the last tuple so it references itself.
        int randomPrerequisites = Arbitraries.integers().between(2, numRequisites).sample();
        prerequisites[randomPrerequisites - 1][1] = prerequisites[randomPrerequisites - 1][0];

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                CourseSchedule.canFinish(numCourses, prerequisites));

        assertEquals("Courses can't reference themselves.", exception.getMessage());
    }

    @Property
    void invalidPrerequisite(
            @ForAll @IntRange(min = 11, max = 100) int numCourses,
            @ForAll @IntRange(min = 2, max = 100) int numRequisites
    ) {
        int[][] prerequisites = new int[numRequisites][2];
        for (int i = 0; i < numRequisites; i++) {
            prerequisites[i][0] = Arbitraries.integers().between(0, numCourses / 2 - 1).sample();
            prerequisites[i][1] = Arbitraries.integers().between(numCourses / 2, numCourses - 1).sample();
        }

        // Change the last tuple so it references itself.
        int randomPrerequisites = Arbitraries.integers().between(2, numRequisites).sample();
        int randomColumn = Arbitraries.integers().between(0, 1).sample();
        int randomOutOfBounds = Arbitraries.integers().between(numCourses + 1, Integer.MAX_VALUE).sample();
        prerequisites[randomPrerequisites - 1][randomColumn] = randomOutOfBounds;

        ArrayIndexOutOfBoundsException exception = assertThrows(ArrayIndexOutOfBoundsException.class, () ->
                CourseSchedule.canFinish(numCourses, prerequisites));

        assertEquals("Prerequisites references non-existing course.", exception.getMessage());
    }

    @Property
    void negativePrerequisite(
            @ForAll @IntRange(min = 11, max = 100) int numCourses,
            @ForAll @IntRange(min = 2, max = 100) int numRequisites
    ) {
        int[][] prerequisites = new int[numRequisites][2];
        for (int i = 0; i < numRequisites; i++) {
            prerequisites[i][0] = Arbitraries.integers().between(0, numCourses / 2 - 1).sample();
            prerequisites[i][1] = Arbitraries.integers().between(numCourses / 2, numCourses - 1).sample();
        }

        // Change the last tuple so it references itself.
        int randomPrerequisites = Arbitraries.integers().between(2, numRequisites).sample();
        int randomColumn = Arbitraries.integers().between(0, 1).sample();
        int randomOutOfBounds = Arbitraries.integers().between(Integer.MIN_VALUE, -1).sample();
        prerequisites[randomPrerequisites - 1][randomColumn] = randomOutOfBounds;

        ArrayIndexOutOfBoundsException exception = assertThrows(ArrayIndexOutOfBoundsException.class, () ->
                CourseSchedule.canFinish(numCourses, prerequisites));

        assertEquals("Prerequisites references non-existing course.", exception.getMessage());
    }
}
