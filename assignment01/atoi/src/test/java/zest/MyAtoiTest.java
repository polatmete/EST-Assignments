package zest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MyAtoiTest {


    @Test
    void happyCase() {
        String input = "42";
        int expectedOutput = 42;
        int result = MyAtoi.myAtoi(input);
        assertEquals(expectedOutput, result);
    }

    @Test
    void inputIsNull() {
        String input = null;
        int expectedOutput = 0;
        int result = MyAtoi.myAtoi(input);
        assertEquals(expectedOutput, result);
    }

    @Test
    void inputIsCharOnly() {
        String input = "abc";
        int expectedOutput = 0;
        int result = MyAtoi.myAtoi(input);
        assertEquals(expectedOutput, result);
    }

    @Test
    void positiveInput() {
        assertEquals(24, MyAtoi.myAtoi("24"));
        assertEquals(24, MyAtoi.myAtoi("+24"));
    }

    @Test
    void negativeInput() {
        assertEquals(-24, MyAtoi.myAtoi("-24"));
    }

    @Test
    void leadingWhitespaces() {
        assertEquals(24, MyAtoi.myAtoi(" 24"));
        assertEquals(24, MyAtoi.myAtoi("     24"));
        assertEquals(-24, MyAtoi.myAtoi("   -24"));
    }

    @Test
    void leadingZeros() {
        assertEquals(24, MyAtoi.myAtoi("024"));
        assertEquals(24, MyAtoi.myAtoi("000024"));
        assertEquals(24, MyAtoi.myAtoi("+000024"));
        assertEquals(-24, MyAtoi.myAtoi("-000024"));
    }

    @Test
    void withChars() {
        assertEquals(24, MyAtoi.myAtoi("24 with words"));
        assertEquals(0, MyAtoi.myAtoi("with words 24"));
        assertEquals(24, MyAtoi.myAtoi("24 plus 46"));
    }

    @Test
    void outOfRange() {
        // on points
        assertEquals(-2147483648, MyAtoi.myAtoi("-2147483648"));
        assertEquals(2147483647, MyAtoi.myAtoi("2147483647"));
        // off points (close to boundary)
        assertEquals(-2147483647, MyAtoi.myAtoi("-2147483647"));
        assertEquals(2147483646, MyAtoi.myAtoi("2147483646"));
        // off points (boundary overflow)
        assertEquals(-2147483648, MyAtoi.myAtoi("-91283472332"));
        assertEquals(2147483647, MyAtoi.myAtoi("91283472332"));
    }

    @Test
    void stringIsEmpty(){
        assertEquals(0, MyAtoi.myAtoi(""));
    }
}
