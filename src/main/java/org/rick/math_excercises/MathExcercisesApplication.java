package org.rick.math_excercises;

import org.rick.math_excercises.model.Equation;
import org.rick.math_excercises.service.GenerateService;
import org.rick.math_excercises.service.PdfService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class MathExcercisesApplication {


    private static final GenerateService generateService = new GenerateService();

    private static final PdfService pdfService = new PdfService();

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MathExcercisesApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        if (args.length < 2) {
            System.out.println("Usage: java -jar math-excercises.jar <limit> <numberOfExercises>");
            System.exit(1);
        }
        int limit = Integer.parseInt(args[0]);
        int numberOfExercises = Integer.parseInt(args[1]);
        List<Equation> equations = generateService.generateExercises(limit, numberOfExercises);
        pdfService.generatePdf(equations);
    }
}
