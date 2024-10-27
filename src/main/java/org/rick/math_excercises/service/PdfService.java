package org.rick.math_excercises.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.rick.math_excercises.model.Equation;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Slf4j
public class PdfService {

    public void generatePdf(List<Equation> equations, int iteration) {
        if (equations.isEmpty()) {
            throw new IllegalArgumentException("Equations list cannot be empty.");
        }
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                PDType0Font font = PDType0Font.load(document, getClass().getResourceAsStream("/arialuni.ttf"));
                setupContentStream(contentStream, font);
                writeEquationsToContentStream(contentStream, equations, page);
            }
            document.save("MathExercises_" + iteration + ".pdf");
        } catch (IOException e) {
            log.info(e.getMessage(), e);
        }
    }

    private void setupContentStream(PDPageContentStream contentStream, PDType0Font font) throws IOException {
        contentStream.setFont(font, 12);
        contentStream.setLeading(14.5f);
        contentStream.beginText();
    }

    private void writeEquationsToContentStream(PDPageContentStream contentStream, List<Equation> equations, PDPage page) throws IOException {
        float margin = 50;
        final float pageWidth = page.getMediaBox().getWidth();
        final float columnWidth = (pageWidth - (4 * margin)) / 3;
        int columns = (equations.size() + 49) / 50;
        float yOffset = 725;
        final float[] xOffset = {margin};
        IntStream.range(0, columns).forEach(i -> {
            try {
                contentStream.newLineAtOffset(xOffset[0], yOffset);
                equations.stream()
                        .skip(i * 50L)
                        .limit(50)
                        .forEach(equation -> {
                            try {
                                contentStream.showText(getEquationWithPlaceHolder(equation));
                                contentStream.newLine();
                            } catch (IOException e) {
                                log.info(e.getMessage(), e);
                            }
                        });
                xOffset[0] = columnWidth;
            } catch (IOException e) {
                log.info(e.getMessage(), e);
            }
        });
        contentStream.endText();
    }

    private String getEquationWithPlaceHolder(Equation equation) {
        int randomIndex = 1 + new Random().nextInt(3);
        String firstNumber = randomIndex == 1 ? "□" : String.valueOf(equation.getFirstNumber());
        String secondNumber = randomIndex == 2 ? "□" : String.valueOf(equation.getSecondNumber());
        String result = randomIndex == 3 ? "□" : String.valueOf(equation.getResult());
        return firstNumber + " " + equation.getOperator() + " " + secondNumber + " = " + result;
    }
}