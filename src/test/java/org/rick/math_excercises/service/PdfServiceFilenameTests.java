package org.rick.math_excercises.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.rick.math_excercises.model.Equation;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests filename customization logic in {@link PdfService} driven by the system properties
 * {@code outputBaseName} and {@code outputSuffix}.
 */
class PdfServiceFilenameTests {

    @AfterEach
    void cleanup() {
        // Clean possible files created by this test
        new File("Worksheets_7_Custom.pdf").delete();
        // Reset system properties
        System.clearProperty("outputBaseName");
        System.clearProperty("outputSuffix");
    }

    /**
     * Verifies that providing custom base name and suffix via system properties results in an output
     * PDF whose filename reflects both values.
     */
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
