package zest;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GenerateParenthesesTest {

    @Test
    void inputNegative() {
        List<String> expected = new ArrayList<>();
        assertEquals(expected, GenerateParentheses.generateParentheses(-1));
    }

    @Test
    void inputZero() {
        List<String> expected = new ArrayList<>();
        assertEquals(expected, GenerateParentheses.generateParentheses(0));
    }

    @Test
    void inputOne() {
        List<String> expected = new ArrayList<>();
        expected.add("()");
        assertEquals(expected, GenerateParentheses.generateParentheses(1));
    }

    @Test
    void inputThree() {
        List<String> expected = new ArrayList<>();
        Collections.addAll(expected, "((()))", "(()())", "(())()", "()(())", "()()()");
        List<String> result = GenerateParentheses.generateParentheses(3);
        assertTrue(expected.size() == result.size() && expected.containsAll(result) && result.containsAll(expected));
    }

    @Test
    void inputEight() {
        assertEquals(1430, GenerateParentheses.generateParentheses(8).size());
    }

    @Test
    void inputNine() {
        List<String> expected = new ArrayList<>();
        assertEquals(expected, GenerateParentheses.generateParentheses(9));
    }
}