/*
 * Math Exercises Generator — Shareware License
 * Copyright (c) 2025 Rick Anderson
 * Contact: rick@getanderson.net
 *
 * Personal, non-commercial use permitted. Redistribution allowed with attribution.
 * Any commercial use requires a paid license or prior written permission.
 * See the LICENSE file for full terms.
 */

package org.rick.math_excercises.service.internal;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

/**
 * Tests deterministic and range-correct behavior of placeholder index selection utilities in {@link
 * PdfRenderSupport}.
 */
class PdfRenderSupportPlaceholderSequenceTests {

  /**
   * Ensures two Random instances with the same seed produce identical placeholder index sequences.
   */
  @Test
  void sequenceDeterministicForSeed() {
    Random r1 = new Random(1234);
    Random r2 = new Random(1234);
    List<Integer> s1 = PdfRenderSupport.placeholderSequence(r1, 20);
    List<Integer> s2 = PdfRenderSupport.placeholderSequence(r2, 20);
    assertEquals(s1, s2, "Sequences with same seed should match");
    assertTrue(s1.stream().allMatch(i -> i >= 1 && i <= 3));
  }

  /** Validates every generated placeholder index falls within the inclusive range [1, 3]. */
  @Test
  void choosePlaceholderIndexRange() {
    Random r = new Random(42);
    for (int i = 0; i < 100; i++) {
      int v = PdfRenderSupport.choosePlaceholderIndex(r);
      assertTrue(v >= 1 && v <= 3, "Value out of range: " + v);
    }
  }
}
