package zest;

import java.util.ArrayList;
import java.util.List;

public class CourseSchedule {
    public static boolean canFinish(int numCourses, int[][] prerequisites) {

        if (prerequisites == null) {
            throw new NullPointerException("Prerequisites are null.");
        }

        if (prerequisites.length == 0) {
            return true;
        }

        if (numCourses < 1) {
            throw new IllegalArgumentException("Number of courses must be higher than 0.");
        }

        // Create a graph from prerequisites
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] prerequisite : prerequisites) {
            if (prerequisite[0] == prerequisite[1]) {
                throw new IllegalArgumentException("Courses can't reference themselves.");
            }

            if (prerequisite[0] >= numCourses || prerequisite[1] >= numCourses || prerequisite[0] < 0 || prerequisite[1] < 0) {
                throw new ArrayIndexOutOfBoundsException("Prerequisites references non-existing course.");
            }

            graph.get(prerequisite[1]).add(prerequisite[0]);
        }

        boolean[] visited = new boolean[numCourses];
        boolean[] onPath = new boolean[numCourses];

        for (int i = 0; i < numCourses; i++) {
            if (!visited[i] && hasCycle(graph, i, visited, onPath)) {
                return false; // Cycle detected
            }
        }
        return true; // No cycle detected
    }

    private static boolean hasCycle(List<List<Integer>> graph, int current, boolean[] visited, boolean[] onPath) {
        if (onPath[current]) return true; // Cycle detected
        if (visited[current]) return false; // Already visited

        visited[current] = true;
        onPath[current] = true;

        for (int neighbor : graph.get(current)) {
            if (hasCycle(graph, neighbor, visited, onPath)) {
                return true;
            }
        }

        onPath[current] = false; // Backtrack
        return false;
    }
}
