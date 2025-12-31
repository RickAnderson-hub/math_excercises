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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.rick.math_excercises.model.Equation;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests filename customization logic in {@link PdfService} driven by the system properties {@code
 * outputBaseName} and {@code outputSuffix}.
 */
class PdfServiceFilenameTests {

	@AfterEach
	void cleanup() {
		// Clean possible files created by this test
		new File("Worksheets_7_Custom.pdf").delete();
		// Reset system properties
		System.clearProperty("outputBaseName");
		System.clearProperty("outputSuffix");
	}

	/**
	 * Verifies that providing custom base name and suffix via system properties results in an output
	 * PDF whose filename reflects both values.
	 */
	@Test
	void usesCustomBaseNameAndSuffix() {
		PdfService pdfService = new PdfService();
		List<Equation> equations =
				List.of(
						Equation.of(2, 3, 5, '+'),
						Equation.of(6, 2, 3, '÷'));
		System.setProperty("outputBaseName", "Worksheets");
		System.setProperty("outputSuffix", "_Custom");

		pdfService.generatePdf(equations, 7);

		File out = new File("Worksheets_7_Custom.pdf");
		assertTrue(out.exists());
		out.delete();
	}
}
