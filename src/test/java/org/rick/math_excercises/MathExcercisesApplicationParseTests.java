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

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.rick.math_excercises.service.Operations;

/**
 * Unit tests for the hidden operation parsing logic in {@link MathExcercisesApplication}.
 *
 * <p>These tests validate:
 *
 * <ul>
 *   <li>Case-insensitive, comma/whitespace-tolerant parsing of operations strings.
 *   <li>Defaulting to ADDITION and SUBTRACTION when the input is blank.
 *   <li>Proper exception throwing on unknown operation tokens.
 * </ul>
 */
class MathExcercisesApplicationParseTests {

  @SuppressWarnings("unchecked")
  private static Set<Operations> invokeParse(String arg) {
    try {
      Method m =
          MathExcercisesApplication.class.getDeclaredMethod("parseOperationsArg", String.class);
      m.setAccessible(true);
      return (Set<Operations>) m.invoke(null, arg);
    } catch (InvocationTargetException e) {
      Throwable cause = e.getTargetException();
      if (cause instanceof RuntimeException re) {
        throw re;
      }
      throw new RuntimeException(cause);
    } catch (NoSuchMethodException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Verifies that a comma-separated, mixed-case list with surrounding whitespace is parsed into the
   * expected distinct {@link Operations} without duplication.
   */
  @Test
  void parsesCommaSeparatedOperationsCaseInsensitive() {
    Set<Operations> ops = invokeParse(" addition, DIVISION ");
    assertTrue(ops.contains(Operations.ADDITION));
    assertTrue(ops.contains(Operations.DIVISION));
    assertEquals(2, ops.size());
  }

  /**
   * Ensures a blank or whitespace-only argument falls back to the default ADDITION & SUBTRACTION
   * set.
   */
  @Test
  void defaultsToAddSubOnBlank() {
    Set<Operations> ops = invokeParse("   ");
    assertTrue(ops.contains(Operations.ADDITION));
    assertTrue(ops.contains(Operations.SUBTRACTION));
    assertEquals(2, ops.size());
  }

  /**
   * Confirms that supplying an unrecognized symbolic name results in an {@link
   * IllegalArgumentException}.
   */
  @Test
  void throwsOnUnknownOperation() {
    assertThrows(IllegalArgumentException.class, () -> invokeParse("FOO"));
  }
}
