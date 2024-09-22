package org.rick.math_excercises.controller;

import org.rick.math_excercises.model.Equation;
import org.rick.math_excercises.service.GenerateService;
import org.rick.math_excercises.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    @Autowired
    private GenerateService generateService;

    @Autowired
    private PdfService pdfService;

    @GetMapping("/generate")
    public String generatePdf(@RequestParam int limit, @RequestParam int numberOfExercises, @RequestParam int iterations) {
        for (int i = 1; i <= iterations; i++) {
            List<Equation> equations = generateService.generateExercises(limit, numberOfExercises);
            pdfService.generatePdf(equations, i);
        }
        return "PDFs generated successfully";
    }
}