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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.rick.math_excercises.model.Equation;

/**
 * Verifies that {@link PdfService} can render a large list of equations across multiple columns on
 * a single page and produce the expected output file when base name and suffix overrides are used.
 */
class PdfServiceMultiColumnTests {

  @AfterEach
  void cleanup() {
    new File("Columns_99_Multi.pdf").delete();
    System.clearProperty("outputBaseName");
    System.clearProperty("outputSuffix");
  }

  /**
   * Generates 120 equations (60 addition, 60 division) and asserts the resulting multi-column PDF
   * file exists.
   */
  @Test
  void rendersMultipleColumnsForManyEquations() {
    PdfService pdfService = new PdfService();
    List<Equation> equations = new ArrayList<>();
    // 120 equations -> 3 columns (50 per column)
    for (int i = 0; i < 60; i++) {
      equations.add(
          Equation.builder().firstNumber(5).secondNumber(3).result(8).operator('+').build());
    }
    for (int i = 0; i < 60; i++) {
      equations.add(
          Equation.builder().firstNumber(9).secondNumber(3).result(3).operator('÷').build());
    }
    System.setProperty("outputBaseName", "Columns");
    System.setProperty("outputSuffix", "_Multi");

    pdfService.generatePdf(equations, 99);

    File out = new File("Columns_99_Multi.pdf");
    assertTrue(out.exists());
    out.delete();
  }
}
