package org.rick.math_excercises.service;

import org.rick.math_excercises.model.Equation;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This class is responsible for generating math exercises.
 */
public class GenerateService {

    private static final Random RANDOM = new Random();

    /**
     * Generates math exercises. A limit is passed to determine the upper limit of the exercises.
     * A subtraction or addition exercise is generated based on the limit.
     *
     * @param limit The upper limit of the math exercises.
     * @param numberOfExercises The number of exercises to generate.
     * @return A list of generated equations.
     */
    public List<Equation> generateExercises(int limit, int numberOfExercises) {
        return IntStream.range(0, numberOfExercises)
                .mapToObj(i -> generateEquation(limit))
                .collect(Collectors.toList());
    }

    /**
     * Generates a single {@link Equation}.
     *
     * @param limit The upper limit of the exercise. Must be 10 or greater.
     * @return The equation.
     */
    private Equation generateEquation(int limit) {
        if (limit < 10) {
            throw new IllegalArgumentException("Limit must be greater than or equal to 10.");
        }
        char operator = RANDOM.nextBoolean() ? '+' : '-';
        int firstNumber, secondNumber;
        do {
            firstNumber = RANDOM.nextInt(limit);
            secondNumber = RANDOM.nextInt(limit);
        } while (isInvalidEquation(firstNumber, secondNumber, operator, limit));
        return Equation.builder()
                .firstNumber(firstNumber)
                .secondNumber(secondNumber)
                .result(calculate(firstNumber, secondNumber, operator))
                .operator(operator)
                .build();
    }

    private boolean isInvalidEquation(int firstNumber, int secondNumber, char operator, int limit) {
        return firstNumber == 0 && secondNumber == 0 ||
                calculate(firstNumber, secondNumber, operator) > limit ||
                calculate(firstNumber, secondNumber, operator) < 0;
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
        return operator == '+' ? firstNumber + secondNumber : firstNumber - secondNumber;
    }
}