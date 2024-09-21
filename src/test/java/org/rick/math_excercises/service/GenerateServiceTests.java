package org.rick.math_excercises.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rick.math_excercises.model.Equation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenerateServiceTests {

    private GenerateService generateService;

    @BeforeEach
    void setUp() {
        generateService = new GenerateService();
    }

    @Test
    void generatesCorrectNumberOfExercises() {
        int limit = 10;
        List<Equation> equations = generateService.generateExercises(limit, 20);
        assertEquals(20, equations.size());
    }

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

    @Test
    void generatesExercisesWithValidOperators() {
        int limit = 20;
        List<Equation> equations = generateService.generateExercises(limit, 20);
        equations.forEach(equation -> assertTrue(equation.getOperator() == '+' || equation.getOperator() == '-'));
    }

    @Test
    void generatesPositiveResultsForAddition() {
        int limit = 20;
        List<Equation> equations = generateService.generateExercises(limit, 30);
        equations.stream().filter(equation -> equation.getOperator() == '+')
                .forEach(equation -> assertTrue(equation.getResult() >= 1));
    }

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

    @Test
    void handlesLowerLimit() {
        int limit = 9;
        assertThrowsExactly(IllegalArgumentException.class, () -> generateService.generateExercises(limit, 1));
    }
}