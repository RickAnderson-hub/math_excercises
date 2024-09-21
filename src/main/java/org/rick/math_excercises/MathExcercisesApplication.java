package org.rick.math_excercises;

import org.rick.math_excercises.model.Equation;
import org.rick.math_excercises.service.GenerateService;
import org.rick.math_excercises.service.PdfService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;

import java.util.List;


public class MathExcercisesApplication {

    private static final GenerateService generateService = new GenerateService();

    private static final PdfService pdfService = new PdfService();


    /**
     * Main method to run the application.
     * The limit is the upper limit of the exercises, e.g. 20.
     * The numberOfExercises is the number of exercises to generate, e.g. 200 fits onto an A4 page.
     * The iterations is the number of sheets to generate.
     *
     * @param args Command line arguments: <limit> <numberOfExercises> <iterations>
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MathExcercisesApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        if (args.length < 3) {
            System.out.println("Usage: java -jar math-excercises.jar <limit> <numberOfExercises> <iterations>");
            System.exit(1);
        }
        int limit = Integer.parseInt(args[0]);
        int numberOfExercises = Integer.parseInt(args[1]);
        int iterations = Integer.parseInt(args[2]);
        for (int i = 1; i <= iterations; i++) {
            List<Equation> equations = generateService.generateExercises(limit, numberOfExercises);
            pdfService.generatePdf(equations, i);
        }
    }
}
