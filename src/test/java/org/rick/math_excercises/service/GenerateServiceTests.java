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
 * Tests covering default (addition & subtraction) generation paths in {@link GenerateService}.
 * Focus areas:
 * <ul>
 *   <li>Requested count honored.</li>
 *   <li>Operand/result bounds respected.</li>
 *   <li>Operator set restricted to + / - in default mode.</li>
 *   <li>No zero+zero equations; non-negative invariants enforced.</li>
 *   <li>Validation for lower-than-minimum limit.</li>
 * </ul>
 */
class GenerateServiceTests {

    private GenerateService generateService;

    @BeforeEach
    void setUp() {
        generateService = new GenerateService();
    }

    /** Ensures numberOfExercises parameter yields exactly that many equations. */
    @Test
    void generatesCorrectNumberOfExercises() {
        int limit = 10;
        List<Equation> equations = generateService.generateExercises(limit, 20);
        assertEquals(20, equations.size());
    }

    /** Verifies all operands and results are within the configured limit. */
    @Test
    void generatesExercisesWithinLimit() {
        int limit = 10;
        List<Equation> equations = generateService.generateExercises(limit, 20);
        equations.forEach(equation -> {
            assertTrue(equation.getResult() <= limit);
            assertTrue(equation.getFirstNumber() <= limit);
            assertTrue(equation.getSecondNumber() <= limit);
        });
    }

    /** Confirms only '+' or '-' operators are produced under default operations. */
    @Test
    void generatesExercisesWithValidOperators() {
        int limit = 20;
        List<Equation> equations = generateService.generateExercises(limit, 20);
        equations.forEach(equation -> assertTrue(equation.getOperator() == '+' || equation.getOperator() == '-'));
    }

    /** Checks that addition results are positive (excludes 0 + 0). */
    @Test
    void generatesPositiveResultsForAddition() {
        int limit = 20;
        List<Equation> equations = generateService.generateExercises(limit, 30);
        equations.stream().filter(equation -> equation.getOperator() == '+')
                .forEach(equation -> assertTrue(equation.getResult() >= 1));
    }

    /** Validates operands/results are non-negative and not both zero simultaneously. */
    @Test
    void generatesNonNegativeNumbers() {
        int limit = 10;
        List<Equation> equations = generateService.generateExercises(limit, 30);
        equations.forEach(equation -> {
            assertTrue(equation.getFirstNumber() >= 0);
            assertTrue(equation.getSecondNumber() >= 0);
            assertFalse(equation.getFirstNumber() == 0 && equation.getSecondNumber() == 0);
            assertTrue(equation.getResult() >= 0);
        });
    }

    /** Asserts that a limit below the enforced minimum triggers an exception. */
    @Test
    void handlesLowerLimit() {
        int limit = 9;
        assertThrowsExactly(IllegalArgumentException.class, () -> generateService.generateExercises(limit, 1));
    }
}