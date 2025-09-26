package org.rick.math_excercises.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.rick.math_excercises.model.Equation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PdfServiceMultiColumnTests {

    @AfterEach
    void cleanup() {
        new File("Columns_99_Multi.pdf").delete();
        System.clearProperty("outputBaseName");
        System.clearProperty("outputSuffix");
    }

    @Test
    void rendersMultipleColumnsForManyEquations() {
        PdfService pdfService = new PdfService();
        List<Equation> equations = new ArrayList<>();
        // 120 equations -> 3 columns (50 per column)
        for (int i = 0; i < 60; i++) {
            equations.add(Equation.builder().firstNumber(5).secondNumber(3).result(8).operator('+').build());
        }
        for (int i = 0; i < 60; i++) {
            equations.add(Equation.builder().firstNumber(9).secondNumber(3).result(3).operator('÷').build());
        }
        System.setProperty("outputBaseName", "Columns");
        System.setProperty("outputSuffix", "_Multi");

        pdfService.generatePdf(equations, 99);

        File out = new File("Columns_99_Multi.pdf");
        assertTrue(out.exists());
        out.delete();
    }
}
