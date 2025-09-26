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
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.rick.math_excercises.model.Equation;
import org.rick.math_excercises.service.internal.IoUtils;
import org.rick.math_excercises.service.internal.PdfRenderSupport;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**
 * Service responsible for rendering a list of {@link Equation} instances into a PDF document.
 */
@Slf4j
public class PdfService {

    private static final float BASE_FONT_SIZE = 12f;
    private static final float OPERATOR_FONT_SIZE = BASE_FONT_SIZE + 1f;
    private static final int LINES_PER_COLUMN = 50;

    private final Random random;

    /** Default constructor uses thread-local randomness. */
    public PdfService() { this(ThreadLocalRandom.current()); }

    /** Injectable randomness for deterministic tests. */
    public PdfService(Random random) { this.random = random; }

    public void generatePdf(List<Equation> equations, int iteration) {
        if (equations.isEmpty()) {
            throw new IllegalArgumentException("Equations list cannot be empty.");
        }
        String baseName = System.getProperty("outputBaseName", "MathExercises");
        String suffix = System.getProperty("outputSuffix", "");
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                PDFont font = loadFont(document);
                setupContentStream(contentStream, font);
                writeEquationsToContentStream(contentStream, equations, page, font);
            }
            document.save(baseName + "_" + iteration + suffix + ".pdf");
        } catch (IOException e) {
            log.info(e.getMessage(), e);
        }
    }

    private PDFont loadFont(PDDocument document) throws IOException {
        var stream = getClass().getResourceAsStream("/arialuni.ttf");
        if (stream != null) {
            try { return PDType0Font.load(document, stream); } catch (IOException e) { log.warn("Failed to load arialuni.ttf, falling back to Helvetica", e); }
        } else {
            log.warn("Font resource /arialuni.ttf not found; falling back to Helvetica");
        }
        return new org.apache.pdfbox.pdmodel.font.PDType1Font(Standard14Fonts.FontName.HELVETICA);
    }

    private void setupContentStream(PDPageContentStream contentStream, PDFont font) throws IOException {
        contentStream.setFont(font, BASE_FONT_SIZE);
        contentStream.setLeading(14.5f);
        contentStream.beginText();
    }

    private void writeEquationsToContentStream(PDPageContentStream contentStream, List<Equation> equations, PDPage page, PDFont font) throws IOException {
        final float margin = 50f;
        final float pageWidth = page.getMediaBox().getWidth();
        final float columnWidth = (pageWidth - (4 * margin)) / 3;
        final float startY = 725f;
        List<List<Equation>> columns = PdfRenderSupport.partition(equations, LINES_PER_COLUMN);

        IntStream.range(0, columns.size()).forEach(columnIndex -> IoUtils.safeIo(() -> {
            if (columnIndex > 0) {
                contentStream.endText();
                contentStream.beginText();
                contentStream.setFont(font, BASE_FONT_SIZE);
                contentStream.setLeading(14.5f);
            }
            float startX = margin + columnIndex * columnWidth;
            contentStream.newLineAtOffset(startX, startY);
            columns.get(columnIndex).forEach(eq -> IoUtils.safeIo(() -> {
                int placeholderIndex = 1 + random.nextInt(3);
                var tokens = PdfRenderSupport.formatTokens(eq, placeholderIndex);
                renderEquationLine(contentStream, font, tokens);
                contentStream.newLine();
            }));
        }));
        contentStream.endText();
    }

    private void renderEquationLine(PDPageContentStream contentStream, PDFont font, List<String> tokens) {
        IntStream.range(0, tokens.size()).forEach(i -> IoUtils.safeIo(() -> {
            String token = tokens.get(i);
            boolean operator = PdfRenderSupport.isOperatorToken(token);
            contentStream.setFont(font, operator ? OPERATOR_FONT_SIZE : BASE_FONT_SIZE);
            contentStream.showText(token);
            if (i < tokens.size() - 1) {
                contentStream.setFont(font, BASE_FONT_SIZE);
                contentStream.showText(" ");
            }
        }));
    }
}