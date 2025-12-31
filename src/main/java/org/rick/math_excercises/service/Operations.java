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

import java.util.function.BiFunction;
import java.util.random.RandomGenerator;
import java.util.stream.Stream;

/**
 * Supported arithmetic operations for worksheet generation.
 * Each operation carries its operator character and a generator function.
 */
public enum Operations {
	/**
	 * Addition operation ( + ).
	 */
	ADDITION('+', Operations::generateAddition),
	/**
	 * Subtraction operation ( - ).
	 */
	SUBTRACTION('-', Operations::generateSubtraction),
	/**
	 * Multiplication operation ( × ).
	 */
	MULTIPLICATION('×', Operations::generateMultiplication),
	/**
	 * Division operation ( ÷ ).
	 */
	DIVISION('÷', Operations::generateDivision);

	private final char operator;
	private final BiFunction<Integer, RandomGenerator, Equation> generator;

	Operations(char operator, BiFunction<Integer, RandomGenerator, Equation> generator) {
		this.operator = operator;
		this.generator = generator;
	}

	/**
	 * Generates an addition equation where the sum does not exceed the limit
	 * and not both operands are zero.
	 */
	private static Equation generateAddition(int limit, RandomGenerator random) {
		return Stream.generate(() -> {
					int first = random.nextInt(limit);
					int second = random.nextInt(limit);
					return new int[]{first, second};
				})
				.filter(nums -> nums[0] + nums[1] <= limit && !(nums[0] == 0 && nums[1] == 0))
				.findFirst()
				.map(nums -> Equation.of(nums[0], nums[1], nums[0] + nums[1], '+'))
				.orElseThrow();
	}

	/**
	 * Generates a subtraction equation where the result is non-negative and within the limit.
	 * Both operands are within [0, limit], but not simultaneously zero.
	 */
	private static Equation generateSubtraction(int limit, RandomGenerator random) {
		return Stream.generate(() -> {
					int first = random.nextInt(limit);
					int second = random.nextInt(limit);
					return new int[]{first, second};
				})
				.filter(nums -> nums[0] - nums[1] >= 0 && !(nums[0] == 0 && nums[1] == 0))
				.findFirst()
				.map(nums -> Equation.of(nums[0], nums[1], nums[0] - nums[1], '-'))
				.orElseThrow();
	}

	/**
	 * Generates a multiplication equation avoiding zero operands and ensuring the product
	 * is within the limit.
	 */
	private static Equation generateMultiplication(int limit, RandomGenerator random) {
		return Stream.generate(() -> {
					int first = random.nextInt(limit);
					int second = random.nextInt(limit);
					return new int[]{first, second};
				})
				.filter(nums -> nums[0] != 0 && nums[1] != 0 && nums[0] * nums[1] <= limit)
				.findFirst()
				.map(nums -> Equation.of(nums[0], nums[1], nums[0] * nums[1], '×'))
				.orElseThrow();
	}

	/**
	 * Generates a division equation with integer quotient and no zero divisors.
	 * The dividend remains within the given limit.
	 */
	private static Equation generateDivision(int limit, RandomGenerator random) {
		return Stream.generate(() -> {
					int divisor = 1 + random.nextInt(limit - 1); // Avoid zero
					int quotient = random.nextInt(limit);
					int dividend = divisor * quotient;
					return new int[]{dividend, divisor, quotient};
				})
				.filter(nums -> nums[0] <= limit)
				.findFirst()
				.map(nums -> Equation.of(nums[0], nums[1], nums[2], '÷'))
				.orElseThrow();
	}

	/**
	 * Generates an equation for this operation within the given limit.
	 *
	 * @param limit  upper bound for operands and results
	 * @param random the random generator to use
	 * @return a valid Equation for this operation
	 */
	public Equation generate(int limit, RandomGenerator random) {
		return generator.apply(limit, random);
	}
}
