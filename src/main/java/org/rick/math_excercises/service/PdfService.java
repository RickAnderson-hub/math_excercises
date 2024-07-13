package org.rick.math_excercises.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.rick.math_excercises.model.Equation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static org.apache.pdfbox.pdmodel.font.Standard14Fonts.FontName.HELVETICA;

/**
 * This class is responsible for generating PDF files.
 */
@Service
@Slf4j
public class PdfService {

    @Autowired
    private GenerateService generate;

    /**
     * Generates a PDF file.
     *
     * @param equations The exercises to be included in the PDF file.
     */
    public void generatePdf(List<Equation> equations) {
        if (equations.isEmpty()) {
            throw new IllegalArgumentException("Equations list cannot be empty.");
        }
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float margin = 25;
                final float pageWidth = page.getMediaBox().getWidth();
                final float columnWidth = (pageWidth - (4 * margin)) / 3; // 3 columns with equal spacing
                int columns = equations.size() / 50 == 0 ? 1 : equations.size() / 50;
                float yOffset = 725;
                float xOffset = margin;
                int lineCounter = 50;
                contentStream.beginText();
                for (int i = 0; i <= columns; i++) {
                    contentStream.setFont(new PDType1Font(HELVETICA), 12);
                    contentStream.setLeading(14.5f);
                    contentStream.newLineAtOffset(xOffset, yOffset);
                    int equationIndex = i == 0 ? 0 : i * 50;
                    for (int j = equationIndex; j < equationIndex + lineCounter; j++) {
                        Equation equation = equations.get(j);
                        String equationText = equation.getFirstNumber() + " " + equation.getOperator() + " " + equation.getSecondNumber() + " = " + equation.getResult();
                        contentStream.showText(equationText);
                        contentStream.newLine();
                    }
                    xOffset += columnWidth + margin;
                    lineCounter = equations.size() - lineCounter > 50 ? equations.size() - 50 : equations.size() - lineCounter;
                }
                contentStream.endText();

            }
            document.save("MathExercises.pdf");
        } catch (IOException e) {
            log.info(e.getMessage(), e);
        }
    }

    /**
     * Generates a PDF file with exercises.  Used from the CLI.
     *
     * @param limit             The upper limit of the exercises.
     * @param numberOfExercises The number of exercises to generate.
     */
    public void cliPdf(int limit, int numberOfExercises) {
        List<Equation> equations = generate.generateExercises(limit, numberOfExercises);
        generatePdf(equations);
    }


}
