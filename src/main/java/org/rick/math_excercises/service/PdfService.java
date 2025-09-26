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

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.rick.math_excercises.model.Equation;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Service responsible for rendering a list of {@link Equation} instances into a PDF document.
 * <p>
 * The output filename can be customized via the JVM system properties:
 * <ul>
 *   <li><b>outputBaseName</b>: base filename (default: {@code MathExercises})</li>
 *   <li><b>outputSuffix</b>: suffix appended after the iteration (e.g., {@code _AddSub})</li>
 * </ul>
 * Layout: up to 50 equations per column, flowing across multiple columns on a single page.
 */
@Slf4j
public class PdfService {

    // Base font sizes
    private static final float BASE_FONT_SIZE = 12f;
    private static final float OPERATOR_FONT_SIZE = BASE_FONT_SIZE + 1f; // operators and '=' one point larger
    private static final int LINES_PER_COLUMN = 50;

    /**
     * Generates a single-page PDF file containing the provided equations.
     *
     * @param equations non-empty list of equations to render
     * @param iteration iteration number used in the output filename
     * @throws IllegalArgumentException if {@code equations} is empty
     */
    public void generatePdf(List<Equation> equations, int iteration) {
        if (equations.isEmpty()) {
            throw new IllegalArgumentException("Equations list cannot be empty.");
        }
        // Allow customizing output name via JVM system properties
        String baseName = System.getProperty("outputBaseName", "MathExercises");
        String suffix = System.getProperty("outputSuffix", ""); // e.g. _AddSub or _MulDiv
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                PDType0Font font = PDType0Font.load(document, getClass().getResourceAsStream("/arialuni.ttf"));

                setupContentStream(contentStream, font);
                writeEquationsToContentStream(contentStream, equations, page, font);
            }
            document.save(baseName + "_" + iteration + suffix + ".pdf");
        } catch (IOException e) {
            log.info(e.getMessage(), e);
        }
    }

    /**
     * Initializes the content stream with font, leading, and begins text mode.
     *
     * @param contentStream content stream for the current page
     * @param font loaded font to be used for text
     * @throws IOException if stream operations fail
     */
    private void setupContentStream(PDPageContentStream contentStream, PDType0Font font) throws IOException {
        contentStream.setFont(font, BASE_FONT_SIZE);
        contentStream.setLeading(14.5f);
        contentStream.beginText();
    }

    /**
     * Writes equations onto the page content stream, flowing across columns.
     *
     * @param contentStream the page content stream
     * @param equations the equations to render
     * @param page the current PDF page
     * @param font the font to be used for rendering text
     * @throws IOException if a PDFBox write operation fails
     */
    private void writeEquationsToContentStream(PDPageContentStream contentStream, List<Equation> equations, PDPage page, PDType0Font font) throws IOException {
        final float margin = 50f;
        final float pageWidth = page.getMediaBox().getWidth();
        final float columnWidth = (pageWidth - (4 * margin)) / 3; // unchanged layout calc

        // Partition equations into columns of up to LINES_PER_COLUMN
        List<List<Equation>> columns = partition(equations, LINES_PER_COLUMN);

        IntStream.range(0, columns.size()).forEach(columnIndex -> {
            float xTranslation = (columnIndex == 0) ? margin : columnWidth;
            try {
                contentStream.newLineAtOffset(xTranslation, 725);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            columns.get(columnIndex).forEach(eq -> {
                try {
                    renderEquationLine(contentStream, font, formatEquationTokens(eq));
                    contentStream.newLine();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        });
        contentStream.endText();
    }

    // Partition a list into fixed-size chunks (last may be smaller)
    private static <T> List<List<T>> partition(List<T> source, int size) {
        if (source.isEmpty()) return List.of();
        return IntStream.range(0, (source.size() + size - 1) / size)
                .mapToObj(chunkIndex -> source.subList(chunkIndex * size, Math.min(source.size(), (chunkIndex + 1) * size)))
                .collect(Collectors.toList());
    }

    // Build tokens for an equation with a randomly positioned placeholder
    private List<String> formatEquationTokens(Equation equation) {
        int randomIndex = 1 + ThreadLocalRandom.current().nextInt(3); // 1->first,2->second,3->result placeholder
        String first = randomIndex == 1 ? "□" : String.valueOf(equation.getFirstNumber());
        String second = randomIndex == 2 ? "□" : String.valueOf(equation.getSecondNumber());
        String result = randomIndex == 3 ? "□" : String.valueOf(equation.getResult());
        return List.of(first, String.valueOf(equation.getOperator()), second, "=", result);
    }

    private void renderEquationLine(PDPageContentStream contentStream, PDType0Font font, List<String> tokens) throws IOException {
        IntStream.range(0, tokens.size()).forEach(i -> {
            String token = tokens.get(i);
            boolean operator = isOperatorToken(token);
            try {
                contentStream.setFont(font, operator ? OPERATOR_FONT_SIZE : BASE_FONT_SIZE);
                contentStream.showText(token);
                if (i < tokens.size() - 1) {
                    contentStream.setFont(font, BASE_FONT_SIZE);
                    contentStream.showText(" ");
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }

    private boolean isOperatorToken(String token) {
        return "+".equals(token) || "-".equals(token) || "×".equals(token) || "÷".equals(token) || "=".equals(token);
    }
}