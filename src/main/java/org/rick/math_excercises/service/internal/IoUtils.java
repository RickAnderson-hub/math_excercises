/*
 * Math Exercises Generator — Shareware License
 * Copyright (c) 2025 Rick Anderson
 * Contact: rick@getanderson.net
 *
 * Personal, non-commercial use permitted. Redistribution allowed with attribution.
 * Any commercial use requires a paid license or prior written permission.
 * See the LICENSE file for full terms.
 */

package org.rick.math_excercises.service.internal;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Internal IO helper utilities to support functional style stream pipelines while still dealing
 * with checked {@link IOException}.
 */
public final class IoUtils {

  private IoUtils() {}

  /** Functional interface mirroring {@link Runnable} but allowing IOException. */
  @FunctionalInterface
  public interface IORunnable {
    void run() throws IOException;
  }

  /** Execute an IO action, rethrowing any {@link IOException} as {@link UncheckedIOException}. */
  public static void safeIo(IORunnable action) {
    try {
      action.run();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
