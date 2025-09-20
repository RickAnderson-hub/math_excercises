/*
 * Math Exercises Generator — Shareware License
 * Copyright (c) 2025 Rick Anderson
 * Contact: rick@getanderson.net
 *
 * Personal, non-commercial use permitted. Redistribution allowed with attribution.
 * Any commercial use requires a paid license or prior written permission.
 * See the LICENSE file for full terms.
 */

package org.rick.math_excercises;

import org.rick.math_excercises.model.Equation;
import org.rick.math_excercises.service.GenerateService;
import org.rick.math_excercises.service.Operations;
import org.rick.math_excercises.service.PdfService;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;


public class MathExcercisesApplication {

    private static final GenerateService generateService = new GenerateService();

    private static final PdfService pdfService = new PdfService();

    /**
     * Main method to run the application.
     * The limit is the upper limit of the exercises, e.g. 20.
     * The numberOfExercises is the number of exercises to generate, e.g. 200 fits onto an A4 page.
     * The iterations is the number of sheets to generate.
     * Optional 4th arg: comma-separated list of operations (ADDITION,SUBTRACTION,MULTIPLICATION,DIVISION)
     *
     * @param args Command line arguments: <limit> <numberOfExercises> <iterations> [operations]
     */
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("""
                    Usage: java -jar math-excercises.jar <limit> <numberOfExercises> <iterations> [operations]
                      operations: comma-separated list using names ADDITION,SUBTRACTION,MULTIPLICATION,DIVISION
                      examples: ADDITION,SUBTRACTION or MULTIPLICATION,DIVISION or ADDITION,SUBTRACTION,MULTIPLICATION,DIVISION""");
            System.exit(1);
        }
        int limit = Integer.parseInt(args[0]);
        int numberOfExercises = Integer.parseInt(args[1]);
        int iterations = Integer.parseInt(args[2]);

        final List<Operations> operations;
        if (args.length >= 4) {
            operations = new ArrayList<>(parseOperationsArg(args[3]));
        } else {
            operations = List.of(Operations.ADDITION, Operations.SUBTRACTION);
        }

        for (int i = 1; i <= iterations; i++) {
            List<Equation> equations = generateService.generateExercises(limit, numberOfExercises, operations);
            pdfService.generatePdf(equations, i);
        }
    }

    private static Set<Operations> parseOperationsArg(String arg) {
        if (arg == null || arg.isBlank()) {
            return EnumSet.of(Operations.ADDITION, Operations.SUBTRACTION);
        }
        String[] parts = arg.split(",");
        EnumSet<Operations> set = EnumSet.noneOf(Operations.class);
        for (String p : parts) {
            String key = p.trim().toUpperCase(Locale.ROOT);
            if (key.isEmpty()) continue;
            try {
                set.add(Operations.valueOf(key));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Unknown operation: " + p + ". Allowed: ADDITION,SUBTRACTION,MULTIPLICATION,DIVISION");
            }
        }
        if (set.isEmpty()) {
            return EnumSet.of(Operations.ADDITION, Operations.SUBTRACTION);
        }
        return set;
    }
}
