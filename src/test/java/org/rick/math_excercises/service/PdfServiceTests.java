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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.rick.math_excercises.model.Equation;

import java.io.File;
import java.util.List;
import java.util.Random;

class PdfServiceTests {

    private PdfService pdfService;

    @BeforeEach
    void setUp() {
        pdfService = new PdfService(new Random(42));
    }

    @Test
    @DisplayName("Generate PDF with multiple equations successfully")
    void generatePdfWithMultipleEquations() {
        // Given
        List<Equation> equations = List.of(
                Equation.builder().firstNumber(1).secondNumber(1).result(2).operator('+').build(),
                Equation.builder().firstNumber(3).secondNumber(1).result(2).operator('-').build()
        );
        // When
        pdfService.generatePdf(equations, 1);
        // Then
        File file = new File("MathExercises_1.pdf");
        assert file.exists();
        file.delete(); // Clean up
    }

    @Test
    @DisplayName("Generate PDF with no equations")
    void generatePdfWithNoEquations() {
        // Given
        List<Equation> emptyList = List.of();
        // Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> pdfService.generatePdf(emptyList, 1));
    }
}