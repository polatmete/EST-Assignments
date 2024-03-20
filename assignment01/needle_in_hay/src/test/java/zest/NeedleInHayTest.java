package zest;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NeedleInHayTest {
    @Test
    void correctAnswer() {
        assertEquals(0, NeedleInHay.find("needleinhaystack", "needle"));
        assertEquals(2, NeedleInHay.find("inneedlehaystack", "needle"));
        assertEquals(10, NeedleInHay.find("haystackinneedle", "needle"));
    }

    @Test
    void stringNotFound() {
        assertEquals(-1, NeedleInHay.find("haystack", "needle"));
    }

    @Test
    void haystackNull() {
        assertEquals(-1, NeedleInHay.find(null, "needle"));
    }

    @Test
    void needleNull() {
        assertEquals(-1, NeedleInHay.find("haystack", null));
    }

    @Test
    void bothNull() {
        assertEquals(-1, NeedleInHay.find(null, null));
    }

    @Test
    void bothEmpty() {
        assertEquals(0, NeedleInHay.find("", ""));
    }

    @Test
    void haystackEmpty() {
        assertEquals(-1, NeedleInHay.find("", "needle"));
    }

    @Test
    void needleEmpty() {
        assertEquals(-1, NeedleInHay.find("haystack", ""));
    }
}