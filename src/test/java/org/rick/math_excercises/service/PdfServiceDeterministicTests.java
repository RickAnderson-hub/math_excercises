package org.rick.math_excercises.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.rick.math_excercises.model.Equation;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PdfServiceDeterministicTests {

    @AfterEach
    void cleanup() {
        new File("Deterministic_1.pdf").delete();
        new File("Deterministic_2.pdf").delete();
        System.clearProperty("outputBaseName");
        System.clearProperty("outputSuffix");
    }

    @Test
    void generatesDeterministicEquationLayoutForSameSeed() throws IOException {
        System.setProperty("outputBaseName", "Deterministic");
        System.setProperty("outputSuffix", "");
        List<Equation> equations = List.of(
                Equation.builder().firstNumber(6).secondNumber(2).result(8).operator('+').build(),
                Equation.builder().firstNumber(9).secondNumber(3).result(3).operator('÷').build(),
                Equation.builder().firstNumber(7).secondNumber(5).result(2).operator('-').build(),
                Equation.builder().firstNumber(3).secondNumber(4).result(12).operator('×').build(),
                Equation.builder().firstNumber(8).secondNumber(2).result(4).operator('÷').build()
        );

        PdfService a = new PdfService(new Random(12345));
        PdfService b = new PdfService(new Random(12345));

        a.generatePdf(equations, 1);
        b.generatePdf(equations, 2);

        assertTrue(new File("Deterministic_1.pdf").exists());
        assertTrue(new File("Deterministic_2.pdf").exists());

        String text1 = extractText("Deterministic_1.pdf");
        String text2 = extractText("Deterministic_2.pdf");
        assertEquals(text1, text2, "PDF text content should be identical for same Random seed");
    }

    private String extractText(String fileName) throws IOException {
        try (PDDocument doc = Loader.loadPDF(new File(fileName))) {
            PDFTextStripper stripper = new PDFTextStripper();
            // Normalize line endings and trim trailing whitespace
            return stripper.getText(doc).replace("\r\n", "\n").trim();
        }
    }
}
