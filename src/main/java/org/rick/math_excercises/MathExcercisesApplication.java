package org.rick.math_excercises;

import org.rick.math_excercises.model.Equation;
import org.rick.math_excercises.service.GenerateService;
import org.rick.math_excercises.service.PdfService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

@SpringBootApplication
public class MathExcercisesApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MathExcercisesApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        ConfigurableApplicationContext context = app.run(args);
        if (args.length < 2) {
            System.out.println("Usage: java -jar math-excercises.jar <limit> <numberOfExercises>");
            System.exit(1);
        }
        int limit = Integer.parseInt(args[1]);
        int numberOfExercises = Integer.parseInt(args[2]);
        GenerateService generateService = context.getBean(GenerateService.class);
        PdfService pdfService = context.getBean(PdfService.class);
        List<Equation> equations = generateService.generateExercises(limit, numberOfExercises);
        pdfService.generatePdf(equations);
    }
}
