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

/**
 * Immutable model representing a single arithmetic equation to be rendered in the PDF.
 *
 * <p>An equation consists of two operands (firstNumber and secondNumber), an operator (one of '+',
 * '-', '×', '÷'), and the expected result.
 *
 * @param firstNumber  The first operand in the equation (left-hand side).
 * @param secondNumber The second operand in the equation (right-hand side).
 * @param result       The computed result of applying the operator to firstNumber and secondNumber.
 * @param operator     The operator character representing the arithmetic operation ('+', '-', '×', '÷').
 */
public record Equation(int firstNumber, int secondNumber, int result, char operator) {

	/**
	 * Static factory method for creating an Equation.
	 *
	 * @param firstNumber  the first operand
	 * @param secondNumber the second operand
	 * @param result       the computed result
	 * @param operator     the operator character
	 * @return a new Equation instance
	 */
	public static Equation of(int firstNumber, int secondNumber, int result, char operator) {
		return new Equation(firstNumber, secondNumber, result, operator);
	}
}
