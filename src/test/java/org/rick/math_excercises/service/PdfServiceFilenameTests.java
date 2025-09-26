package org.rick.math_excercises.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.rick.math_excercises.model.Equation;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PdfServiceFilenameTests {

    @AfterEach
    void cleanup() {
        // Clean possible files created by this test
        new File("Worksheets_7_Custom.pdf").delete();
        // Reset system properties
        System.clearProperty("outputBaseName");
        System.clearProperty("outputSuffix");
    }

    @Test
    void usesCustomBaseNameAndSuffix() {
        PdfService pdfService = new PdfService();
        List<Equation> equations = List.of(
                Equation.builder().firstNumber(2).secondNumber(3).result(5).operator('+').build(),
                Equation.builder().firstNumber(6).secondNumber(2).result(3).operator('÷').build()
        );
        System.setProperty("outputBaseName", "Worksheets");
        System.setProperty("outputSuffix", "_Custom");

        pdfService.generatePdf(equations, 7);

        File out = new File("Worksheets_7_Custom.pdf");
        assertTrue(out.exists());
        out.delete();
    }
}
