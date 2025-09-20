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

import org.rick.math_excercises.model.Equation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class is responsible for generating math exercises.
 */
public class GenerateService {

    /**
     * Generates math exercises using addition and subtraction by default.
     * A limit is passed to determine the upper limit of the exercises.
     *
     * @param limit The upper limit of the math exercises.
     * @param numberOfExercises how many equations to generate
     */
    public List<Equation> generateExercises(int limit, int numberOfExercises) {
        // default to ADDITION and SUBTRACTION to preserve existing behavior/tests
        return generateExercises(limit, numberOfExercises, List.of(Operations.ADDITION, Operations.SUBTRACTION));
    }

    /**
     * Generates math exercises using the provided set of operations.
     * If operations is null or empty, defaults to ADDITION and SUBTRACTION.
     *
     * @param limit The upper limit of the math exercises (must be >= 10)
     * @param numberOfExercises how many equations to generate
     * @param operations collection of allowed operations
     * @return list of generated equations
     */
    public List<Equation> generateExercises(int limit, int numberOfExercises, Collection<Operations> operations) {
        if (limit < 10) {
            throw new IllegalArgumentException("Limit must be greater than or equal to 10.");
        }
        if (numberOfExercises < 1) {
            throw new IllegalArgumentException("numberOfExercises must be >= 1");
        }
        // sanitize operations
        List<Operations> ops;
        if (operations == null || operations.isEmpty()) {
            ops = List.of(Operations.ADDITION, Operations.SUBTRACTION);
        } else {
            ops = new ArrayList<>();
            for (Operations op : operations) {
                if (op != null) ops.add(op);
            }
            if (ops.isEmpty()) {
                ops = List.of(Operations.ADDITION, Operations.SUBTRACTION);
            }
        }

        List<Equation> equations = new ArrayList<>(numberOfExercises);
        for (int i = 0; i < numberOfExercises; i++) {
            Equation equation = generateEquationForOperations(limit, ops);
            equations.add(Equation.builder()
                    .firstNumber(equation.getFirstNumber())
                    .secondNumber(equation.getSecondNumber())
                    .result(equation.getResult())
                    .operator(equation.getOperator())
                    .build());
        }
        return equations;
    }

    // --- Specific equation generators ---

    private Equation generateSubtractionEquation(int limit) {
        int firstNumber = (int) (Math.random() * limit);
        int secondNumber = (int) (Math.random() * limit);
        while (firstNumber - secondNumber < 0 || (firstNumber == 0 && secondNumber == 0)) {
            firstNumber = (int) (Math.random() * limit);
            secondNumber = (int) (Math.random() * limit);
        }
        return Equation.builder()
                .firstNumber(firstNumber)
                .secondNumber(secondNumber)
                .result(firstNumber - secondNumber)
                .operator('-')
                .build();
    }

    private Equation generateAdditionEquation(int limit) {
        int firstNumber = (int) (Math.random() * limit);
        int secondNumber = (int) (Math.random() * limit);
        while (firstNumber + secondNumber > limit || (firstNumber == 0 && secondNumber == 0)) {
            firstNumber = (int) (Math.random() * limit);
            secondNumber = (int) (Math.random() * limit);
        }
        return Equation.builder()
                .firstNumber(firstNumber)
                .secondNumber(secondNumber)
                .result(firstNumber + secondNumber)
                .operator('+')
                .build();
    }

    private Equation generateDivisionEquation(int limit) {
        int secondNumber = (int) (Math.random() * (limit - 1)) + 1; // Avoid zero
        int result = (int) (Math.random() * limit);
        int firstNumber = secondNumber * result;
        while (firstNumber > limit) {
            secondNumber = (int) (Math.random() * (limit - 1)) + 1; // Avoid zero
            result = (int) (Math.random() * limit);
            firstNumber = secondNumber * result;
        }
        return Equation.builder()
                .firstNumber(firstNumber)
                .secondNumber(secondNumber)
                .result(result)
                .operator('/')
                .build();
    }

    private Equation generateMultiplicationEquation(int limit) {
        int firstNumber = (int) (Math.random() * limit);
        int secondNumber = (int) (Math.random() * limit);
        while (firstNumber == 0 || secondNumber == 0 || firstNumber * secondNumber > limit) {
            firstNumber = (int) (Math.random() * limit);
            secondNumber = (int) (Math.random() * limit);
        }
        return Equation.builder()
                .firstNumber(firstNumber)
                .secondNumber(secondNumber)
                .result(firstNumber * secondNumber)
                .operator('*')
                .build();
    }

    // --- Selection helpers ---

    private Equation generateSumOrDifferenceEquation(int limit) {
        // 0 -> addition, 1 -> subtraction
        if (ThreadLocalRandom.current().nextBoolean()) {
            return generateAdditionEquation(limit);
        } else {
            return generateSubtractionEquation(limit);
        }
    }

    private Equation generateEquationForOperations(int limit, List<Operations> ops) {
        // Fast path for common case of add/sub only
        if (ops.size() == 2 && ops.contains(Operations.ADDITION) && ops.contains(Operations.SUBTRACTION)) {
            return generateSumOrDifferenceEquation(limit);
        }
        int index = ThreadLocalRandom.current().nextInt(ops.size());
        Operations op = ops.get(index);
        return switch (op) {
            case ADDITION -> generateAdditionEquation(limit);
            case SUBTRACTION -> generateSubtractionEquation(limit);
            case MULTIPLICATION -> generateMultiplicationEquation(limit);
            case DIVISION -> generateDivisionEquation(limit);
        };
    }

    /**
     * This function does add and subtract operations.
     *
     * @param firstNumber  The first number.
     * @param secondNumber The second number.
     * @param operator     The operator (plus or minus).
     * @return The result of the operation that is less than the limit supplied.
     */
    private int calculate(int firstNumber, int secondNumber, char operator) {
        if (operator == '+') {
            return firstNumber + secondNumber;
        } else {
            return firstNumber - secondNumber;
        }
    }
}
