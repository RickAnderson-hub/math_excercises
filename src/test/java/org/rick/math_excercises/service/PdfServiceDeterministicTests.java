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

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.rick.math_excercises.model.Equation;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests deterministic behavior of {@link PdfService} when supplied with seeded {@link
 * java.util.Random} instances. Ensures identical equation token placement (including placeholder
 * positions) produces byte-equivalent extracted text.
 */
class PdfServiceDeterministicTests {

	@AfterEach
	void cleanup() {
		new File("Deterministic_1.pdf").delete();
		new File("Deterministic_2.pdf").delete();
		System.clearProperty("outputBaseName");
		System.clearProperty("outputSuffix");
	}

	/**
	 * Generates two PDFs using different {@link PdfService} instances sharing the same PRNG seed and
	 * asserts that their extracted textual content matches exactly.
	 */
	@Test
	void generatesDeterministicEquationLayoutForSameSeed() throws IOException {
		System.setProperty("outputBaseName", "Deterministic");
		System.setProperty("outputSuffix", "");
		List<Equation> equations =
				List.of(
						Equation.of(6, 2, 8, '+'),
						Equation.of(9, 3, 3, '÷'),
						Equation.of(7, 5, 2, '-'),
						Equation.of(3, 4, 12, '×'),
						Equation.of(8, 2, 4, '÷'));

		PdfService a = new PdfService(new Random(12345));
		PdfService b = new PdfService(new Random(12345));

		a.generatePdf(equations, 1);
		b.generatePdf(equations, 2);

		assertTrue(new File("Deterministic_1.pdf").exists());
		assertTrue(new File("Deterministic_2.pdf").exists());

		String text1 = extractText("Deterministic_1.pdf");
		String text2 = extractText("Deterministic_2.pdf");
		assertEquals(text1, text2, "PDF text content should be identical for same Random seed");
	}

	private String extractText(String fileName) throws IOException {
		try (PDDocument doc = Loader.loadPDF(new File(fileName))) {
			PDFTextStripper stripper = new PDFTextStripper();
			// Normalize line endings and trim trailing whitespace
			return stripper.getText(doc).replace("\r\n", "\n").trim();
		}
	}
}
