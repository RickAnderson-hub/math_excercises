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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rick.math_excercises.model.Equation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

	/**
	 * Verifies only ADDITION equations are produced with correct sums and constraints.
	 */
	@Test
	void generatesOnlyAdditionWhenRequested() {
		int limit = 20;
		List<Equation> equations =
				generateService.generateExercises(limit, 40, List.of(Operations.ADDITION));
		assertEquals(40, equations.size());
		for (Equation eq : equations) {
			assertEquals('+', eq.operator());
			assertTrue(eq.firstNumber() >= 0);
			assertTrue(eq.secondNumber() >= 0);
			assertTrue(eq.result() <= limit);
			assertEquals(eq.firstNumber() + eq.secondNumber(), eq.result());
			assertFalse(eq.firstNumber() == 0 && eq.secondNumber() == 0);
		}
	}

	/**
	 * Verifies only SUBTRACTION equations are produced with non-negative results.
	 */
	@Test
	void generatesOnlySubtractionWhenRequested() {
		int limit = 20;
		List<Equation> equations =
				generateService.generateExercises(limit, 40, List.of(Operations.SUBTRACTION));
		assertEquals(40, equations.size());
		for (Equation eq : equations) {
			assertEquals('-', eq.operator());
			assertTrue(eq.firstNumber() >= 0);
			assertTrue(eq.secondNumber() >= 0);
			assertTrue(eq.result() >= 0);
			assertEquals(eq.firstNumber() - eq.secondNumber(), eq.result());
			assertFalse(eq.firstNumber() == 0 && eq.secondNumber() == 0);
		}
	}
}
