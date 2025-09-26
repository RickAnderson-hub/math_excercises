package org.rick.math_excercises.service.internal;

import org.rick.math_excercises.model.Equation;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Internal rendering support helpers extracted for reuse and unit testing.
 */
public final class PdfRenderSupport {

    private PdfRenderSupport() {}

    /** Partition a list into sublists of at most {@code size}. */
    public static <T> List<List<T>> partition(List<T> source, int size) {
        Objects.requireNonNull(source, "source");
        if (size <= 0) throw new IllegalArgumentException("size must be > 0");
        if (source.isEmpty()) return List.of();
        return IntStream.range(0, (source.size() + size - 1) / size)
                .mapToObj(chunkIndex -> source.subList(chunkIndex * size, Math.min(source.size(), (chunkIndex + 1) * size)))
                .collect(Collectors.toList());
    }

    /** Build display tokens for an equation. placeholderIndex: 1=first operand, 2=second operand, 3=result. */
    public static List<String> formatTokens(Equation equation, int placeholderIndex) {
        if (placeholderIndex < 1 || placeholderIndex > 3) throw new IllegalArgumentException("placeholderIndex must be 1..3");
        String first = placeholderIndex == 1 ? "□" : String.valueOf(equation.getFirstNumber());
        String second = placeholderIndex == 2 ? "□" : String.valueOf(equation.getSecondNumber());
        String result = placeholderIndex == 3 ? "□" : String.valueOf(equation.getResult());
        return List.of(first, String.valueOf(equation.getOperator()), second, "=", result);
    }

    /** Whether a token should be rendered using operator styling. */
    public static boolean isOperatorToken(String token) {
        return "+".equals(token) || "-".equals(token) || "×".equals(token) || "÷".equals(token) || "=".equals(token);
    }

    public static int choosePlaceholderIndex(Random r) { return 1 + r.nextInt(3); }

    public static List<Integer> placeholderSequence(Random r, int n) {
        if (n < 0) throw new IllegalArgumentException("n must be >= 0");
        return IntStream.range(0, n).map(i -> choosePlaceholderIndex(r)).boxed().toList();
    }
}
