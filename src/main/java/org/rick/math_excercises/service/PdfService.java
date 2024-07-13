package org.rick.math_excercises.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.rick.math_excercises.model.Equation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Random;

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
                float margin = 50;
                final float pageWidth = page.getMediaBox().getWidth();
                final float columnWidth = (pageWidth - ( 4 * margin)) / 3; // 3 columns with equal spacing
                int columns = (equations.size() + 49) / 50;
                float yOffset = 725;
                float xOffset = margin;
                int lineCounter = Math.min(50, equations.size());
                int arrayIndex = 0;
                contentStream.beginText();
                for (int i = 1; i <= columns; i++) {
                    PDType0Font font = PDType0Font.load(document, getClass().getResourceAsStream("/arialuni.ttf"));
                    contentStream.setFont(font, 12);
                    contentStream.setLeading(14.5f);
                    contentStream.newLineAtOffset(xOffset, yOffset);
                    for (int j = 0; j < lineCounter; j++) {
                        Equation equation = equations.get(arrayIndex++);
                        String equationText = getEquationWithPlaceHolder(equation);
                        contentStream.showText(equationText);
                        contentStream.newLine();
                    }
                    lineCounter = Math.min(equations.size() - i * lineCounter, 50);
                    xOffset = columnWidth;
                }
                contentStream.endText();
            }
            document.save("MathExercises.pdf");
        } catch (IOException e) {
            log.info(e.getMessage(), e);
        }
    }

    private String getEquationWithPlaceHolder(Equation equation) {
        int randomIndex = 1 + new Random().nextInt(3);
        String firstNumber = randomIndex == 1 ? "\u25A1" : String.valueOf(equation.getFirstNumber());
        String secondNumber = randomIndex == 2 ? "\u25A1" : String.valueOf(equation.getSecondNumber());
        String result = randomIndex == 3 ? "\u25A1" : String.valueOf(equation.getResult());
        return firstNumber + " " + equation.getOperator() + " " + secondNumber + " = " + result;
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
