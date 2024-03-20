package zest;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


class Frac2DecTest {

    @Test
    void correctAnswerPositive() {
        assertEquals("0.5", Frac2Dec.fractionToDecimal(1, 2));
        assertEquals("2", Frac2Dec.fractionToDecimal(2, 1));
        assertEquals("0.(012)", Frac2Dec.fractionToDecimal(4, 333));
        assertEquals("0.1(6)", Frac2Dec.fractionToDecimal(1, 6));
        assertEquals("0.1(6)", Frac2Dec.fractionToDecimal(-1, -6));
    }

    @Test
    void correctAnswerNegative() {
        assertEquals("-0.5", Frac2Dec.fractionToDecimal(-1, 2));
        assertEquals("-2", Frac2Dec.fractionToDecimal(2, -1));
        assertEquals("-0.(012)", Frac2Dec.fractionToDecimal(-4, 333));
        assertEquals("-0.1(6)", Frac2Dec.fractionToDecimal(1, -6));
    }

    @Test
    void zeroDominator() {
        assertNull(Frac2Dec.fractionToDecimal(1, 0));
        assertNull(Frac2Dec.fractionToDecimal(0, 0));
    }

    @Test
    void zeroNumerator() {
        assertEquals("0", Frac2Dec.fractionToDecimal(0, 1));
    }
}