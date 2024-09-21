package org.rick.math_excercises.service;

import org.rick.math_excercises.model.Equation;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for generating math exercises.
 */
public class GenerateService {

    /**
     * Generates math exercises.  A limit is passed to determine the upper limit of the exercises.
     * A subtraction or addition exercise is generated based on the limit.
     *
     * @param limit The upper limit of the moth exercises.
     */
    public List<Equation> generateExercises(int limit, int numberOfExercises) {
        List<Equation> equations = new ArrayList<>();
        for (int i = 0; i < numberOfExercises; i++) {
            Equation equation = generateEquation(limit);
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
     * Generates a single {@link Equation}.
     *
     * @param limit The upper limit of the exercise.  Must be 10 or greater.
     * @return The equation.
     */
    private Equation generateEquation(int limit) {
        if (limit < 10) {
            throw new IllegalArgumentException("Limit must be greater than or equal to 10.");
        }
        char operator = Math.random() > 0.5 ? '+' : '-';
        int firstNumber = (int) (Math.random() * limit);
        int secondNumber = (int) (Math.random() * limit);
        boolean isBothZero = firstNumber == 0 && secondNumber == 0;
        boolean resultIsNegative = isResultIsNegative(firstNumber, secondNumber, operator);
        while (calculate(firstNumber, secondNumber, operator) > limit || isBothZero || resultIsNegative) {
            firstNumber = (int) (Math.random() * limit);
            secondNumber = (int) (Math.random() * limit);
            resultIsNegative = isResultIsNegative(firstNumber, secondNumber, operator);
            isBothZero = firstNumber == 0 && secondNumber == 0;
        }
        return Equation.builder()
                .firstNumber(firstNumber)
                .secondNumber(secondNumber)
                .result(calculate(firstNumber, secondNumber, operator))
                .operator(operator)
                .build();
    }

    private boolean isResultIsNegative(int firstNumber, int secondNumber, char operator) {
        return calculate(firstNumber, secondNumber, operator) < 0;
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
