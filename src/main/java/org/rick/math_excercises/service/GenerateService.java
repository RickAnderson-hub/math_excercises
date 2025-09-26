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
     * @return list of generated equations using ADDITION and SUBTRACTION
     */
    public List<Equation> generateExercises(int limit, int numberOfExercises) {
        return generateExercises(limit, numberOfExercises, List.of(Operations.ADDITION, Operations.SUBTRACTION));
    }

    /**
     * Generates math exercises using the provided set of operations.
     * If operations is null or empty, defaults to ADDITION and SUBTRACTION.
     * <p>
     * Constraints enforced by the generator:
     * <ul>
     *   <li>limit must be >= 10</li>
     *   <li>numberOfExercises must be >= 1</li>
     *   <li>operands and results are non-negative and within the provided limit</li>
     *   <li>division avoids zero divisors; multiplication avoids zero factors</li>
     * </ul>
     *
     * @param limit The upper limit of the math exercises (must be >= 10)
     * @param numberOfExercises how many equations to generate (must be >= 1)
     * @param operations collection of allowed operations; null/empty uses ADDITION and SUBTRACTION
     * @return list of generated equations
     * @throws IllegalArgumentException if limit {@code <} 10 or numberOfExercises {@code <} 1
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

    /**
     * Generates a subtraction equation where the result is non-negative and within the limit.
     * Both operands are within [0, limit], but not simultaneously zero.
     *
     * @param limit upper bound for operands and results (exclusive for random generation)
     * @return an {@link Equation} representing subtraction
     */
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

    /**
     * Generates an addition equation where the sum does not exceed the limit and not both operands are zero.
     *
     * @param limit upper bound for operands and results (exclusive for random generation)
     * @return an {@link Equation} representing addition
     */
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

    /**
     * Generates a division equation with integer quotient and no zero divisors.
     * The product (dividend) remains within the given limit.
     *
     * @param limit upper bound for operands and derived dividend
     * @return an {@link Equation} representing division (firstNumber ÷ secondNumber = result)
     */
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
                .operator('÷')
                .build();
    }

    /**
     * Generates a multiplication equation avoiding zero operands and ensuring the product is within the limit.
     *
     * @param limit upper bound for operands and results (exclusive for random generation)
     * @return an {@link Equation} representing multiplication
     */
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
                .operator('×')
                .build();
    }

    /**
     * Randomly generates either an addition or subtraction equation using the limit.
     *
     * @param limit upper bound for operands and results
     * @return an {@link Equation} representing addition or subtraction
     */
    private Equation generateSumOrDifferenceEquation(int limit) {
        if (ThreadLocalRandom.current().nextBoolean()) {
            return generateAdditionEquation(limit);
        } else {
            return generateSubtractionEquation(limit);
        }
    }

    /**
     * Picks a random operation from the provided set and generates an equation accordingly.
     * Optimized fast-path for the common case of ADDITION+SUBTRACTION.
     *
     * @param limit upper bound for operands and results
     * @param ops non-empty list of allowed operations
     * @return an {@link Equation} matching one of the allowed operations
     */
    private Equation generateEquationForOperations(int limit, List<Operations> ops) {
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
}
