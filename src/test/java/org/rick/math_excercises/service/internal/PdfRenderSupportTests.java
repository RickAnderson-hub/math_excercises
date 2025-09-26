package org.rick.math_excercises.service.internal;

import org.junit.jupiter.api.Test;
import org.rick.math_excercises.model.Equation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PdfRenderSupportTests {

    @Test
    void partitionEmpty() {
        assertTrue(PdfRenderSupport.partition(List.of(), 10).isEmpty());
    }

    @Test
    void partitionExact() {
        List<Integer> src = List.of(1,2,3,4,5,6);
        var parts = PdfRenderSupport.partition(src, 3);
        assertEquals(2, parts.size());
        assertEquals(List.of(1,2,3), parts.get(0));
        assertEquals(List.of(4,5,6), parts.get(1));
    }

    @Test
    void partitionRemainder() {
        List<Integer> src = List.of(1,2,3,4,5);
        var parts = PdfRenderSupport.partition(src, 2);
        assertEquals(3, parts.size());
        assertEquals(List.of(1,2), parts.get(0));
        assertEquals(List.of(3,4), parts.get(1));
        assertEquals(List.of(5), parts.get(2));
    }

    @Test
    void formatTokensEachPlaceholder() {
        Equation eq = Equation.builder().firstNumber(6).secondNumber(2).result(3).operator('÷').build();
        var t1 = PdfRenderSupport.formatTokens(eq, 1);
        var t2 = PdfRenderSupport.formatTokens(eq, 2);
        var t3 = PdfRenderSupport.formatTokens(eq, 3);
        assertEquals(List.of("□", "÷", "2", "=", "3"), t1);
        assertEquals(List.of("6", "÷", "□", "=", "3"), t2);
        assertEquals(List.of("6", "÷", "2", "=", "□"), t3);
    }

    @Test
    void formatTokensTokenCountAndOperators() {
        Equation eq = Equation.builder().firstNumber(5).secondNumber(7).result(12).operator('+').build();
        var tokens = PdfRenderSupport.formatTokens(eq, 2);
        assertEquals(5, tokens.size());
        assertEquals("+", tokens.get(1));
        assertEquals("=", tokens.get(3));
        assertTrue(PdfRenderSupport.isOperatorToken("+"));
        assertTrue(PdfRenderSupport.isOperatorToken("="));
        assertFalse(PdfRenderSupport.isOperatorToken("12"));
    }
}

