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
import org.junit.jupiter.api.Test;
import org.rick.math_excercises.model.Equation;

/**
 * Unit tests for {@link PdfRenderSupport} covering list partitioning and token formatting logic
 * including placeholder substitution and operator detection.
 */
class PdfRenderSupportTests {

  /** Ensures partitioning an empty list yields an empty result collection. */
  @Test
  void partitionEmpty() {
    assertTrue(PdfRenderSupport.partition(List.of(), 10).isEmpty());
  }

  /** Verifies exact division of list size by chunk size produces evenly sized partitions. */
  @Test
  void partitionExact() {
    List<Integer> src = List.of(1, 2, 3, 4, 5, 6);
    var parts = PdfRenderSupport.partition(src, 3);
    assertEquals(2, parts.size());
    assertEquals(List.of(1, 2, 3), parts.get(0));
    assertEquals(List.of(4, 5, 6), parts.get(1));
  }

  /** Confirms remainder elements form a final smaller partition. */
  @Test
  void partitionRemainder() {
    List<Integer> src = List.of(1, 2, 3, 4, 5);
    var parts = PdfRenderSupport.partition(src, 2);
    assertEquals(3, parts.size());
    assertEquals(List.of(1, 2), parts.get(0));
    assertEquals(List.of(3, 4), parts.get(1));
    assertEquals(List.of(5), parts.get(2));
  }

  /** Validates placeholder substitution for each of the three possible masked positions. */
  @Test
  void formatTokensEachPlaceholder() {
    Equation eq = Equation.builder().firstNumber(6).secondNumber(2).result(3).operator('÷').build();
    var t1 = PdfRenderSupport.formatTokens(eq, 1);
    var t2 = PdfRenderSupport.formatTokens(eq, 2);
    var t3 = PdfRenderSupport.formatTokens(eq, 3);
    assertEquals(List.of("□", "÷", "2", "=", "3"), t1);
    assertEquals(List.of("6", "÷", "□", "=", "3"), t2);
    assertEquals(List.of("6", "÷", "2", "=", "□"), t3);
  }

  /** Checks token count, operator placement, and operator detection classification. */
  @Test
  void formatTokensTokenCountAndOperators() {
    Equation eq =
        Equation.builder().firstNumber(5).secondNumber(7).result(12).operator('+').build();
    var tokens = PdfRenderSupport.formatTokens(eq, 2);
    assertEquals(5, tokens.size());
    assertEquals("+", tokens.get(1));
    assertEquals("=", tokens.get(3));
    assertTrue(PdfRenderSupport.isOperatorToken("+"));
    assertTrue(PdfRenderSupport.isOperatorToken("="));
    assertFalse(PdfRenderSupport.isOperatorToken("12"));
  }
}
