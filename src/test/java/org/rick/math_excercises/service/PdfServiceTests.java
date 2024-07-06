package org.rick.math_excercises.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rick.math_excercises.model.Equation;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

class PdfServiceTests {

    @Mock
    private GenerateService generate;

    @InjectMocks
    private PdfService pdfService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Generate PDF with multiple equations successfully")
    void generatePdfWithMultipleEquations() {
        // Given
        List<Equation> equations = Arrays.asList(
                new Equation(1, '1', 2, '+'),
                new Equation(3, '1', 2, '-')
        );
        when(generate.generateExercises(10, 2)).thenReturn(equations);

        // When
        pdfService.generatePdf(equations);

        // Then
        File file = new File("MathExercises.pdf");
        assert file.exists();
        file.delete(); // Clean up
    }

    @Test
    @DisplayName("Generate PDF with no equations")
    void generatePdfWithNoEquations() {
        // Given
        List<Equation> emptyList = List.of();
        Assertions.assertThrows(IllegalArgumentException.class, () -> pdfService.generatePdf(emptyList));
    }
}