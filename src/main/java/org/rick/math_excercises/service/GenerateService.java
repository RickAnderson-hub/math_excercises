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

import org.rick.math_excercises.model.Equation;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

/**
 * Service responsible for generating math exercises.
 * Uses functional patterns with injectable RandomGenerator for testability.
 */
public class GenerateService {

	private static final List<Operations> DEFAULT_OPS = List.of(Operations.ADDITION, Operations.SUBTRACTION);

	private final RandomGenerator random;

	/**
	 * Default constructor uses thread-local randomness.
	 */
	public GenerateService() {
		this(ThreadLocalRandom.current());
	}

	/**
	 * Injectable randomness for deterministic tests.
	 */
	public GenerateService(RandomGenerator random) {
		this.random = random;
	}

	/**
	 * Generates math exercises using addition and subtraction by default. A limit is passed to
	 * determine the upper limit of the exercises.
	 *
	 * @param limit             The upper limit of the math exercises.
	 * @param numberOfExercises how many equations to generate
	 * @return list of generated equations using ADDITION and SUBTRACTION
	 */
	public List<Equation> generateExercises(int limit, int numberOfExercises) {
		return generateExercises(limit, numberOfExercises, DEFAULT_OPS);
	}

	/**
	 * Generates math exercises using the provided set of operations. If operations is null or empty,
	 * defaults to ADDITION and SUBTRACTION.
	 *
	 * <p>Constraints enforced by the generator:
	 *
	 * <ul>
	 *   <li>limit must be >= 10
	 *   <li>numberOfExercises must be >= 1
	 *   <li>operands and results are non-negative and within the provided limit
	 *   <li>division avoids zero divisors; multiplication avoids zero factors
	 * </ul>
	 *
	 * @param limit             The upper limit of the math exercises (must be >= 10)
	 * @param numberOfExercises how many equations to generate (must be >= 1)
	 * @param operations        collection of allowed operations; null/empty uses ADDITION and SUBTRACTION
	 * @return list of generated equations
	 * @throws IllegalArgumentException if limit {@code <} 10 or numberOfExercises {@code <} 1
	 */
	public List<Equation> generateExercises(
			int limit, int numberOfExercises, Collection<Operations> operations) {
		if (limit < 10) {
			throw new IllegalArgumentException("Limit must be greater than or equal to 10.");
		}
		if (numberOfExercises < 1) {
			throw new IllegalArgumentException("numberOfExercises must be >= 1");
		}

		List<Operations> ops = Optional.ofNullable(operations)
				.filter(c -> !c.isEmpty())
				.map(c -> c.stream().filter(Objects::nonNull).toList())
				.filter(list -> !list.isEmpty())
				.orElse(DEFAULT_OPS);

		return IntStream.range(0, numberOfExercises)
				.mapToObj(i -> generateEquationForOperations(limit, ops))
				.toList();
	}

	/**
	 * Picks a random operation from the provided set and generates an equation accordingly.
	 * Uses the functional generator on each Operations enum constant.
	 *
	 * @param limit upper bound for operands and results
	 * @param ops   non-empty list of allowed operations
	 * @return an {@link Equation} matching one of the allowed operations
	 */
	private Equation generateEquationForOperations(int limit, List<Operations> ops) {
		// Fast-path optimization for common ADDITION+SUBTRACTION case
		if (ops.size() == 2
				&& ops.contains(Operations.ADDITION)
				&& ops.contains(Operations.SUBTRACTION)) {
			return random.nextBoolean()
			       ? Operations.ADDITION.generate(limit, random)
			       : Operations.SUBTRACTION.generate(limit, random);
		}

		Operations op = ops.get(random.nextInt(ops.size()));
		return op.generate(limit, random);
	}
}
