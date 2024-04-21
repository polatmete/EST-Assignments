package zest;


import net.jqwik.api.*;
import net.jqwik.api.constraints.IntRange;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CourseScheduleTest {

    @Test
    public void withoutCircles() {
        int[][] input = {{1, 0}};
        assertEquals(true, CourseSchedule.canFinish(2, input));
    }

    @Test
    public void withCircles() {
        int[][] input = {{1, 0}, {0,1}};
        assertEquals(false, CourseSchedule.canFinish(2, input));
    }

    @Test
    public void emptyPrerequisites() {
        int[][] input = {};
        assertEquals(true, CourseSchedule.canFinish(2, input));
    }

    @Test
    public void nullPrerequisites() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                CourseSchedule.canFinish(2, null));

        assertEquals("Prerequisites are null.", exception.getMessage());
    }

    @Test
    public void largeWithoutCircles() {
        int[][] input = {{1,0}, {4, 0}, {5, 1}, {3, 2}, {2, 5}, {3, 0}};
        assertEquals(true, CourseSchedule.canFinish(6, input));
    }

    @Test
    public void largeWithCircles() {
        int[][] input = {{1, 0}, {0, 4}, {4, 2}, {2, 3}, {3, 5}, {5, 1}};
        assertEquals(false, CourseSchedule.canFinish(6, input));
    }

    @Test
    public void invalidNumCourses() {
        int[][] input = {{1, 0}, {0,1}};
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                CourseSchedule.canFinish(0, input));

        assertEquals("Number of courses must be higher than 0.", exception.getMessage());
    }

    @Test
    public void circleTooSmall() {
        int[][] input = {{1, 0}, {0,0}};
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                CourseSchedule.canFinish(2, input));

        assertEquals("Courses can't reference themselves.", exception.getMessage());
    }

    @Test
    public void invalidPrerequisite() {
        int[][] input = {{1, 0}, {2,0}};
        ArrayIndexOutOfBoundsException exception = assertThrows(ArrayIndexOutOfBoundsException.class, () ->
                CourseSchedule.canFinish(2, input));

        assertEquals("Prerequisites references non-existing course.", exception.getMessage());
    }

    // This doesn't work yet. I just don't get it.
    @Property
    void validRangeWithoutCircles(
            @ForAll @IntRange(min = 1, max = 20) int numCourses,
            @ForAll @IntRange(min = 1, max = 20) int numRequisites
    ) {
        boolean selfReference = false;
        Arbitrary<int[][]> prerequisites = Arbitraries.randomValue(rnd -> {
            int[][] tuples = new int[numRequisites][2];
            for (int i = 0; i < numRequisites; i++) {
                tuples[i][0] = Arbitraries.integers().between(0, numCourses/2).sample(); // First element of tuple
                tuples[i][1] = Arbitraries.integers().between(numCourses/2, numCourses-1).sample(); // Second element of tuple
            }
            return tuples;
        });

        // Check if self reference exists
        for (int i = 0; i < prerequisites.sample().length; i++) {
            if (prerequisites.sample()[i][0] == prerequisites.sample()[i][1]) {
                selfReference = true;
            }
        }

        if (selfReference) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    CourseSchedule.canFinish(numCourses, prerequisites.sample()));

            assertEquals("Courses can't reference themselves.", exception.getMessage());
        } else {
            assertEquals(true, CourseSchedule.canFinish(numCourses, prerequisites.sample()));
        }
    }

}
