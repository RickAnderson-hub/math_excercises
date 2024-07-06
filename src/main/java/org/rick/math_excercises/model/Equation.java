package org.rick.math_excercises.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is a model for an equation.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Equation {

    private int firstNumber;
    private int secondNumber;
    private int result;
    private char operator;
}
