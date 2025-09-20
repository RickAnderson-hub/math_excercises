package org.rick.math_excercises.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rick.math_excercises.model.Equation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenerateServiceAddSubTests {

    private GenerateService generateService;

    @BeforeEach
    void setUp() {
        generateService = new GenerateService();
    }

    @Test
    void generatesOnlyAdditionWhenRequested() {
        int limit = 20;
        List<Equation> equations = generateService.generateExercises(limit, 40, List.of(Operations.ADDITION));
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

    @Test
    void generatesOnlySubtractionWhenRequested() {
        int limit = 20;
        List<Equation> equations = generateService.generateExercises(limit, 40, List.of(Operations.SUBTRACTION));
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

