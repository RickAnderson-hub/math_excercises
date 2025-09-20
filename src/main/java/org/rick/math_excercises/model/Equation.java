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
