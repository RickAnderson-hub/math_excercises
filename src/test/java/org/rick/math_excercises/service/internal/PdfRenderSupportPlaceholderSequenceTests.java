package org.rick.math_excercises.service.internal;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PdfRenderSupportPlaceholderSequenceTests {

    @Test
    void sequenceDeterministicForSeed() {
        Random r1 = new Random(1234);
        Random r2 = new Random(1234);
        List<Integer> s1 = PdfRenderSupport.placeholderSequence(r1, 20);
        List<Integer> s2 = PdfRenderSupport.placeholderSequence(r2, 20);
        assertEquals(s1, s2, "Sequences with same seed should match");
        assertTrue(s1.stream().allMatch(i -> i>=1 && i<=3));
    }

    @Test
    void choosePlaceholderIndexRange() {
        Random r = new Random(42);
        for (int i = 0; i < 100; i++) {
            int v = PdfRenderSupport.choosePlaceholderIndex(r);
            assertTrue(v >= 1 && v <= 3, "Value out of range: " + v);
        }
    }
}

