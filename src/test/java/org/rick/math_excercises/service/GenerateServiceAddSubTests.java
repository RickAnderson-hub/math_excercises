/*
 * Math Exercises Generator — Shareware License
 * Copyright (c) 2025 Rick Anderson
 * Contact: rick@getanderson.net
 *
 * Personal, non-commercial use permitted. Redistribution allowed with attribution.
 * Any commercial use requires a paid license or prior written permission.
 * See the LICENSE file for full terms.
 */

package org.rick.math_excercises.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rick.math_excercises.model.Equation;

/**
 * Tests for explicitly constrained ADDITION vs SUBTRACTION generation ensuring only the requested
 * operator is produced and arithmetic invariants hold (no zero+zero, non-negative results, bounds
 * respected).
 */
class GenerateServiceAddSubTests {

  private GenerateService generateService;

  @BeforeEach
  void setUp() {
    generateService = new GenerateService();
  }

  /** Verifies only ADDITION equations are produced with correct sums and constraints. */
  @Test
  void generatesOnlyAdditionWhenRequested() {
    int limit = 20;
    List<Equation> equations =
        generateService.generateExercises(limit, 40, List.of(Operations.ADDITION));
    assertEquals(40, equations.size());
    for (Equation eq : equations) {
      assertEquals('+', eq.getOperator());
      assertTrue(eq.getFirstNumber() >= 0);
      assertTrue(eq.getSecondNumber() >= 0);
      assertTrue(eq.getResult() <= limit);
      assertEquals(eq.getFirstNumber() + eq.getSecondNumber(), eq.getResult());
      assertFalse(eq.getFirstNumber() == 0 && eq.getSecondNumber() == 0);
    }
  }

  /** Verifies only SUBTRACTION equations are produced with non-negative results. */
  @Test
  void generatesOnlySubtractionWhenRequested() {
    int limit = 20;
    List<Equation> equations =
        generateService.generateExercises(limit, 40, List.of(Operations.SUBTRACTION));
    assertEquals(40, equations.size());
    for (Equation eq : equations) {
      assertEquals('-', eq.getOperator());
      assertTrue(eq.getFirstNumber() >= 0);
      assertTrue(eq.getSecondNumber() >= 0);
      assertTrue(eq.getResult() >= 0);
      assertTrue(eq.getFirstNumber() - eq.getSecondNumber() == eq.getResult());
      assertFalse(eq.getFirstNumber() == 0 && eq.getSecondNumber() == 0);
    }
  }
}
