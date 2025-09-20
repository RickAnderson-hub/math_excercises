package org.rick.math_excercises.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rick.math_excercises.model.Equation;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenerateServiceOperationsTests {

    private GenerateService generateService;

    @BeforeEach
    void setUp() {
        generateService = new GenerateService();
    }

    @Test
    void generatesOnlyMultiplicationWhenRequested() {
        int limit = 12;
        List<Equation> equations = generateService.generateExercises(limit, 50, List.of(Operations.MULTIPLICATION));
        assertEquals(50, equations.size());
        for (Equation eq : equations) {
            assertEquals('*', eq.getOperator());
            assertTrue(eq.getFirstNumber() > 0);
            assertTrue(eq.getSecondNumber() > 0);
            assertTrue(eq.getResult() <= limit);
            assertEquals(eq.getFirstNumber() * eq.getSecondNumber(), eq.getResult());
        }
    }

    @Test
    void generatesOnlyDivisionWhenRequested() {
        int limit = 20;
        List<Equation> equations = generateService.generateExercises(limit, 50, List.of(Operations.DIVISION));
        assertEquals(50, equations.size());
        for (Equation eq : equations) {
            assertEquals('/', eq.getOperator());
            assertTrue(eq.getSecondNumber() > 0); // divisor not zero
            assertTrue(eq.getFirstNumber() <= limit);
            assertEquals(eq.getFirstNumber(), eq.getSecondNumber() * eq.getResult());
        }
    }

    @Test
    void defaultsToAddSubWhenOperationsNullOrEmptyOrNullEntries() {
        int limit = 15;
        List<Equation> a = generateService.generateExercises(limit, 10, null);
        List<Equation> b = generateService.generateExercises(limit, 10, List.of());
        List<Equation> c = generateService.generateExercises(limit, 10, Arrays.asList(null, null));
        for (Equation eq : a) assertTrue(eq.getOperator() == '+' || eq.getOperator() == '-');
        for (Equation eq : b) assertTrue(eq.getOperator() == '+' || eq.getOperator() == '-');
        for (Equation eq : c) assertTrue(eq.getOperator() == '+' || eq.getOperator() == '-');
    }

    @Test
    void throwsOnInvalidNumberOfExercises() {
        assertThrows(IllegalArgumentException.class, () -> generateService.generateExercises(10, 0));
        assertThrows(IllegalArgumentException.class, () -> generateService.generateExercises(10, 0, List.of(Operations.ADDITION)));
    }
}

