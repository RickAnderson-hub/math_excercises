/*
 * Math Exercises Generator — Shareware License
 * Copyright (c) 2025 Rick Anderson
 * Contact: rick@getanderson.net
 *
 * Personal, non-commercial use permitted. Redistribution allowed with attribution.
 * Any commercial use requires a paid license or prior written permission.
 * See the LICENSE file for full terms.
 */

package org.rick.math_excercises.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model representing a single arithmetic equation to be rendered in the PDF.
 * <p>
 * An equation consists of two operands (firstNumber and secondNumber), an
 * operator (one of '+', '-', '*', '/'), and the expected result.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Equation {

    /**
     * The first operand in the equation (left-hand side).
     */
    private int firstNumber;

    /**
     * The second operand in the equation (right-hand side).
     */
    private int secondNumber;

    /**
     * The computed result of applying {@link #operator} to {@link #firstNumber} and {@link #secondNumber}.
     */
    private int result;

    /**
     * The operator character representing the arithmetic operation ('+', '-', '*', '/').
     */
    private char operator;
}
