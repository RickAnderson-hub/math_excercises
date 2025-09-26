package org.rick.math_excercises.service.internal;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Internal IO helper utilities to support functional style stream pipelines
 * while still dealing with checked {@link IOException}.
 */
public final class IoUtils {

    private IoUtils() {}

    /** Functional interface mirroring {@link Runnable} but allowing IOException. */
    @FunctionalInterface
    public interface IORunnable { void run() throws IOException; }

    /** Execute an IO action, rethrowing any {@link IOException} as {@link UncheckedIOException}. */
    public static void safeIo(IORunnable action) {
        try {
            action.run();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

